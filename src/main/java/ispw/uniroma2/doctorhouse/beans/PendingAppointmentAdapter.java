package ispw.uniroma2.doctorhouse.beans;

import ispw.uniroma2.doctorhouse.model.appointment.PendingInfo;

import java.time.LocalDateTime;

public class PendingAppointmentAdapter extends PendingAppointmentBean {
    private final PendingInfo adaptee;
    private final DoctorBean doctorBean;
    private final UserBean userBean;
    private final OfficeBean officeBean;
    private final SpecialtyBean specialtyBean;

    public PendingAppointmentAdapter(PendingInfo adaptee) {
        this.adaptee = adaptee;
        doctorBean = new DoctorBean();
        doctorBean.setEmail(adaptee.getDoctor().getEmail());
        userBean = new UserBean();
        userBean.setEmail(adaptee.getPatient().getEmail());
        officeBean = new OfficeBean();
        officeBean.setId(adaptee.getOffice().getId());
        officeBean.setDoctor(doctorBean);
        officeBean.setCountry(adaptee.getOffice().getLocation().getCountry());
        officeBean.setProvince(adaptee.getOffice().getLocation().getProvince());
        officeBean.setCity(adaptee.getOffice().getLocation().getCity());
        officeBean.setAddress(adaptee.getOffice().getLocation().getAddress());
        specialtyBean = new SpecialtyBean();
        specialtyBean.setDoctor(doctorBean);
        specialtyBean.setName(adaptee.getSpecialty().getName());
        specialtyBean.setDuration(adaptee.getSpecialty().getDuration());
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
