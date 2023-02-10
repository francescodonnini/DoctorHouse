package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public class Incoming implements AppointmentState {
    private final IncomingInfo info;

    public Incoming(IncomingInfo info) {
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
        PendingInfo newStateInfo = new PendingInfo(info.getDateTime(), newDate, info.getInterval().getDuration(), initiator);
        PendingState newState = new PendingState(newStateInfo);
        appointment.setState(newState);
    }
}
