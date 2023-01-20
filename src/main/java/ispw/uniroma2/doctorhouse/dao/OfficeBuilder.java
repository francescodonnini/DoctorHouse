package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.model.ClockInterval;
import ispw.uniroma2.doctorhouse.model.Location;
import ispw.uniroma2.doctorhouse.model.doctor.OfficeImpl;
import ispw.uniroma2.doctorhouse.model.doctor.Specialty;

import java.time.DayOfWeek;

public interface OfficeBuilder {
    void addInterval(DayOfWeek day, ClockInterval interval);

    void addSpecialties(Specialty specialty);
    void setLocation(Location location);
    OfficeImpl build();


}
