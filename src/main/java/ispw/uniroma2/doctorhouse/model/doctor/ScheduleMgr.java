package ispw.uniroma2.doctorhouse.model.doctor;

import ispw.uniroma2.doctorhouse.model.ClockInterval;
import ispw.uniroma2.doctorhouse.model.Office;

import java.time.DayOfWeek;
import java.util.List;

public interface ScheduleMgr {
    void addShift(DayOfWeek day, Office office, List<ClockInterval> intervals);

    void remove(DayOfWeek day, Office office);

    void remove(DayOfWeek day, Office office, List<ClockInterval> intervals);
}