package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

public class PendingState implements AppointmentState {
    private final PendingInfo info;

    public PendingState(PendingInfo info) {
        this.info = info;
    }

    @Override
    public AppointmentInfo getInfo() {
        return info;
    }

    @Override
    public void cancel(AppointmentImpl appointment, User initiator) {
        if (!initiator.equals(info.getInitiator())) {
            CanceledInfo newInfo = new CanceledInfo(info.getNewDate(), initiator);
            AppointmentState newState = new CanceledState(newInfo);
            appointment.setState(newState);
        }
    }

    @Override
    public void confirm(AppointmentImpl appointment, User initiator) {
        if (!initiator.equals(info.getInitiator())) {
            ScheduledInfo newInfo = new ScheduledInfo(info.getNewDate(), info.getDuration());
            AppointmentState newState = new ScheduledState(newInfo);
            appointment.setState(newState);
        }
    }
}
