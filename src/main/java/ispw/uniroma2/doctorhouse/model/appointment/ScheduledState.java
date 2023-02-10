package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public class ScheduledState implements AppointmentState {
    private final ScheduledInfo info;

    public ScheduledState(ScheduledInfo info) {
        this.info = info;
    }

    @Override
    public AppointmentInfo getInfo() {
        return info;
    }

    @Override
    public void cancel(AppointmentImpl appointment, User initiator) {
        CanceledInfo newStateInfo = new CanceledInfo(info.getDate(), initiator);
        CanceledState newState = new CanceledState(newStateInfo);
        appointment.setState(newState);
    }

    @Override
    public void reschedule(AppointmentImpl appointment, LocalDateTime newDate, User initiator) {
        PendingInfo newStateInfo = new PendingInfo(info.getDate(), newDate, info.getDuration(), initiator);
        PendingState newState = new PendingState(newStateInfo);
        appointment.setState(newState);
    }
}
