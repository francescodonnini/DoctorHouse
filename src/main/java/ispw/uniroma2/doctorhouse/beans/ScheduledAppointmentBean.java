package ispw.uniroma2.doctorhouse.beans;

import java.time.LocalDateTime;

public class ScheduledAppointmentBean {
    private DoctorBean doctor;
    private UserBean patient;
    private LocalDateTime dateTime;
    private SpecialtyBean specialty;
    private OfficeBean officeBean;

    public DoctorBean getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorBean doctor) {
        this.doctor = doctor;
    }

    public UserBean getPatient() {
        return patient;
    }

    public void setPatient(UserBean patient) {
        this.patient = patient;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public OfficeBean getOffice() {
        return officeBean;
    }

    public void setOffice(OfficeBean officeBean) {
        this.officeBean = officeBean;
    }

    public SpecialtyBean getSpecialty() {
        return specialty;
    }

    public void setSpecialty(SpecialtyBean specialty) {
        this.specialty = specialty;
    }
}
