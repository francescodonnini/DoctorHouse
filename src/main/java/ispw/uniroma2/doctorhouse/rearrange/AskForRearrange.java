package ispw.uniroma2.doctorhouse.rearrange;

import ispw.uniroma2.doctorhouse.beans.AppointmentBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.DateTimeInterval;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AskForRearrange {
    void ask(AppointmentBean bean, LocalDateTime newDate) throws InvalidTimeSlot, PersistentLayerException;
    List<DateTimeInterval> getFreeSlots(AppointmentBean bean, LocalDate start, LocalDate endInclusive) throws PersistentLayerException;
    List<AppointmentBean> getIncomingAppointments() throws PersistentLayerException;
}
