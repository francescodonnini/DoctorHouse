package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.model.ClockInterval;
import ispw.uniroma2.doctorhouse.model.Location;
import ispw.uniroma2.doctorhouse.model.ShiftImpl;
import ispw.uniroma2.doctorhouse.model.doctor.OfficeImpl;
import ispw.uniroma2.doctorhouse.model.doctor.Specialty;

import java.time.DayOfWeek;
import java.util.*;

public class Builder implements OfficeBuilder {
    private final Map<DayOfWeek, List<ClockInterval>> map;
    private Location location;
    private final List<Specialty> specialties;

    public Builder() {
        this.specialties = new ArrayList<>();
        this.map = new EnumMap<>(DayOfWeek.class);
    }

    @Override
    public void addInterval(DayOfWeek day, ClockInterval interval) {
        map.computeIfAbsent(day, f -> new ArrayList<>()).add(interval);
    }

    public void addSpecialties(Specialty specialty) {
        specialties.add(specialty);
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    public OfficeImpl build() {
        List<ShiftImpl> shifts = new ArrayList<>();
        List<Specialty> listOfSpecialty = new ArrayList<>();
        map.forEach((key, value) -> shifts.add(new ShiftImpl(key, value)));
        return new OfficeImpl(location, listOfSpecialty, shifts);
    }


}
