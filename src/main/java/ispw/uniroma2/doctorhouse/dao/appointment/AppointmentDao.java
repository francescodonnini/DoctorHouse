package ispw.uniroma2.doctorhouse.dao.appointment;

import ispw.uniroma2.doctorhouse.beans.AppointmentBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.appointment.Appointment;
import ispw.uniroma2.doctorhouse.model.appointment.AppointmentInfo;

import java.util.List;
import java.util.Optional;

public interface AppointmentDao {
    Optional<Appointment> create(AppointmentBean appointment) throws PersistentLayerException;
    List<Appointment> findByEmail(String participantEmail, Class<? extends AppointmentInfo> type) throws PersistentLayerException;
    List<Appointment> findByOffice(String doctorEmail, int officeId, Class<? extends AppointmentInfo> type) throws PersistentLayerException;
    void save(Appointment appointment) throws PersistentLayerException, InvalidTimeSlot;
}
