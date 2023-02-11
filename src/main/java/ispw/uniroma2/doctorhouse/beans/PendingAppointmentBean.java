package ispw.uniroma2.doctorhouse.beans;

import ispw.uniroma2.doctorhouse.model.appointment.Appointment;
import ispw.uniroma2.doctorhouse.model.appointment.AppointmentInfo;
import ispw.uniroma2.doctorhouse.model.appointment.PendingInfo;

import java.time.LocalDateTime;

public class PendingAppointmentBean {
    private LocalDateTime dateTime;
    private LocalDateTime oldDateTime;
    private DoctorBean doctor;
    private OfficeBean office;
    private SpecialtyBean specialty;
    private UserBean user;
    private UserBean initiator;

    public PendingAppointmentBean(Appointment appointment) {
        user = new UserBean(appointment.getPatient());
        doctor = new DoctorBean(appointment.getDoctor());
        office = new OfficeBean();
        office.setId(appointment.getOffice().getId());
        office.setCountry(appointment.getOffice().getLocation().getCountry());
        office.setProvince(appointment.getOffice().getLocation().getProvince());
        office.setCity(appointment.getOffice().getLocation().getCity());
        office.setAddress(appointment.getOffice().getLocation().getAddress());
        office.setDoctor(doctor);
        specialty = new SpecialtyBean();
        specialty.setDoctor(doctor);
        specialty.setDuration(appointment.getSpecialty().getDuration());
        specialty.setName(appointment.getSpecialty().getName());
        AppointmentInfo info = appointment.getInfo();
        if (info instanceof PendingInfo) {
            dateTime = ((PendingInfo) info).getNewDate();
            oldDateTime = ((PendingInfo) info).getOldDate();
            initiator = new UserBean(((PendingInfo) info).getInitiator());
        } else {
            throw new IllegalArgumentException("expected an appointment waiting to be rearranged");
        }
    }

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

    public UserBean getInitiator() {
        return initiator;
    }

    public void setInitiator(UserBean initiator) {
        this.initiator = initiator;
    }
}
