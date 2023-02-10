package ispw.uniroma2.doctorhouse.model;

import java.time.LocalDateTime;

public interface Slot {
    LocalDateTime getDateTime();
    TimeInterval getInterval();
}
