package ispw.uniroma2.doctorhouse.model;

import java.time.LocalDate;

public class User {
    private final LocalDate birthDate;
    private final String fiscalCode;
    private final String firstName;
    private final String email;
    private final Gender gender;
    private final String lastName;

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