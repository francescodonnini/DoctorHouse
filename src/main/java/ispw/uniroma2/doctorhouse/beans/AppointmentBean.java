package ispw.uniroma2.doctorhouse.beans;

import ispw.uniroma2.doctorhouse.model.appointment.Appointment;

import java.time.LocalDateTime;

public class AppointmentBean {
    private LocalDateTime dateTime;
    private DoctorBean doctor;
    private OfficeBean office;
    private UserBean patient;
    private SpecialtyBean specialty;

    public AppointmentBean(Appointment appointment) {
        patient = new UserBean(appointment.getPatient());
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
        dateTime = appointment.getInfo().getDate();
    }

    public AppointmentBean() {}

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
