package ispw.uniroma2.doctorhouse.rearrange;

import ispw.uniroma2.doctorhouse.beans.AppointmentBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.slot.SlotDao;
import ispw.uniroma2.doctorhouse.model.ClockInterval;
import ispw.uniroma2.doctorhouse.model.DateTimeInterval;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.Session;
import ispw.uniroma2.doctorhouse.model.Shift;
import ispw.uniroma2.doctorhouse.model.TakenSlot;
import ispw.uniroma2.doctorhouse.model.TimeInterval;
import ispw.uniroma2.doctorhouse.model.TimeIntervalSet;
import ispw.uniroma2.doctorhouse.model.appointment.Appointment;
import ispw.uniroma2.doctorhouse.model.appointment.AppointmentMemento;
import ispw.uniroma2.doctorhouse.model.appointment.IncomingInfo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AskForRearrangeImpl implements AskForRearrange {
    private final AppointmentDao appointmentDao;
    private final SlotDao slotDao;
    private final OfficeDao officeDao;
    private final Map<AppointmentBean, Appointment> appointmentMap;

    public AskForRearrangeImpl(AppointmentDao appointmentDao, SlotDao slotDao, OfficeDao officeDao) {
        this.appointmentDao = appointmentDao;
        this.slotDao = slotDao;
        this.officeDao = officeDao;
        appointmentMap = new HashMap<>();
    }

    /**
     * tries to rearrange the appointment to the date newDate
     * @param bean                      bean of the appointment the user want to rearrange
     * @param newDate                   date that the user want to rearrange the appointment to
     * @throws InvalidTimeSlot          if it is not possible to rearrange the appointment on that date - e.g. the time slot has already
     *                                  been taken by another user.
     * @throws PersistentLayerException if there is some kind of error communicating with the DAOs
     */
    @Override
    public void ask(AppointmentBean bean, LocalDateTime newDate) throws InvalidTimeSlot, PersistentLayerException {
        Appointment appointment = appointmentMap.get(bean);
        AppointmentMemento memento = appointment.createMemento();
        appointment.reschedule(Session.getSession().getUser(), newDate);
        try {
            appointmentDao.save(appointment);
        } catch (PersistentLayerException | InvalidTimeSlot e) {
            appointment.restore(memento);
            throw e;
        }
    }

    /**
     *
     * @param bean                      bean of the appointment the user want to rearrange
     * @param start                     beginning of the date interval the user would like to rearrange the appointment
     * @param endInclusive              end of the date interval the user would like to rearrange the appointment
     * @return                          a list of DateTimeInterval objects that the user can choose to rearrange the appointment
     * @throws PersistentLayerException if there is some kind of error communicating with the DAOs
     */
    @Override
    public List<DateTimeInterval> getFreeSlots(AppointmentBean bean, LocalDate start, LocalDate endInclusive) throws PersistentLayerException {
        Optional<Office> optionalOffice = officeDao.getOffice(bean.getOffice());
        if (optionalOffice.isEmpty()) {
            return List.of();
        }
        Office office = optionalOffice.get();
        Duration specialtyDuration = bean.getSpecialty().getDuration();
        Map<LocalDate, List<TakenSlot>> slots = new HashMap<>();
        for (TakenSlot slot : slotDao.getSlots(bean.getOffice())) {
            slots.computeIfAbsent(slot.getDateTime().toLocalDate(), m -> new ArrayList<>()).add(slot);
        }
        List<DateTimeInterval> freeSlots = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(endInclusive); date = date.plusDays(1)) {
            Optional<Shift> optionalShift = office.getShift(date);
            if (optionalShift.isEmpty()) {
                continue;
            }
            Shift shift = optionalShift.get();
            for (ClockInterval interval : shift.getIntervals()) {
                TimeIntervalSet set = new TimeIntervalSet(interval);
                for (TakenSlot slot : slots.getOrDefault(date, List.of())) {
                    set.add(slot.getInterval());
                }
                for (TimeInterval i : set.complementary()) {
                    makeDateTimeInterval(date, i.getStart(), i.getDuration(), specialtyDuration).ifPresent(freeSlots::add);
                }
            }
        }
        return freeSlots;
    }

    private Optional<DateTimeInterval> makeDateTimeInterval(LocalDate date, LocalTime startTime, Duration intervalDuration, Duration specialtyDuration) {
        if (specialtyDuration.compareTo(intervalDuration) <= 0) {
            return Optional.of(new DateTimeInterval(LocalDateTime.of(date, startTime), intervalDuration));
        }
        return Optional.empty();
    }

    /**
     * @return a list of incoming appointments where the user participates
     * @throws PersistentLayerException if there is some kind of error communicating with the DAOs
     */
    @Override
    public List<AppointmentBean> getIncomingAppointments() throws PersistentLayerException {
        appointmentMap.clear();
        UserBean participant = new UserBean();
        participant.setEmail(Session.getSession().getUser().getEmail());
        appointmentDao.find(participant, IncomingInfo.class).forEach(a -> appointmentMap.put(new AppointmentBeanAdapter(a.getInfo()), a));
        return new ArrayList<>(appointmentMap.keySet());
    }
}
