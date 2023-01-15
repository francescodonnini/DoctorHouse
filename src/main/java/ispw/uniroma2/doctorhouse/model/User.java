package ispw.uniroma2.doctorhouse.model;

import ispw.uniroma2.doctorhouse.model.doctor.Doctor;

import java.time.LocalDate;
import java.util.Optional;

public class User {
    private final LocalDate birthDate;
    private final String fiscalCode;
    private final String firstName;
    private final String email;
    private final Gender gender;
    private final String lastName;
    private final Doctor familyDoctor;

    public User(LocalDate birthDate, String fiscalCode, String firstName, String email, Gender gender, String lastName) {
        this(birthDate, fiscalCode, firstName, email, gender, lastName, null);
    }

    public User(LocalDate birthDate, String fiscalCode, String firstName, String email, Gender gender, String lastName, Doctor familyDoctor) {
        this.birthDate = birthDate;
        this.fiscalCode = fiscalCode;
        this.firstName = firstName;
        this.email = email;
        this.gender = gender;
        this.lastName = lastName;
        this.familyDoctor = familyDoctor;
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

    public Optional<Doctor> getFamilyDoctor() {
        return Optional.ofNullable(familyDoctor);
    }
}