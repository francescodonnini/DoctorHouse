package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public class Canceled implements AppointmentState {
    private final CanceledInfo info;

    public Canceled(CanceledInfo info) {
        this.info = info;
    }

    @Override
    public CanceledInfo getInfo() {
        return info;
    }

    @Override
    public void cancel(AppointmentImpl appointment, User initiator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void confirm(AppointmentImpl appointment, User initiator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reschedule(AppointmentImpl appointment, LocalDateTime newDate, User initiator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void tick(AppointmentImpl appointment, LocalDateTime currentDate) {
        throw new UnsupportedOperationException();
    }
}