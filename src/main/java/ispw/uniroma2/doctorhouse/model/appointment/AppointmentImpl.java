package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.doctor.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.doctor.Specialty;

import java.time.LocalDate;

public class AppointmentImpl implements Appointment {
    private AppointmentState state;

    public AppointmentImpl(Doctor doctor, User patient, Specialty specialty, Office office, LocalDate date) {
        ScheduledInfo info = new ScheduledInfo(doctor, patient, specialty, office, date);
        state = new Scheduled(info);
    }

    @Override
    public void confirm(User confirmee) {
        state.confirm(this, confirmee);
    }

    @Override
    public void cancel(User cancelee) {
        state.cancel(this, cancelee);
    }

    @Override
    public void reschedule(User reschedulee, LocalDate newDate) {
        state.reschedule(this, reschedulee, newDate);
    }

    @Override
    public AppointmentInfo getInfo() {
        return state.getInfo();
    }

    @Override
    public Memento createMemento() {
        Memento memento = new Memento();
        memento.setState(state);
        return memento;
    }

    @Override
    public void setMemento(Memento memento) {
        this.state = memento.getState();
    }

    public void setState(AppointmentState state) {
        this.state = state;
    }

    public static class Memento {
        private AppointmentState state;

        private Memento() {
        }

        private AppointmentState getState() {
            return state;
        }

        private void setState(AppointmentState state) {
            this.state = state;
        }
    }
}
