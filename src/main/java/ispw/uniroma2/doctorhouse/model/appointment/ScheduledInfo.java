package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.Specialty;

import java.time.LocalDateTime;

public class ScheduledInfo extends AppointmentInfo {
    public ScheduledInfo(Doctor doctor, User patient, Specialty specialty, Office office, LocalDateTime date) {
        super(doctor, patient, specialty, office, date);
    }
}