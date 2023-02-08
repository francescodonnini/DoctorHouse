package ispw.uniroma2.doctorhouse.dao.responses;


import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDao;

import java.sql.Connection;


public class ResponseDaoFactoryImpl implements ResponseDaoFactory{
    private final Connection connection;
    private final PrescriptionDao prescriptionDao;

    public ResponseDaoFactoryImpl(Connection connection, PrescriptionDao prescriptionDao) {
        this.connection = connection;
        this.prescriptionDao = prescriptionDao;
    }

    @Override
    public ResponseDao create() {
        return new ResponseDatabase(connection, prescriptionDao);
    }


}
