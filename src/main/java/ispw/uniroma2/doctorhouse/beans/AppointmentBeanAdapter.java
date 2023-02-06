package ispw.uniroma2.doctorhouse.beans;

import ispw.uniroma2.doctorhouse.model.appointment.Appointment;
import ispw.uniroma2.doctorhouse.model.appointment.AppointmentInfo;

import java.time.LocalDateTime;

public class AppointmentBeanAdapter extends AppointmentBean {
    private final AppointmentInfo adaptee;
    private final DoctorBean doctorBean;
    private final OfficeBean officeBean;
    private final UserBean userBean;
    private final SpecialtyBean specialtyBean;

    public AppointmentBeanAdapter(Appointment appointment) {
        adaptee = appointment.getInfo();
        doctorBean = new DoctorBean();
        doctorBean.setEmail(appointment.getDoctor().getEmail());
        userBean = new UserBean();
        userBean.setEmail(appointment.getPatient().getEmail());
        officeBean = new OfficeBean();
        officeBean.setId(appointment.getOffice().getId());
        officeBean.setDoctor(doctorBean);
        officeBean.setCountry(appointment.getOffice().getLocation().getCountry());
        officeBean.setProvince(appointment.getOffice().getLocation().getProvince());
        officeBean.setCity(appointment.getOffice().getLocation().getCity());
        officeBean.setAddress(appointment.getOffice().getLocation().getAddress());
        specialtyBean = new SpecialtyBean();
        specialtyBean.setDoctor(doctorBean);
        specialtyBean.setName(appointment.getSpecialty().getName());
        specialtyBean.setDuration(appointment.getSpecialty().getDuration());
    }

    @Override
    public LocalDateTime getDateTime() {
        return adaptee.getDate();
    }

    @Override
    public DoctorBean getDoctor() {
        return doctorBean;
    }

    @Override
    public OfficeBean getOffice() {
        return officeBean;
    }

    @Override
    public UserBean getPatient() {
        return userBean;
    }

    @Override
    public SpecialtyBean getSpecialty() {
       return specialtyBean;
    }
}
