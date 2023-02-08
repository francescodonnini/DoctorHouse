package ispw.uniroma2.doctorhouse.dao.appointment;

import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

public interface AppointmentDaoFactory {
    AppointmentDao create() throws PersistentLayerException;
}
