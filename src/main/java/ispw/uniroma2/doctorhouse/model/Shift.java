package ispw.uniroma2.doctorhouse.model;

import java.time.DayOfWeek;
import java.util.List;

public interface Shift {
    DayOfWeek getDay();
    List<ClockInterval> getIntervals();
}
