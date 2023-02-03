package ispw.uniroma2.doctorhouse.dao.appointment;

import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.users.UserDao;

import java.sql.Connection;

public class AppointmentDatabaseFactory implements AppointmentDaoFactory {
    private final Connection connection;
    private final OfficeDao officeDao;
    private final SpecialtyDao specialtyDao;
    private final UserDao userDao;

    public AppointmentDatabaseFactory(Connection connection, OfficeDao officeDao, SpecialtyDao specialtyDao, UserDao userDao) {
        this.connection = connection;
        this.officeDao = officeDao;
        this.specialtyDao = specialtyDao;
        this.userDao = userDao;
    }

    @Override
    public AppointmentDao create() {
        return new AppointmentDatabase(connection, userDao, officeDao, specialtyDao);
    }
}
