package ispw.uniroma2.doctorhouse.model.appointment;

public class Canceled implements AppointmentState {
    private final CanceledInfo info;

    public Canceled(CanceledInfo info) {
        this.info = info;
    }

    @Override
    public CanceledInfo getInfo() {
        return info;
    }
}