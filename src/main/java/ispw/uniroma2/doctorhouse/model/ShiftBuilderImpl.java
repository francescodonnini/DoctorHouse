package ispw.uniroma2.doctorhouse.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class ShiftBuilderImpl implements ShiftBuilder {
    private DayOfWeek day;
    private List<ClockInterval> intervals;

    public ShiftBuilderImpl() {
        intervals = new ArrayList<>();
    }

    @Override
    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    @Override
    public void addInterval(ClockInterval interval) {
        for (ClockInterval i : intervals) {
            if (overlap(i, interval)) {
                throw new IllegalArgumentException("intervals overlap");
            }
        }
        intervals.add(interval);
    }

    private boolean overlap(ClockInterval i1, ClockInterval i2) {
        return i1.getStart().isAfter(i2.getStart()) && i1.getEnd().isBefore(i2.getEnd());
    }

    @Override
    public Shift build() {
        if (day == null || intervals.isEmpty()) {
            throw new IllegalStateException();
        }
        return new ShiftImpl(day, intervals);
    }

    @Override
    public void reset() {
        day = null;
        intervals.clear();
    }
}
