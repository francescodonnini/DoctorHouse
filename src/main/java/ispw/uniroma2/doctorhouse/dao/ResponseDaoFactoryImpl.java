package ispw.uniroma2.doctorhouse.dao;


import java.sql.Connection;


public class ResponseDaoFactoryImpl implements ResponseDaoFactory{
    private final Connection connection;

    public ResponseDaoFactoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ResponseDao create() {
        return new ResponseDatabase(connection);
    }


}
