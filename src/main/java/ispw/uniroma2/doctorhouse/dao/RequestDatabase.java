package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.beans.PrescriptionRequestBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class RequestDatabase implements RequestDao{
    private static RequestDatabase instance;

    private final Connection connection;

    public RequestDatabase(Connection connection) {
        this.connection = connection;
    }

    public static RequestDatabase getInstance(Properties credentials) {
        if (instance == null) {
            try {
                Connection connection = DriverManager.getConnection(credentials.getProperty("url"), credentials.getProperty("user"), credentials.getProperty("password"));
                instance = new RequestDatabase(connection);
            } catch (SQLException e) {
                throw new IrrecoverableError(e);
            }
        }
        return instance;
    }

    @Override
    public void addRequestOfPrescription(PrescriptionRequestBean requestBean) {
        try (PreparedStatement statement = connection.prepareStatement("CALL request_prescription(?, ?, ?);")) {
            statement.setString(1, requestBean.getPatient().getFamilyDoctor().orElseThrow().getEmail());
            statement.setString(2, requestBean.getPatient().getEmail());
            statement.setString(3, requestBean.getMessage());
            statement.execute();
        } catch (SQLException e) {
            throw new IrrecoverableError(e);
        }
    }
}
