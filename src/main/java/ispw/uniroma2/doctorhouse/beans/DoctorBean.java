package ispw.uniroma2.doctorhouse.beans;

import ispw.uniroma2.doctorhouse.model.Doctor;

public class DoctorBean extends UserBean {
    private String field;

    public DoctorBean(Doctor doctor) {
        super(doctor);
        this.field = doctor.getField();
    }

    public DoctorBean() {}

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
