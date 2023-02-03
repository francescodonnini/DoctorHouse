package ispw.uniroma2.doctorhouse.dao.shift;

import java.sql.Connection;


public class ShiftDatabaseFactory implements ShiftDaoFactory {

    private final Connection connection;

    public ShiftDatabaseFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ShiftDao create() {
        return new ShiftDatabase(connection);
    }

}
