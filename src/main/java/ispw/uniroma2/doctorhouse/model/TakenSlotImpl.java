package ispw.uniroma2.doctorhouse.model;

import java.time.LocalDateTime;

public class TakenSlotImpl implements TakenSlot {
    private final LocalDateTime dateTime;
    private final TimeInterval interval;

    public TakenSlotImpl(LocalDateTime dateTime, TimeInterval interval) {
        this.dateTime = dateTime;
        this.interval = interval;
    }

    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public TimeInterval getInterval() {
        return interval;
    }
}
