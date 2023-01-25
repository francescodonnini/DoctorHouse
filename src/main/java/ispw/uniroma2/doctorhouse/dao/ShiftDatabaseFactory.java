package ispw.uniroma2.doctorhouse.dao;

import java.sql.Connection;


public class ShiftDatabaseFactory implements ShiftDaoFactory {

    private Connection connection;
    @Override
    public ShiftDao create() {
        return new ShiftDatabase(connection);
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
