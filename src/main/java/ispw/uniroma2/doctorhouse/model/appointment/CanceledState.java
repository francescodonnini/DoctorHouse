package ispw.uniroma2.doctorhouse.model.appointment;

public class CanceledState implements AppointmentState {
    private final CanceledInfo info;

    public CanceledState(CanceledInfo info) {
        this.info = info;
    }

    @Override
    public CanceledInfo getInfo() {
        return info;
    }
}