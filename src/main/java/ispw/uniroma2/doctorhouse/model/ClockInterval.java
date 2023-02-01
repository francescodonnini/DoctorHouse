package ispw.uniroma2.doctorhouse.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public class ClockInterval implements TimeInterval {
    private final LocalTime start;
    private final LocalTime end;

    public ClockInterval(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public ClockInterval(LocalTime start, Duration duration) {
        this(start, start.plus(duration));
    }

    @Override
    public int compareTo(TimeInterval o) {
        return start.compareTo(o.getStart());
    }

    @Override
    public boolean contains(TimeInterval other) {
        return !start.isAfter(other.getStart()) && !end.isBefore(other.getEnd());
    }

    @Override
    public boolean contains(LocalTime point) {
        return !start.isAfter(point) && !end.isBefore(point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClockInterval that = (ClockInterval) o;
        return start.equals(that.start) && end.equals(that.end);
    }

    @Override
    public Duration getDuration() {
        return Duration.between(start, end);
    }

    @Override
    public LocalTime getEnd() {
        return end;
    }

    @Override
    public LocalTime getStart() {
        return start;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public boolean overlaps(TimeInterval other) {
        return !(getEnd().isBefore(other.getStart()) || other.getEnd().isBefore(getStart()));
    }

    @Override
    public String toString() {
        return String.format("[%s; %s]", start, end);
    }
}
