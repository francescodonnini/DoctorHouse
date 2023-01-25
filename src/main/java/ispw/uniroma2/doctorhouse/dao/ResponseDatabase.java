package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.model.Session;
import ispw.uniroma2.doctorhouse.view.exception.RequestNotFound;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ResponseDatabase implements ResponseDao{
    private static ResponseDatabase instance;

    private final Connection connection;

    public ResponseDatabase(Connection connection) {
        this.connection = connection;
    }

    public static ResponseDatabase getInstance(Properties credentials) {
        if (instance == null) {
            try {
                Connection connection = DriverManager.getConnection(credentials.getProperty("url"), credentials.getProperty("user"), credentials.getProperty("password"));
                instance = new ResponseDatabase(connection);
            } catch (SQLException e) {
                throw new IrrecoverableError(e);
            }
        }
        return instance;
    }

    @Override
    public Optional<List<DoctorRequestBean>> requestFinder() throws RequestNotFound {
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
