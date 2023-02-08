package ispw.uniroma2.doctorhouse.dao.prescriptions;

import java.sql.Connection;

public class PrescriptionDatabaseFactory implements PrescriptionDaoFactory{
    private final Connection connection;

    public PrescriptionDatabaseFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PrescriptionDao create() {
        return new PrescriptionDatabase(connection);
    }
}
