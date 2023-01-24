package ispw.uniroma2.doctorhouse.model;


import java.util.*;

public class Doctor extends User {
    private final String field;
    private final List<Office> offices;

    public Doctor(String email, Person person, Doctor familyDoctor, String field, List<Office> offices) {
        super(email, person, familyDoctor);
        this.field = field;
        this.offices = offices;
    }

    public String getField() {
        return field;
    }

    public List<Office> getOffices() {
        return Collections.unmodifiableList(offices);
    }
}
