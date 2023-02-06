package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.Specialty;
import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

public class AppointmentBuilderImpl implements AppointmentBuilder {
    private User patient;
    private Doctor doctor;
    private Specialty specialty;
    private Office office;
    private LocalDateTime date;
    private LocalDateTime oldDate;
    private User initiator;

    @Override
    public void setPatient(User user) {
        this.patient = user;
    }

    @Override
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    @Override
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public void setOldDate(LocalDateTime oldDate) {
        this.oldDate = oldDate;
    }

    @Override
    public void setOffice(Office office) {
        this.office = office;
    }

    @Override
    public void setInitiator(User user) {
        this.initiator = user;
    }

    @Override
    public Appointment build(Class<? extends AppointmentInfo> type) {
        if (type.equals(CanceledInfo.class)) {
            return makeCanceledInfo();
        } else if (type.equals(PendingInfo.class)) {
            return makePendingInfo();
        } else if (type.equals(IncomingInfo.class)) {
            return makeScheduledInfo();
        } else if (type.equals(ConsumedInfo.class)) {
            return makeConsumedInfo();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private Appointment makeCanceledInfo() {
        if (patient == null || doctor == null || specialty == null || date == null || office == null || initiator == null) {
            throw new IllegalStateException();
        }
        return new AppointmentImpl(doctor, patient, specialty, office, new CanceledInfo(date, initiator));
    }

    private Appointment makeConsumedInfo() {
        if (patient == null || doctor == null || specialty == null || date == null || office == null) {
            throw new IllegalStateException();
        }
        return new AppointmentImpl(doctor, patient, specialty, office, new ConsumedInfo(date));
    }

    private Appointment makePendingInfo() {
        if (patient == null || doctor == null || specialty == null || date == null || oldDate == null || office == null || initiator == null) {
            throw new IllegalStateException();
        }
        return new AppointmentImpl(doctor, patient, specialty, office, new PendingInfo(oldDate, date, specialty.getDuration(), initiator));
    }

    private Appointment makeScheduledInfo() {
        if (patient == null || doctor == null || specialty == null || date == null || office == null) {
            throw new IllegalStateException();
        }
        return new AppointmentImpl(doctor, patient, specialty, office, new IncomingInfo(date, specialty.getDuration()));
    }

    @Override
    public void reset() {
        patient = null;
        doctor = null;
        specialty = null;
        office = null;
        date = null;
        oldDate = null;
        initiator = null;
    }
}
