package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public class CanceledInfo extends AppointmentInfo {
    private final User initiator;

    public CanceledInfo(LocalDateTime date, User initiator) {
        super(date);
        this.initiator = initiator;
    }

    public Optional<User> getInitiator() {
        return Optional.ofNullable(initiator);
    }

}