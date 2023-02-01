package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.Specialty;

import java.time.LocalDateTime;
import java.util.Optional;

public class CanceledInfo extends AppointmentInfo {
    private final User initiator;

    public CanceledInfo(Doctor doctor, User patient, Specialty specialty, Office office, LocalDateTime date, User initiator) {
        super(doctor, patient, specialty, office, date);
        this.initiator = initiator;
    }

    public Optional<User> getInitiator() {
        return Optional.ofNullable(initiator);
    }

}