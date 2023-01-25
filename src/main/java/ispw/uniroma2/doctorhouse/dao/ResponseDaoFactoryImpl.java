package ispw.uniroma2.doctorhouse.dao;


import java.sql.Connection;


public class ResponseDaoFactoryImpl implements ResponseDaoFactory{
    private Connection connection;
    @Override
    public ResponseDao create() {
        return new ResponseDatabase(connection);
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
