package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.*;

import java.time.LocalDateTime;

public class IncomingInfo extends AppointmentInfo implements TakenSlot {
    public IncomingInfo(Doctor doctor, User patient, Specialty specialty, Office office, LocalDateTime date) {
        super(doctor, patient, specialty, office, date);
    }

    @Override
    public LocalDateTime getDateTime() {
        return getDate();
    }

    @Override
    public TimeInterval getInterval() {
        return new ClockInterval(getDateTime().toLocalTime(), getSpecialty().getDuration());
    }
}