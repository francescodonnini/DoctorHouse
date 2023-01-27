package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

interface AppointmentState {
    AppointmentInfo getInfo();
    void cancel(AppointmentImpl appointment, User initiator);
    void confirm(AppointmentImpl appointment, User initiator);
    void reschedule(AppointmentImpl appointment, LocalDateTime newDate, User initiator);
}
