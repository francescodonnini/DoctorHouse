package ispw.uniroma2.doctorhouse.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateTimeInterval implements TakenSlot {
    private final LocalDateTime dateTime;
    private final Duration duration;

    public DateTimeInterval(LocalDateTime dateTime, Duration duration) {
        this.dateTime = dateTime;
        this.duration = duration;
    }

    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public TimeInterval getInterval() {
        return new ClockInterval(dateTime.toLocalTime(), duration);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", dateTime, dateTime.toLocalTime().plus(duration));
    }
}
