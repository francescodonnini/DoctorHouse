package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.doctor.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.doctor.Specialty;

public class CanceledInfo extends AppointmentInfo {
    private final User cancelee;

    public CanceledInfo(Doctor doctor, User patient, Specialty specialty, Office office, User cancelee) {
        super(doctor, patient, specialty, office);
        this.cancelee = cancelee;
    }

    public User getCancelee() {
        return cancelee;
    }

}
