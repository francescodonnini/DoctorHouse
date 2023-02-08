package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.Specialty;
import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public interface AppointmentBuilder {
    void setPatient(User user);

    void setDoctor(Doctor doctor);

    void setSpecialty(Specialty specialty);

    void setDate(LocalDateTime date);

    void setOldDate(LocalDateTime oldDate);

    void setOffice(Office office);

    void setInitiator(User user);

    Appointment build(Class<? extends AppointmentInfo> type);

    void reset();
}
