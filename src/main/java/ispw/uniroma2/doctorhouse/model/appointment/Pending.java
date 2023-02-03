package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public class Pending implements AppointmentState {
    private final PendingInfo info;

    public Pending(PendingInfo info) {
        this.info = info;
    }

    @Override
    public AppointmentInfo getInfo() {
        return info;
    }

    @Override
    public void cancel(AppointmentImpl appointment, User initiator) {
        if (!initiator.equals(info.getInitiator())) {
            CanceledInfo newInfo = new CanceledInfo(info.getDoctor(), info.getPatient(), info.getSpecialty(), info.getOffice(), info.getOldDate(), initiator);
            AppointmentState newState = new Canceled(newInfo);
            appointment.setState(newState);
        }
    }

    @Override
    public void confirm(AppointmentImpl appointment, User initiator) {
        if (!initiator.equals(info.getInitiator())) {
            IncomingInfo newInfo = new IncomingInfo(info.getDoctor(), info.getPatient(), info.getSpecialty(), info.getOffice(), info.getOldDate());
            AppointmentState newState = new Incoming(newInfo);
            appointment.setState(newState);
        }
    }

    @Override
    public void tick(AppointmentImpl appointment, LocalDateTime currentDate) {
       if (!info.getOldDate().isAfter(currentDate)) {
           CanceledInfo newInfo = new CanceledInfo(info.getDoctor(), info.getPatient(), info.getSpecialty(), info.getOffice(), info.getOldDate(), null);
           AppointmentState newState = new Canceled(newInfo);
           appointment.setState(newState);
       }
    }
}
