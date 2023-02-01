package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.*;

import java.time.LocalDateTime;

public class PendingInfo extends AppointmentInfo implements TakenSlot {
    private final User initiator;
    private final LocalDateTime oldDate;

    public PendingInfo(Doctor doctor, User patient, Specialty specialty, Office office, User initiator, LocalDateTime oldDate, LocalDateTime newDate) {
        super(doctor, patient, specialty, office, newDate);
        this.initiator = initiator;
        this.oldDate = oldDate;
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
    public LocalDateTime getDateTime() {
        return getNewDate();
    }

    @Override
    public TimeInterval getInterval() {
        return new ClockInterval(getNewDate().toLocalTime(), getSpecialty().getDuration());
    }
}
