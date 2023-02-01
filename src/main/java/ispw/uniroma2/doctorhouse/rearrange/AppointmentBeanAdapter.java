package ispw.uniroma2.doctorhouse.rearrange;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.model.appointment.AppointmentInfo;

import java.time.LocalDateTime;

public class AppointmentBeanAdapter extends AppointmentBean {
    private final AppointmentInfo adaptee;
    private DoctorBean doctor;
    private OfficeBean office;
    private UserBean patient;
    private SpecialtyBean specialty;

    public AppointmentBeanAdapter(AppointmentInfo adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public LocalDateTime getDateTime() {
        return adaptee.getDate();
    }

    @Override
    public DoctorBean getDoctor() {
        if (doctor == null) {
            doctor = new DoctorBean();
            doctor.setEmail(adaptee.getDoctor().getEmail());
        }
        return doctor;
    }

    @Override
    public OfficeBean getOffice() {
        if (office == null) {
            office = new OfficeBean();
            office.setId(adaptee.getOffice().getId());
            office.setDoctor(getDoctor());
            office.setCountry(adaptee.getOffice().getLocation().getCountry());
            office.setProvince(adaptee.getOffice().getLocation().getProvince());
            office.setCity(adaptee.getOffice().getLocation().getCity());
            office.setAddress(adaptee.getOffice().getLocation().getAddress());
        }
        return office;
    }

    @Override
    public UserBean getPatient() {
        if (patient == null) {
            patient = new UserBean();
            patient.setEmail(adaptee.getPatient().getEmail());
        }
        return patient;
    }

    @Override
    public SpecialtyBean getSpecialty() {
       if (specialty == null) {
           specialty = new SpecialtyBean();
           specialty.setName(adaptee.getSpecialty().getName());
           specialty.setDuration(adaptee.getSpecialty().getDuration());
       }
       return specialty;
    }
}
