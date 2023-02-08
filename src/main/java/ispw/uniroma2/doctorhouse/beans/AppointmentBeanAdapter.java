package ispw.uniroma2.doctorhouse.beans;

import ispw.uniroma2.doctorhouse.model.appointment.Appointment;
import ispw.uniroma2.doctorhouse.model.appointment.AppointmentInfo;

import java.time.LocalDateTime;

public class AppointmentBeanAdapter extends AppointmentBean {
    private final AppointmentInfo adaptee;
    private final DoctorBean doctor;
    private final OfficeBean officeBean;
    private final UserBean patient;
    private final SpecialtyBean specialtyBean;

    public AppointmentBeanAdapter(Appointment appointment) {
        adaptee = appointment.getInfo();
        doctor = new DoctorBean(appointment.getDoctor());
        patient = new UserBean(appointment.getPatient());
        officeBean = new OfficeBean();
        officeBean.setId(appointment.getOffice().getId());
        officeBean.setDoctor(doctor);
        officeBean.setCountry(appointment.getOffice().getLocation().getCountry());
        officeBean.setProvince(appointment.getOffice().getLocation().getProvince());
        officeBean.setCity(appointment.getOffice().getLocation().getCity());
        officeBean.setAddress(appointment.getOffice().getLocation().getAddress());
        specialtyBean = new SpecialtyBean();
        specialtyBean.setDoctor(doctor);
        specialtyBean.setName(appointment.getSpecialty().getName());
        specialtyBean.setDuration(appointment.getSpecialty().getDuration());
    }

    @Override
    public LocalDateTime getDateTime() {
        return adaptee.getDate();
    }

    @Override
    public DoctorBean getDoctor() {
        return doctor;
    }

    @Override
    public OfficeBean getOffice() {
        return officeBean;
    }

    @Override
    public UserBean getPatient() {
        return patient;
    }

    @Override
    public SpecialtyBean getSpecialty() {
       return specialtyBean;
    }
}
