package ispw.uniroma2.doctorhouse.model;

import java.time.LocalDateTime;

public interface TakenSlot {
    LocalDateTime getDateTime();
    TimeInterval getInterval();
}
