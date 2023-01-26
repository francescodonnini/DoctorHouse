package ispw.uniroma2.doctorhouse.dao;


import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.model.Session;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResponseDatabase implements ResponseDao{

    private final Connection connection;

    public ResponseDatabase(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<List<DoctorRequestBean>> requestFinder() {
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
            return Optional.empty();
        }
    }
}
