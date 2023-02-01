package ispw.uniroma2.doctorhouse.rearrange;

import ispw.uniroma2.doctorhouse.beans.PendingAppointmentBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

import java.util.List;

public interface DoRearrange {
    void submit(PendingAppointmentBean pendingAppointmentBean, What what) throws InvalidTimeSlot, PersistentLayerException;
    List<PendingAppointmentBean> getPendingAppointments() throws PersistentLayerException;
}
