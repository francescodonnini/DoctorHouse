package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.doctor.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.doctor.Specialty;

import java.time.LocalDate;

public class PendingInfo extends AppointmentInfo {
    private final User requestee;
    private final LocalDate oldDate;
    private final LocalDate newDate;

    public PendingInfo(Doctor doctor, User patient, Specialty specialty, Office office, User requestee, LocalDate oldDate, LocalDate newDate) {
        super(doctor, patient, specialty, office);
        this.requestee = requestee;
        this.oldDate = oldDate;
        this.newDate = newDate;
    }

    public User getRequestee() {
        return requestee;
    }

    public LocalDate getOldDate() {
        return oldDate;
    }

    public LocalDate getNewDate() {
        return newDate;
    }
}
