package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class ScheduledInfo extends AppointmentInfo {
    private final Duration duration;

    public ScheduledInfo(LocalDateTime date, Duration duration) {
        super(date);
        this.duration = duration;
    }

    @Override
    public Optional<Slot> getSlot() {
        return Optional.of(new DateTimeInterval(getDate(), duration));
    }

    public Duration getDuration() {
        return duration;
    }
}