package ispw.uniroma2.doctorhouse.auth.beans;

public class LoginRequestBean {
    private EmailBean email;
    private String password;

    public LoginRequestBean() {
    }

    public EmailBean getEmail() {
        return email;
    }

    public void setEmail(EmailBean email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
