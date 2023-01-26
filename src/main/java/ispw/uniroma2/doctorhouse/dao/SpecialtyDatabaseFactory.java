package ispw.uniroma2.doctorhouse.dao;

import java.sql.Connection;


public class SpecialtyDatabaseFactory implements SpecialtyDaoFactory {
    private final Connection connection;

    public SpecialtyDatabaseFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public SpecialtyDao create() {
        return new SpecialtyDatabase(connection);
    }

}
