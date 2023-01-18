package ispw.uniroma2.doctorhouse.model;

import java.time.LocalDate;

public class Person {
    private final LocalDate birthDate;
    private final String fiscalCode;
    private final String firstName;
    private final String lastName;
    private final Gender gender;

    public Person(LocalDate birthDate, String fiscalCode, String firstName, String lastName, Gender gender) {
        this.birthDate = birthDate;
        this.fiscalCode = fiscalCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
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

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

}
