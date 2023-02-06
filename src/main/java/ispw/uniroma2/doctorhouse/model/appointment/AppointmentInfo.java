package ispw.uniroma2.doctorhouse.model.appointment;

import java.time.LocalDateTime;

public abstract class AppointmentInfo {
    private final LocalDateTime date;

    protected AppointmentInfo(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
