package ispw.uniroma2.doctorhouse.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class OfficeImpl implements Office {
    private final int id;
    private final Location location;
    private final List<Specialty> specialties;
    private final EnumMap<DayOfWeek, Shift> shifts;

    public OfficeImpl(int id, Location location, List<Specialty> specialties, List<Shift> shifts) {
        this.id = id;
        this.location = location;
        this.specialties = specialties;
        this.shifts = new EnumMap<>(DayOfWeek.class);
        for (Shift s : shifts) {
            this.shifts.put(s.getDay(), s);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public List<Specialty> getSpecialties() {
        return Collections.unmodifiableList(specialties);
    }

    @Override
    public Optional<Shift> getShift(LocalDate date) {
        return  Optional.ofNullable(shifts.get(date.getDayOfWeek()));
    }

    public Map<DayOfWeek, Shift> getActualShift() {
        return shifts;
    }
}
