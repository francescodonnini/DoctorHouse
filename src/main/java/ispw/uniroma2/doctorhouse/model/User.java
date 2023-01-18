package ispw.uniroma2.doctorhouse.model;

import ispw.uniroma2.doctorhouse.model.doctor.Doctor;

import java.time.LocalDate;
import java.util.Optional;

public class User {
    private final String email;
    private final Person person;
    private final Doctor familyDoctor;


    public User(String email, Person person, Doctor familyDoctor) {
        this.email = email;
        this.person = person;
        this.familyDoctor = familyDoctor;
    }

    public Optional<Doctor> getFamilyDoctor() {
        return Optional.ofNullable(familyDoctor);
    }
}