package ispw.uniroma2.doctorhouse.model;

import java.time.LocalTime;

public class ClockInterval {
    private final LocalTime start;
    private final LocalTime end;

    public ClockInterval(LocalTime start, LocalTime duration) {
        this.start = start;
        this.end = duration;
    }
    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
}
