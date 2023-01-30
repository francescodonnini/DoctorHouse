package ispw.uniroma2.doctorhouse.dao;


import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Session;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResponseDatabase implements ResponseDao{

    private final Connection connection;

    private final PrescriptionDao prescriptionDao;

    public ResponseDatabase(Connection connection, PrescriptionDao prescriptionDao) {
        this.connection = connection;
        this.prescriptionDao = prescriptionDao;
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

    @Override
    public void insertVisitResponse(ResponseBean responseBean, VisitPrescriptionBean visitPrescriptionBean) throws PersistentLayerException {
        int prescriptionId = prescriptionDao.addVisitPrescription(visitPrescriptionBean);
        try (PreparedStatement ps = connection.prepareStatement("CALL insert_response(?, ?, ?);")){
            ps.setInt(1, responseBean.getRequestId());
            ps.setString(2, responseBean.getMessage());
            ps.setInt(3, prescriptionId);
            ps.execute();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public void insertDrugResponse(ResponseBean responseBean, DrugPrescriptionBean drugPrescriptionBean) throws PersistentLayerException {
        int prescriptionId = prescriptionDao.addDrugPrescription(drugPrescriptionBean);
        try (PreparedStatement ps = connection.prepareStatement("CALL insert_response(?, ?, ?);")){
            ps.setInt(1, responseBean.getRequestId());
            ps.setString(2, responseBean.getMessage());
            ps.setInt(3, prescriptionId);
            ps.execute();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

}
