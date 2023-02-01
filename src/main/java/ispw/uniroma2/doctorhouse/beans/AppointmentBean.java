package ispw.uniroma2.doctorhouse.beans;

import java.time.LocalDateTime;

public class AppointmentBean {
    private LocalDateTime dateTime;
    private DoctorBean doctor;
    private OfficeBean office;
    private UserBean patient;
    private SpecialtyBean specialty;

    public UserBean getPatient() {
        return patient;
    }

    public void setPatient(UserBean patient) {
        this.patient = patient;
    }

    public DoctorBean getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorBean doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public SpecialtyBean getSpecialty() {
        return specialty;
    }

    public void setSpecialty(SpecialtyBean specialty) {
        this.specialty = specialty;
    }

    public OfficeBean getOffice() {
        return office;
    }

    public void setOffice(OfficeBean office) {
        this.office = office;
    }
}
