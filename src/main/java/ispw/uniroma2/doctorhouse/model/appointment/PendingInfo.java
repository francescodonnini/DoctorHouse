package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class PendingInfo extends AppointmentInfo implements Slot {
    private final User initiator;
    private final LocalDateTime oldDate;
    private final Duration duration;

    public PendingInfo(LocalDateTime oldDate, LocalDateTime newDate, Duration duration, User initiator) {
        super(newDate);
        this.initiator = initiator;
        this.oldDate = oldDate;
        this.duration = duration;
    }

    public User getInitiator() {
        return initiator;
    }

    public LocalDateTime getOldDate() {
        return oldDate;
    }

    public LocalDateTime getNewDate() {
        return getDate();
    }

    @Override
    public Optional<Slot> getSlot() {
        return Optional.of(new DateTimeInterval(getNewDate(), duration));
    }

    @Override
    public LocalDateTime getDateTime() {
        return getNewDate();
    }

    @Override
    public TimeInterval getInterval() {
        return new ClockInterval(getNewDate().toLocalTime(), duration);
    }
}
