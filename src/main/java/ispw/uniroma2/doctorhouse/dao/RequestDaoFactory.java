package ispw.uniroma2.doctorhouse.dao;

import java.sql.Connection;

public interface RequestDaoFactory {
    RequestDao create();
    void setConnection(Connection connection);
}
