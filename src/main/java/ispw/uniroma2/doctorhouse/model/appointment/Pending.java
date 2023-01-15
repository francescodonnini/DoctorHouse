package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDate;

public class Pending implements AppointmentState {
    private final PendingInfo info;

    public Pending(PendingInfo info) {
        this.info = info;
    }

    @Override
    public AppointmentInfo getInfo() {
        return info;
    }

    @Override
    public void cancel(AppointmentImpl appointment, User cancelee) {
        CanceledInfo newInfo = new CanceledInfo(info.getDoctor(), info.getPatient(), info.getSpecialty(), info.getOffice(), cancelee);
        AppointmentState newState = new Canceled(newInfo);
        appointment.setState(newState);
    }

    @Override
    public void confirm(AppointmentImpl appointment, User confirmee) {
        ScheduledInfo newInfo = new ScheduledInfo(info.getDoctor(), info.getPatient(), info.getSpecialty(), info.getOffice(), info.getNewDate());
        AppointmentState newState = new Scheduled(newInfo);
        appointment.setState(newState);
    }

    @Override
    public void reschedule(AppointmentImpl appointment, User reschedulee, LocalDate newDate) {
        throw new UnsupportedOperationException();
    }
}