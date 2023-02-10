package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

interface AppointmentState {
    AppointmentInfo getInfo();
    default void cancel(AppointmentImpl appointment, User initiator) {
        throw new UnsupportedOperationException();
    }
    default void confirm(AppointmentImpl appointment, User initiator) {
        throw new UnsupportedOperationException();
    }
    default void reschedule(AppointmentImpl appointment, LocalDateTime newDate, User initiator) {
        throw new UnsupportedOperationException();
    }
}
