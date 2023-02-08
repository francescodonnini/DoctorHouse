package ispw.uniroma2.doctorhouse.dao.requests;

import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RequestDatabase implements RequestDao{

    private final Connection connection;

    public RequestDatabase(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addRequestOfPrescription(String message) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL request_prescription(?, ?, ?);")) {
            statement.setString(1, Session.getSession().getUser().getFamilyDoctor().orElseThrow().getEmail());
            statement.setString(2, Session.getSession().getUser().getEmail());
            statement.setString(3, message);
            statement.execute();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Optional<List<DoctorRequestBean>> requestFinder() throws PersistentLayerException {
        List<DoctorRequestBean> bean = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("CALL  search_request(?);")) {
            ps.setString(1, Session.getSession().getUser().getEmail());
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while(rs.next()) {
                DoctorRequestBean requestBean = new DoctorRequestBean();
                requestBean.setId(rs.getInt(1));
                requestBean.setPatient(rs.getString(3));
                requestBean.setMessage(rs.getString(4));
                bean.add(requestBean);
            }
            return Optional.of(bean);
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }
}
