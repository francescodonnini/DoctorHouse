package ispw.uniroma2.doctorhouse.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface Office {
    int getId();
    Location getLocation();
    List<Specialty> getSpecialties();
    Optional<Shift> getShift(LocalDate date);
}
