package ispw.uniroma2.doctorhouse.model;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;

public class Shift {
    private final DayOfWeek day;
    private final List<ClockInterval> clockIntervals;

    public Shift(DayOfWeek day, List<ClockInterval> clockIntervals) {
        this.day = day;
        this.clockIntervals = clockIntervals;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public List<ClockInterval> getIntervals() {
        return Collections.unmodifiableList(clockIntervals);
    }
}
