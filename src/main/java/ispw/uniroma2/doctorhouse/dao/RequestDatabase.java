package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.beans.PrescriptionRequestBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class RequestDatabase implements RequestDao{

    private final Connection connection;

    public RequestDatabase(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void addRequestOfPrescription(PrescriptionRequestBean requestBean) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL request_prescription(?, ?, ?);")) {
            statement.setString(1, requestBean.getPatient().getFamilyDoctor().orElseThrow().getEmail());
            statement.setString(2, requestBean.getPatient().getEmail());
            statement.setString(3, requestBean.getMessage());
            statement.execute();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }
}
