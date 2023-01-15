package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.doctor.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.doctor.Specialty;

import java.time.LocalDate;

public class ScheduledInfo extends AppointmentInfo {
    private final LocalDate date;

    public ScheduledInfo(Doctor doctor, User patient, Specialty specialty, Office office, LocalDate date) {
        super(doctor, patient, specialty, office);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}