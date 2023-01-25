package ispw.uniroma2.doctorhouse.dao;

import java.sql.Connection;

public interface UserDaoFactory {
    void setOfficeDao(OfficeDao officeDao);
    UserDao create();

    void setConnection(Connection connection);
}
