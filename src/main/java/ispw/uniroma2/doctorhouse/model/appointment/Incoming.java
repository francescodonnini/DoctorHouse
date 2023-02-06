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
        Canceled newState = new Canceled(newStateInfo);
        appointment.setState(newState);
    }

    @Override
    public void reschedule(AppointmentImpl appointment, LocalDateTime newDate, User initiator) {
        PendingInfo newStateInfo = new PendingInfo(info.getDateTime(), newDate, info.getInterval().getDuration(), initiator);
        Pending newState = new Pending(newStateInfo);
        appointment.setState(newState);
    }

    @Override
    public void tick(AppointmentImpl appointment, LocalDateTime currentDate) {
        if (!info.getDate().isAfter(currentDate)) {
            ConsumedInfo consumedInfo = new ConsumedInfo(info.getDate());
            Consumed consumed = new Consumed(consumedInfo);
            appointment.setState(consumed);
        }
    }
}
