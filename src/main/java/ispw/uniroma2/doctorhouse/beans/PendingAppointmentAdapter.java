package ispw.uniroma2.doctorhouse.beans;

import ispw.uniroma2.doctorhouse.model.appointment.Appointment;
import ispw.uniroma2.doctorhouse.model.appointment.PendingInfo;

import java.time.LocalDateTime;

public class PendingAppointmentAdapter extends PendingAppointmentBean {
    private final PendingInfo adaptee;
    private final DoctorBean doctorBean;
    private final UserBean userBean;
    private final OfficeBean officeBean;
    private final SpecialtyBean specialtyBean;

    public PendingAppointmentAdapter(Appointment appointment) {
        if (appointment.getInfo() instanceof PendingInfo) {
            adaptee = (PendingInfo) appointment.getInfo();
        } else {
            throw new IllegalArgumentException();
        }
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
        return adaptee.getOldDate();
    }

    @Override
    public LocalDateTime getOldDateTime() {
        return adaptee.getOldDate();
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
    public SpecialtyBean getSpecialty() {
        return specialtyBean;
    }

    @Override
    public UserBean getUser() {
        return userBean;
    }
}
