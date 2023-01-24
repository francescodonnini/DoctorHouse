package ispw.uniroma2.doctorhouse.model;

import java.time.DayOfWeek;

public interface ShiftBuilder {
    void setDay(DayOfWeek day);
    void addInterval(ClockInterval interval);
    Shift build();
    void reset();
}
