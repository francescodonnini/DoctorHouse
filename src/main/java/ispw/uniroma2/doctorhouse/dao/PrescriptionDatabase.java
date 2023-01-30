package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.beans.DrugPrescriptionBean;
import ispw.uniroma2.doctorhouse.beans.VisitPrescriptionBean;

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
    public int addDrugPrescription(DrugPrescriptionBean bean) {
        try(PreparedStatement ps = connection.prepareStatement("CALL  add_drug_prescription(?, ?, ?);")) {
            ps.setString(1, "Drug");
            ps.setString(2, bean.getName());
            ps.setInt(3, bean.getQuantity());
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
    public int addVisitPrescription(VisitPrescriptionBean bean) {
        try(PreparedStatement ps = connection.prepareStatement("CALL  add_visit_prescription(?, ?);")) {
            ps.setString(1, "Visit");
            ps.setString(2, bean.getName());
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if(rs.next()) {
                return rs.getInt(1);
            } else return -1;
        } catch (SQLException e) {
            return -1;
        }
    }
}
