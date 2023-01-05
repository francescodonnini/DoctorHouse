package ispw.uniroma2.doctorhouse.model;

import java.time.LocalDate;

public class User {
    private LocalDate birthDate;
    private String fiscalCode;
    private String firstName;
    private String email;
    private Gender gender;
    private String lastName;

    public User(LocalDate birthDate, String fiscalCode, String firstName, String email, Gender gender, String lastName) {
        this.birthDate = birthDate;
        this.fiscalCode = fiscalCode;
        this.firstName = firstName;
        this.email = email;
        this.gender = gender;
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public Gender getGender() {
        return gender;
    }

    public String getLastName() {
        return lastName;
    }
}
