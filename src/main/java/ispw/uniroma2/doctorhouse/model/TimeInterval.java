package ispw.uniroma2.doctorhouse.model;

import java.time.Duration;
import java.time.LocalTime;

public interface TimeInterval extends Comparable<TimeInterval> {
    boolean contains(TimeInterval other);
    boolean contains(LocalTime point);
    Duration getDuration();
    LocalTime getEnd();
    LocalTime getStart();
    boolean overlaps(TimeInterval other);
}
