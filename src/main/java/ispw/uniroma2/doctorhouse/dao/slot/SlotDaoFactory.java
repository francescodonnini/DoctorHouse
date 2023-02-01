package ispw.uniroma2.doctorhouse.dao.slot;

import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;

public interface SlotDaoFactory {
    void setAppointmentDao(AppointmentDao appointmentDao);
    SlotDao create();
}
