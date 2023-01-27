package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public interface Appointment {
    void confirm(User initiator);
    void cancel(User initiator);
    void reschedule(User initiator, LocalDateTime newDate);
    AppointmentInfo getInfo();
}
