package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public class CanceledInfo extends AppointmentInfo {
    private final User initiator;

    public CanceledInfo(LocalDateTime date, User initiator) {
        super(date);
        this.initiator = initiator;
    }

    public User getInitiator() {
        return initiator;
    }

}