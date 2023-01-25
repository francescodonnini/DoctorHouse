package ispw.uniroma2.doctorhouse.dao;

import java.sql.Connection;

public interface ResponseDaoFactory {
    ResponseDao create();
    void setConnection(Connection connection);
}
