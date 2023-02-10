package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class IncomingInfo extends AppointmentInfo implements Slot {
    private final Duration duration;

    public IncomingInfo(LocalDateTime date, Duration duration) {
        super(date);
        this.duration = duration;
    }

    @Override
    public Optional<Slot> getSlot() {
        return Optional.of(new DateTimeInterval(getDate(), duration));
    }

    @Override
    public LocalDateTime getDateTime() {
        return getDate();
    }

    @Override
    public TimeInterval getInterval() {
        return new ClockInterval(getDateTime().toLocalTime(), duration);
    }
}