package ispw.uniroma2.doctorhouse.dao.prescriptions;

import ispw.uniroma2.doctorhouse.beans.PrescriptionBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Prescription;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PrescriptionDatabase implements PrescriptionDao {
    private final Connection connection;

    public PrescriptionDatabase(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int savePrescription(PrescriptionBean bean) {
        try(PreparedStatement ps = connection.prepareStatement("CALL  add_prescription(?, ?, ?);")) {
            ps.setString(2, bean.getName());
            if(bean.getQuantity() == 0) {
                ps.setString(1, "Visit");
                ps.setInt(3, bean.getQuantity());
            } else {
                ps.setString(1, "Drug");
                ps.setInt(3, bean.getQuantity());
            }
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if(rs.next()) {
                return rs.getInt(1);
            } else return -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public Prescription getPrescription(int prescriptionId) throws PersistentLayerException {
        String kind = "";
        String name = "";
        int quantity = 0;
        try(PreparedStatement ps = connection.prepareStatement("CALL view_prescription(?)")){
            ps.setInt(1, prescriptionId);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if(rs.next()) {
                kind = rs.getString(1);
                name = rs.getString(2);
                quantity = rs.getInt(3);
            }
            return new Prescription(kind, name, quantity);
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

}
