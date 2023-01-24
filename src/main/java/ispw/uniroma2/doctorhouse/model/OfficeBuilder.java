package ispw.uniroma2.doctorhouse.model;

public interface OfficeBuilder {
    void setId(int id);
    void setLocation(Location location);
    void addShift(Shift shift);
    void addSpecialty(Specialty specialty);
    Office build();
    void reset();
}
