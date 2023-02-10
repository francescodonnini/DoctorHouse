package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.Slot;

import java.time.LocalDateTime;
import java.util.Optional;

public abstract class AppointmentInfo {
    private final LocalDateTime date;
    protected AppointmentInfo(LocalDateTime date) {
        this.date = date;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public Optional<Slot> getSlot() {
        return Optional.empty();
    }
}
