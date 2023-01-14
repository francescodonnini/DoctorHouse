package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDate;

public class Canceled implements AppointmentState {
    private final CanceledInfo info;

    public Canceled(CanceledInfo info) {
        this.info = info;
    }

    @Override
    public AppointmentInfo getInfo() {
        return info;
    }

    @Override
    public void cancel(AppointmentImpl appointment, User cancelee) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void confirm(AppointmentImpl appointment, User confirmee) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reschedule(AppointmentImpl appointment, User reschedulee, LocalDate newDate) {
        throw new UnsupportedOperationException();
    }
}
