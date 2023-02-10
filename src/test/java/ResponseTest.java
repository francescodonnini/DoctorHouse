import ispw.uniroma2.doctorhouse.auth.Login;
import ispw.uniroma2.doctorhouse.auth.LoginFactory;
import ispw.uniroma2.doctorhouse.auth.LoginFactoryImpl;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.dao.ConnectionFactory;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDatabase;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDatabase;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDao;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDatabase;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDatabase;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDatabase;
import ispw.uniroma2.doctorhouse.dao.users.UserDatabase;
import ispw.uniroma2.doctorhouse.model.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseTest {

    //Gentili Emanuele : This test verified that for all prescription inserted in database for a specific patient there's the name's field
    @Test
    void prescriptionTest() throws PersistentLayerException, UserNotFound {
        LoginFactory loginFactory = new LoginFactoryImpl(new UserDatabase(ConnectionFactory.getConnection(), new OfficeDatabase(ConnectionFactory.getConnection(), new SpecialtyDatabase(ConnectionFactory.getConnection()), new ShiftDatabase(ConnectionFactory.getConnection()))));
        Login login = loginFactory.create();
        LoginRequestBean loginRequestBean = new LoginRequestBean();
        loginRequestBean.setEmail("sheba.olympie@email.it");
        loginRequestBean.setPassword("1234");
        login.login(loginRequestBean);
        ResponseDao responseDao = new ResponseDatabase(ConnectionFactory.getConnection(), new PrescriptionDatabase(ConnectionFactory.getConnection()));
        Optional<List<Response>> responses = responseDao.responseFinder();
        responses.get().forEach(f -> {
            if(f.getPrescription().getKind() != null)
                assertTrue(!f.getPrescription().getName().isEmpty());
        });
    }
}
