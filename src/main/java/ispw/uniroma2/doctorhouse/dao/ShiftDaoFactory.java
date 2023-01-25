package ispw.uniroma2.doctorhouse.dao;

import java.sql.Connection;

public interface ShiftDaoFactory {
    ShiftDao create();
    void setConnection(Connection connection);
}
