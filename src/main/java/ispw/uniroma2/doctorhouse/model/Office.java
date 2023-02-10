package ispw.uniroma2.doctorhouse.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class Office {
    private final int id;
    private final Location location;
    private final List<Specialty> specialties;
    private final EnumMap<DayOfWeek, Shift> shifts;

    public Office(int id, Location location, List<Specialty> specialties, List<Shift> shifts) {
        this.id = id;
        this.location = location;
        this.specialties = specialties;
        this.shifts = new EnumMap<>(DayOfWeek.class);
        for (Shift s : shifts) {
            this.shifts.put(s.getDay(), s);
        }
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public List<Specialty> getSpecialties() {
        return Collections.unmodifiableList(specialties);
    }

    public Optional<Shift> getShift(LocalDate date) {
        return Optional.ofNullable(shifts.get(date.getDayOfWeek()));
    }
}