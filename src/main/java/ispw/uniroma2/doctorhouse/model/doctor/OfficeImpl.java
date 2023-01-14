package ispw.uniroma2.doctorhouse.model.doctor;

import ispw.uniroma2.doctorhouse.model.Location;
import ispw.uniroma2.doctorhouse.model.Office;

import java.util.ArrayList;
import java.util.List;

public class OfficeImpl implements Office {
    private final Location location;
    private final List<Specialty> specialties;
    private Doctor owner;

    public OfficeImpl(Location location, List<Specialty> specialties) {
        this.location = location;
        this.specialties = specialties;
    }

    @Override
    public Doctor getDoctor() {
        return owner;
    }

    protected void setDoctor(Doctor doctor) {
        owner = doctor;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public List<Specialty> getSpecialties() {
        return new ArrayList<>(specialties);
    }

    protected void addSpecialty(Specialty specialty) {
        throw new UnsupportedOperationException();
    }

    protected void removeSpecialty(Specialty specialty) {
        throw new UnsupportedOperationException();
    }
}
