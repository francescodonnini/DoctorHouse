package ispw.uniroma2.doctorhouse.dao;

import java.sql.Connection;

public interface SpecialtyDaoFactory {
    SpecialtyDao create();
    void setConnection(Connection connection);
}
