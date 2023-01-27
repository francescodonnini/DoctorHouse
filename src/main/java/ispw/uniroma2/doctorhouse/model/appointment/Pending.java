package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

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
    public void cancel(AppointmentImpl appointment, User initiator) {
        if (!initiator.equals(info.getInitiator())) {
            CanceledInfo newInfo = new CanceledInfo(info.getDoctor(), info.getPatient(), info.getSpecialty(), info.getOffice(), info.getOldDate(), initiator);
            AppointmentState newState = new Canceled(newInfo);
            appointment.setState(newState);
        }
    }

    @Override
    public void confirm(AppointmentImpl appointment, User initiator) {
        if (!initiator.equals(info.getInitiator())) {
            ScheduledInfo newInfo = new ScheduledInfo(info.getDoctor(), info.getPatient(), info.getSpecialty(), info.getOffice(), info.getNewDate());
            AppointmentState newState = new Scheduled(newInfo);
            appointment.setState(newState);
        }
    }

    @Override
    public void reschedule(AppointmentImpl appointment, LocalDateTime newDate, User initiator) {
        throw new UnsupportedOperationException();
    }
}
