package ispw.uniroma2.doctorhouse.model;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;

public class ShiftImpl implements Shift {
    private final DayOfWeek day;
    private final List<ClockInterval> clockIntervals;

    public ShiftImpl(DayOfWeek day, List<ClockInterval> clockIntervals) {
        this.day = day;
        this.clockIntervals = clockIntervals;
    }

    @Override
    public DayOfWeek getDay() {
        return day;
    }

    @Override
    public List<ClockInterval> getIntervals() {
        return Collections.unmodifiableList(clockIntervals);
    }
}
