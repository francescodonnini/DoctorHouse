package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public class Scheduled implements AppointmentState {
    private final ScheduledInfo info;

    public Scheduled(ScheduledInfo info) {
        this.info = info;
    }

    @Override
    public AppointmentInfo getInfo() {
        return info;
    }

    @Override
    public void cancel(AppointmentImpl appointment, User cancelee) {
        CanceledInfo newStateInfo = new CanceledInfo(info.getDoctor(), info.getPatient(), info.getSpecialty(), info.getOffice(), info.getDate(), cancelee);
        Canceled newState = new Canceled(newStateInfo);
        appointment.setState(newState);
    }

    @Override
    public void confirm(AppointmentImpl appointment, User confirmee) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reschedule(AppointmentImpl appointment, User reschedulee, LocalDateTime newDate) {
        PendingInfo newStateInfo = new PendingInfo(info.getDoctor(), info.getPatient(), info.getSpecialty(), info.getOffice(), reschedulee, info.getDate(), newDate);
        Pending newState = new Pending(newStateInfo);
        appointment.setState(newState);
    }
}
