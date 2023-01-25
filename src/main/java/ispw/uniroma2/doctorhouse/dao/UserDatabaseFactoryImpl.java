package ispw.uniroma2.doctorhouse.dao;


import java.sql.Connection;

public class UserDatabaseFactoryImpl implements UserDaoFactory {
    private OfficeDao officeDao;
    private Connection connection;

    @Override
    public void setOfficeDao(OfficeDao officeDao) {
        this.officeDao = officeDao;
    }

    @Override
    public UserDao create() {
        return new UserDatabase(connection, officeDao);
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
