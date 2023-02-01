package ispw.uniroma2.doctorhouse.dao.appointment;

import ispw.uniroma2.doctorhouse.dao.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.UserDao;

import java.sql.Connection;

public class AppointmentDatabaseFactory implements AppointmentDaoFactory {
    private final Connection connection;
    private OfficeDao officeDao;
    private SpecialtyDao specialtyDao;
    private UserDao userDao;

    public AppointmentDatabaseFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void setOfficeDao(OfficeDao officeDao) {
        this.officeDao = officeDao;
    }

    @Override
    public void setSpecialtyDao(SpecialtyDao specialtyDao) {
        this.specialtyDao = specialtyDao;
    }

    @Override
    public AppointmentDao create() {
        return new AppointmentDatabase(connection, userDao, officeDao, specialtyDao);
    }
}
