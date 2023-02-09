package ispw.uniroma2.doctorhouse.dao.responses;


import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.NotValidRequest;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Prescription;
import ispw.uniroma2.doctorhouse.model.Response;
import ispw.uniroma2.doctorhouse.model.Session;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResponseDatabase implements ResponseDao{

    private final Connection connection;

    private final PrescriptionDao prescriptionDao;

    private static final String QUERY = "CALL insert_response(?, ?, ?);";

    public ResponseDatabase(Connection connection, PrescriptionDao prescriptionDao) {
        this.connection = connection;
        this.prescriptionDao = prescriptionDao;
    }

    @Override
    public void insertResponse(ResponseBean responseBean, PrescriptionBean prescriptionBean) throws PersistentLayerException, NotValidRequest {
        int prescriptionId = prescriptionDao.savePrescription(prescriptionBean);
        try (PreparedStatement ps = connection.prepareStatement(QUERY)){
            ps.setInt(1, responseBean.getRequestId());
            ps.setString(2, responseBean.getMessage());
            ps.setInt(3, prescriptionId);
            ps.execute();
        } catch (SQLException e) {
            if(e.getSQLState().equals("23000")) {
                throw new NotValidRequest();
            } else throw new PersistentLayerException(e);
        }
    }

    public Optional<List<Response>> responseFinder() throws PersistentLayerException {
        List<Response> responses = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement("CALL view_response(?)")){
            ps.setString(1, Session.getSession().getUser().getEmail());
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while(rs.next()) {
                String message = rs.getString(1);
                String kind = rs.getString(2);
                String name = rs.getString(3);
                int quantity = rs.getInt(4);
                Response response = new Response(message, new Prescription(kind, name, quantity));
                responses.add(response);
            }
            return Optional.of(responses);
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }
    @Override
    public void insertRejection(ResponseBean bean) throws PersistentLayerException {
        try(PreparedStatement ps = connection.prepareStatement(QUERY)) {
            ps.setInt(1, bean.getRequestId());
            ps.setString(2, bean.getMessage());
            ps.setNull(3, Types.INTEGER);
            ps.execute();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

}

