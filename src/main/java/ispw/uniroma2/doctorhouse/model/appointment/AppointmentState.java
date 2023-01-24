package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

interface AppointmentState {
    AppointmentInfo getInfo();

    void cancel(AppointmentImpl appointment, User cancelee);

    void confirm(AppointmentImpl appointment, User confirmee);

    void reschedule(AppointmentImpl appointment, User reschedulee, LocalDateTime newDate);
}