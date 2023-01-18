package ispw.uniroma2.doctorhouse.model;

import ispw.uniroma2.doctorhouse.model.doctor.Specialty;
import ispw.uniroma2.doctorhouse.model.doctor.Doctor;

import java.util.List;

public interface Office {
    Location getLocation();
    List<Specialty> getSpecialties();
}
