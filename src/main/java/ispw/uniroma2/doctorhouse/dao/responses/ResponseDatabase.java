package ispw.uniroma2.doctorhouse.dao.responses;


import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
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
    public void insertVisitResponse(ResponseBean responseBean, VisitPrescriptionBean visitPrescriptionBean) throws PersistentLayerException {
        int prescriptionId = prescriptionDao.addVisitPrescription(visitPrescriptionBean);
        try (PreparedStatement ps = connection.prepareStatement(QUERY)){
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
        try (PreparedStatement ps = connection.prepareStatement(QUERY)){
            ps.setInt(1, responseBean.getRequestId());
            ps.setString(2, responseBean.getMessage());
            ps.setInt(3, prescriptionId);
            ps.execute();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    public Optional<List<ResponsePatientBean>> responseFinder() throws PersistentLayerException {
        List<ResponsePatientBean> bean = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement("CALL view_response(?)")){
            ps.setString(1, Session.getSession().getUser().getEmail());
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while(rs.next()) {
                ResponsePatientBean responsePatientBean = new ResponsePatientBean();
                responsePatientBean.setMessage(rs.getString(1));
                responsePatientBean.setKind(rs.getString(2));
                responsePatientBean.setName(rs.getString(3));
                responsePatientBean.setQuantity(rs.getInt(4));
                bean.add(responsePatientBean);
            }
            return Optional.of(bean);
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

