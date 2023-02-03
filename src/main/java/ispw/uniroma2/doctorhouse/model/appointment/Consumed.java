package ispw.uniroma2.doctorhouse.model.appointment;

public class Consumed implements AppointmentState {
    private final ConsumedInfo info;

    public Consumed(ConsumedInfo info) {
        this.info = info;
    }

    @Override
    public AppointmentInfo getInfo() {
        return info;
    }
}
