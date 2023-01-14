package ispw.uniroma2.doctorhouse.model.doctor;

import ispw.uniroma2.doctorhouse.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class Doctor extends User implements OfficeMgr, ScheduleMgr {
    private final String field;
    private final List<OfficeImpl> offices;
    private final List<Specialty> specialties;
    private final Map<DayOfWeek, ShiftImpl> schedule;

    public Doctor(LocalDate birthDate, String fiscalCode, String firstName, String email, Gender gender, String lastName, String field, List<OfficeImpl> offices, List<Specialty> specialties, Map<DayOfWeek, ShiftImpl> schedule) {
        super(birthDate, fiscalCode, firstName, email, gender, lastName);
        this.field = field;
        offices.forEach(office -> office.setDoctor(this));
        this.offices = new ArrayList<>(offices);
        this.specialties = new ArrayList<>(specialties);
        this.schedule = new EnumMap<>(schedule);
    }

    public String getField() {
        return field;
    }

    public List<Office> getOffices() {
        return new ArrayList<>(this.offices);
    }

    public List<Specialty> getSpecialties() {
        return new ArrayList<>(specialties);
    }

    public Optional<Shift> getShift(DayOfWeek day) {
        return Optional.ofNullable(schedule.get(day));
    }

    @Override
    public void addSpecialty(OfficeImpl office, Specialty specialty) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSpecialty(OfficeImpl office, Specialty specialty) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addShift(DayOfWeek day, Office office, List<ClockInterval> intervals) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(DayOfWeek day, Office office) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(DayOfWeek day, Office office, List<ClockInterval> intervals) {
        throw new UnsupportedOperationException();
    }
}
