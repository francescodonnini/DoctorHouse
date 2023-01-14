package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDate;

public interface Appointment {
    void confirm(User confirmee);
    void cancel(User cancelee);
    void reschedule(User reschedulee, LocalDate newDate);
    AppointmentInfo getInfo();
    AppointmentImpl.Memento createMemento();
    void setMemento(AppointmentImpl.Memento memento);
}
