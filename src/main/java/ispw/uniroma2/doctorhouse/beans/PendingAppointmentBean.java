package ispw.uniroma2.doctorhouse.beans;

import java.time.LocalDateTime;

public class PendingAppointmentBean {
    private LocalDateTime dateTime;
    private LocalDateTime oldDateTime;
    private DoctorBean doctor;
    private OfficeBean office;
    private SpecialtyBean specialty;
    private UserBean user;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getOldDateTime() {
        return oldDateTime;
    }

    public void setOldDateTime(LocalDateTime oldDateTime) {
        this.oldDateTime = oldDateTime;
    }

    public DoctorBean getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorBean doctor) {
        this.doctor = doctor;
    }

    public OfficeBean getOffice() {
        return office;
    }

    public void setOffice(OfficeBean office) {
        this.office = office;
    }

    public SpecialtyBean getSpecialty() {
        return specialty;
    }

    public void setSpecialty(SpecialtyBean specialty) {
        this.specialty = specialty;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
