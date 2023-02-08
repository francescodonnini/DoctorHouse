package ispw.uniroma2.doctorhouse.beans;

import ispw.uniroma2.doctorhouse.model.User;

public class UserBean {
    protected String email;
    protected String firstName;
    protected String lastName;

    public UserBean(User user) {
        email = user.getEmail();
        firstName = user.getPerson().getFirstName();
        lastName = user.getPerson().getLastName();
    }

    public UserBean() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
