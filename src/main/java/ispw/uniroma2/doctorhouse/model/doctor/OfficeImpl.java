package ispw.uniroma2.doctorhouse.model.doctor;

import ispw.uniroma2.doctorhouse.model.Location;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.ShiftImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class OfficeImpl implements Office {
    private final Location location;
    private final List<Specialty> specialties;
    private final EnumMap<DayOfWeek, ShiftImpl> shifts;

    public OfficeImpl(Location location, List<Specialty> specialties, List<ShiftImpl> shifts) {
        this.location = location;
        this.specialties = specialties;
        this.shifts = new EnumMap<>(DayOfWeek.class);
        for (ShiftImpl s : shifts) {
            this.shifts.put(s.getDay(), s);
        }
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public List<Specialty> getSpecialties() {
        return Collections.unmodifiableList(specialties);
    }

    public Optional<ShiftImpl> getShift(LocalDate date) {
        return  Optional.ofNullable(shifts.get(date.getDayOfWeek()));
    }

}
