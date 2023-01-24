package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.Specialty;

import java.time.LocalDateTime;

public class PendingInfo extends AppointmentInfo {
    private final User initiator;
    private final LocalDateTime newDate;

    public PendingInfo(Doctor doctor, User patient, Specialty specialty, Office office, User initiator, LocalDateTime oldDate, LocalDateTime newDate) {
        super(doctor, patient, specialty, office, oldDate);
        this.initiator = initiator;
        this.newDate = newDate;
    }

    public User getInitiator() {
        return initiator;
    }

    public LocalDateTime getOldDate() {
        return getDate();
    }

    public LocalDateTime getNewDate() {
        return newDate;
    }
}
