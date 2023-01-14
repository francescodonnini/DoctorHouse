package ispw.uniroma2.doctorhouse.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShiftImpl implements Shift {
    private final DayOfWeek day;
    private final Map<Office, List<ClockInterval>> shifts;

    public ShiftImpl(DayOfWeek day, Map<Office, List<ClockInterval>> shifts) {
        this.day = day;
        this.shifts = shifts;
    }

    @Override
    public DayOfWeek getDay() {
        return day;
    }

    @Override
    public List<ClockInterval> getIntervals(Office office) {
        if (shifts.containsKey(office)) {
            return new ArrayList<>(shifts.get(office));
        }
        return List.of();
    }

    protected void add(Office office, List<ClockInterval> intervals) {
        throw new UnsupportedOperationException();
    }

    protected void remove(Office office, List<ClockInterval> intervals) {
        throw new UnsupportedOperationException();
    }
}
