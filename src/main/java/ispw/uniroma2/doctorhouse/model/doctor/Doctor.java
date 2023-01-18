package ispw.uniroma2.doctorhouse.model.doctor;

import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.Person;
import ispw.uniroma2.doctorhouse.model.User;

import java.util.Collections;
import java.util.List;

public class Doctor extends User {
    private final String field;
    private final List<OfficeImpl> offices;

    public Doctor(String email, Person person, Doctor familyDoctor, String field, List<OfficeImpl> offices) {
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
