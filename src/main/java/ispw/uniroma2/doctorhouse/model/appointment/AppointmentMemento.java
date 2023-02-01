package ispw.uniroma2.doctorhouse.model.appointment;

public class AppointmentMemento {
    private final AppointmentInfo info;
    public AppointmentMemento(AppointmentInfo info) {
        this.info = info;
    }

    public AppointmentInfo getInfo() {
        return info;
    }
}
