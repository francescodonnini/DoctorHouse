import ispw.uniroma2.doctorhouse.Logout;
import ispw.uniroma2.doctorhouse.auth.Login;
import ispw.uniroma2.doctorhouse.auth.LoginFactory;
import ispw.uniroma2.doctorhouse.auth.LoginFactoryImpl;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.dao.ConnectionFactory;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDatabase;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDatabase;
import ispw.uniroma2.doctorhouse.dao.requests.RequestDatabase;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDatabase;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDatabase;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDatabase;
import ispw.uniroma2.doctorhouse.dao.users.UserDatabase;
import ispw.uniroma2.doctorhouse.model.Request;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestTest {
    //Gentili Emanuele : This test check that when a user send a request the doctor correctly view the request message
    @Test
    void requestTest() throws UserNotFound, PersistentLayerException {
        try {
            init("sheba.olympie@email.it", "1234");
            RequestDatabase requestDatabase = new RequestDatabase(ConnectionFactory.getConnection());
            RequestPrescription requestPrescription = new RequestPrescription(requestDatabase, new ResponseDatabase(ConnectionFactory.getConnection(), new PrescriptionDatabase(ConnectionFactory.getConnection())));
            requestPrescription.sendPrescriptionRequest("Testing");
            destroy();
            init("adah.mickie@email.it", "1234");
            Optional<List<Request>> requests = requestDatabase.requestFinder();
            requests.orElseThrow().forEach(f -> {
                if (f.getPatientEmail().equals("sheba.olympie@email.it"))
                    assertEquals("Testing", f.getMessage());
            });
        } catch (PersistentLayerException | UserNotFound e) {
            Assertions.fail();
        }
    }

    private void init(String email, String password) throws UserNotFound, PersistentLayerException {
        LoginFactory loginFactory = new LoginFactoryImpl(new UserDatabase(ConnectionFactory.getConnection(), new OfficeDatabase(ConnectionFactory.getConnection(), new SpecialtyDatabase(ConnectionFactory.getConnection()), new ShiftDatabase(ConnectionFactory.getConnection()))));
        Login login = loginFactory.create();
        LoginRequestBean loginRequestBean = new LoginRequestBean();
        loginRequestBean.setEmail(email);
        loginRequestBean.setPassword(password);
        login.login(loginRequestBean);
    }

    private void destroy() {
        Logout logout = new Logout();
        logout.destroySession();
    }

    void cleanDatabase() throws PersistentLayerException {
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement("DELETE FROM requests WHERE message = 'Testing'")){
            ps.execute();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }
}

