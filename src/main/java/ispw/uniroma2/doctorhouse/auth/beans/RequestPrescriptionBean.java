package ispw.uniroma2.doctorhouse.auth.beans;

import ispw.uniroma2.doctorhouse.model.User;

public class RequestPrescriptionBean {
    private User user;
    private String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
