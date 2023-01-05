package ispw.uniroma2.doctorhouse.auth.beans;

import java.time.LocalDate;

public class UserRegistrationRequestBean {
    private LocalDate birthDate;
    private EmailBean email;
    private String fiscalCode;
    private String firstName;
    private int genderIsoCode;
    private String lastName;
    private String password;

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public EmailBean getEmail() {
        return email;
    }

    public void setEmail(EmailBean email) {
        this.email = email;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getGenderIsoCode() {
        return genderIsoCode;
    }

    public void setGenderIsoCode(int genderIsoCode) {
        this.genderIsoCode = genderIsoCode;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
