package ispw.uniroma2.doctorhouse.dao.appointment;

import ispw.uniroma2.doctorhouse.beans.AppointmentBean;
import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.TakenSlot;
import ispw.uniroma2.doctorhouse.model.appointment.Appointment;
import ispw.uniroma2.doctorhouse.model.appointment.AppointmentInfo;

import java.util.List;
import java.util.Optional;

public interface AppointmentDao {
    Optional<Appointment> create(AppointmentBean appointment) throws PersistentLayerException;
    List<Appointment> find(UserBean participant, Class<? extends AppointmentInfo> type) throws PersistentLayerException;
    List<TakenSlot> find(OfficeBean office) throws PersistentLayerException;
    void save(Appointment appointment) throws PersistentLayerException, InvalidTimeSlot;
}
