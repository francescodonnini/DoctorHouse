package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.doctor.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.doctor.Specialty;

public abstract class AppointmentInfo {
    private final Doctor doctor;
    private final User patient;
    private final Specialty specialty;
    private final Office office;

    public AppointmentInfo(Doctor doctor, User patient, Specialty specialty, Office office) {
        this.doctor = doctor;
        this.patient = patient;
        this.specialty = specialty;
        this.office = office;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public User getPatient() {
        return patient;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public Office getOffice() {
        return office;
    }
}
