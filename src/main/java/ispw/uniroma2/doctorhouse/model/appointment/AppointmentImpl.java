package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.Specialty;
import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public class AppointmentImpl implements Appointment {
    private final Doctor doctor;
    private final User patient;
    private final Specialty specialty;
    private final Office office;
    private AppointmentState state;

    public AppointmentImpl(Doctor doctor, User patient, Specialty specialty, Office office, AppointmentInfo info) {
        this.doctor = doctor;
        this.patient = patient;
        this.specialty = specialty;
        this.office = office;
        if (info instanceof CanceledInfo) {
            state = new CanceledState((CanceledInfo) info);
        } else if (info instanceof PendingInfo) {
            state = new PendingState((PendingInfo) info);
        } else if (info instanceof ScheduledInfo){
            state = new ScheduledState((ScheduledInfo) info);
        } else {
            throw new IllegalArgumentException();
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

    @Override
    public AppointmentMemento createMemento() {
        return new AppointmentMemento(getInfo());
    }

    @Override
    public void restore(AppointmentMemento memento) {
        AppointmentInfo info = memento.getInfo();
        if (info instanceof CanceledInfo) {
            setState(new CanceledState((CanceledInfo) info));
        } else if (info instanceof ScheduledInfo) {
            setState(new ScheduledState((ScheduledInfo) info));
        } else if (info instanceof PendingInfo) {
            setState(new PendingState((PendingInfo) info));
        }
    }

    @Override
    public Doctor getDoctor() {
        return doctor;
    }

    @Override
    public Office getOffice() {
        return office;
    }

    @Override
    public User getPatient() {
        return patient;
    }

    @Override
    public Specialty getSpecialty() {
        return specialty;
    }

    public void setState(AppointmentState state) {
        this.state = state;
    }
}