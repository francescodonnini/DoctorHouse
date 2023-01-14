package ispw.uniroma2.doctorhouse.model;

import java.time.Duration;
import java.time.LocalTime;

public class ClockInterval {
    private final LocalTime start;
    private final Duration duration;

    public ClockInterval(LocalTime start, Duration duration) {
        this.start = start;
        this.duration = duration;
    }

    public LocalTime getStart() {
        return start;
    }

    public Duration getDuration() {
        return duration;
    }
}
