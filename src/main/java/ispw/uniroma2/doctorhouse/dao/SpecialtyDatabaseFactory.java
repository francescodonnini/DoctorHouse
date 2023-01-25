package ispw.uniroma2.doctorhouse.dao;

import java.sql.Connection;


public class SpecialtyDatabaseFactory implements SpecialtyDaoFactory {
    private Connection connection;
    @Override
    public SpecialtyDao create() {
        return new SpecialtyDatabase(connection);
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
