package ispw.uniroma2.doctorhouse.dao.exceptions;

import java.time.LocalDateTime;

public class InvalidTimeSlot extends Exception {
    private final LocalDateTime invalidSlot;

    public InvalidTimeSlot(LocalDateTime invalidSlot) {
        this.invalidSlot = invalidSlot;
    }

    public LocalDateTime getInvalidSlot() {
        return invalidSlot;
    }
}
