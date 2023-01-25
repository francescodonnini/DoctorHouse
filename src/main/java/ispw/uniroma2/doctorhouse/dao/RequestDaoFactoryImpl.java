package ispw.uniroma2.doctorhouse.dao;


import java.sql.Connection;

public class RequestDaoFactoryImpl implements RequestDaoFactory{
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public RequestDao create() {
        return new RequestDatabase(connection);
    }
}
