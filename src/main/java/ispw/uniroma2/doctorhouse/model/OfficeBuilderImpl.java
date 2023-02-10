package ispw.uniroma2.doctorhouse.model;

import java.util.ArrayList;
import java.util.List;

public class OfficeBuilderImpl implements OfficeBuilder {
    private int id;
    private Location location;
    List<Shift> shifts;
    List<Specialty> specialties;

    public OfficeBuilderImpl() {
        shifts = new ArrayList<>();
        specialties = new ArrayList<>();
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void addShift(Shift shift) {
        shifts.add(shift);
    }

    @Override
    public void addSpecialty(Specialty specialty) {
        specialties.add(specialty);
    }

    @Override
    public Office build() {
        return new Office(id, location, new ArrayList<>(specialties), new ArrayList<>(shifts));
    }

    @Override
    public void reset() {
        location = null;
        shifts.clear();
        specialties.clear();
    }
}
