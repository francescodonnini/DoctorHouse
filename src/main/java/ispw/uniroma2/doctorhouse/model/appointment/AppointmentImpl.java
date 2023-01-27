package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public class AppointmentImpl implements Appointment {
    private AppointmentState state;

    public AppointmentImpl(AppointmentInfo info) {
        if (info instanceof CanceledInfo) {
            state = new Canceled((CanceledInfo) info);
        } else if (info instanceof PendingInfo) {
            state = new Pending((PendingInfo) info);
        } else {
            state = new Scheduled((ScheduledInfo) info);
        }
    }

    @Override
    public void confirm(User initiator) {
        state.confirm(this, initiator);
    }

    @Override
    public void cancel(User initiator) {
        state.cancel(this, initiator);
    }

    @Override
    public void reschedule(User initiator, LocalDateTime newDate) {
        state.reschedule(this, newDate, initiator);
    }

    @Override
    public AppointmentInfo getInfo() {
        return state.getInfo();
    }

    public void setState(AppointmentState state) {
        this.state = state;
    }
}