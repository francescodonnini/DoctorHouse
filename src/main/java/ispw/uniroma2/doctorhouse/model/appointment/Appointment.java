package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.Specialty;
import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public interface Appointment {
    void confirm(User initiator);

    void cancel(User initiator);

    void reschedule(User initiator, LocalDateTime newDate);
    AppointmentInfo getInfo();

    AppointmentMemento createMemento();

    void restore(AppointmentMemento memento);
    Doctor getDoctor();
    Office getOffice();
    User getPatient();
    Specialty getSpecialty();
}
