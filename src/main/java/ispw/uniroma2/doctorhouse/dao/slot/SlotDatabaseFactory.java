package ispw.uniroma2.doctorhouse.dao.slot;

import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;

import java.sql.Connection;

public class SlotDatabaseFactory implements SlotDaoFactory {
    private final Connection connection;
    private AppointmentDao appointmentDao;

    public SlotDatabaseFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setAppointmentDao(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    @Override
    public SlotDao create() {
        return new SlotDatabase(connection, appointmentDao);
    }
}
