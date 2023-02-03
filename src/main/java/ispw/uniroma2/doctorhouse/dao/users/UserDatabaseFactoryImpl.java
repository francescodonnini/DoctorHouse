package ispw.uniroma2.doctorhouse.dao.users;


import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;

import java.sql.Connection;

public class UserDatabaseFactoryImpl implements UserDaoFactory {
    private final OfficeDao officeDao;
    private final Connection connection;

    public UserDatabaseFactoryImpl(OfficeDao officeDao, Connection connection) {
        this.officeDao = officeDao;
        this.connection = connection;
    }

    public UserDao create() {
        return new UserDatabase(connection, officeDao);
    }
}
