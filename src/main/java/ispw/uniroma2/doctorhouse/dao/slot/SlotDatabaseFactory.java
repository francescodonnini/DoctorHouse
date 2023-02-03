package ispw.uniroma2.doctorhouse.dao.slot;

import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;

import java.sql.Connection;

public class SlotDatabaseFactory implements SlotDaoFactory {
    private final Connection connection;
    private final AppointmentDao appointmentDao;

    public SlotDatabaseFactory(Connection connection, AppointmentDao appointmentDao) {
        this.connection = connection;
        this.appointmentDao = appointmentDao;
    }

    @Override
    public SlotDao create() {
        return new SlotDatabase(connection, appointmentDao);
    }
}
