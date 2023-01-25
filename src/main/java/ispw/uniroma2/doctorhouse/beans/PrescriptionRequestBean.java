package ispw.uniroma2.doctorhouse.beans;

import ispw.uniroma2.doctorhouse.model.User;

public class PrescriptionRequestBean {
    private String message;
    private User patient;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }
}
