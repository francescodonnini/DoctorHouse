package ispw.uniroma2.doctorhouse.beans;

import java.time.LocalDate;
import java.util.Optional;

public class UserRegistrationRequestBean {
    private LocalDate birthDate;
    private String email;
    private String fiscalCode;
    private String firstName;
    private GenderBean gender;
    private String lastName;
    private String password;
    private DoctorBean familyDoctor;

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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

    public GenderBean getGender() {
        return gender;
    }

    public void setGender(GenderBean gender) {
        this.gender = gender;
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

    public Optional<DoctorBean> getFamilyDoctor() {
        return Optional.ofNullable(familyDoctor);
    }

    public void setFamilyDoctor(DoctorBean familyDoctor) {
        this.familyDoctor = familyDoctor;
    }
}
