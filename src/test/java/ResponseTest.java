
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
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDao;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDatabase;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDatabase;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDatabase;
import ispw.uniroma2.doctorhouse.dao.users.UserDatabase;
import ispw.uniroma2.doctorhouse.model.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ResponseTest {

    @Test
    void prescriptionTest() {
        try {
            init("sheba.olympie@email.it", "1234");
            ResponseDao responseDao = new ResponseDatabase(ConnectionFactory.getConnection(), new PrescriptionDatabase(ConnectionFactory.getConnection()));
            Optional<List<Response>> responseList = responseDao.responseFinder();
            if(!responseList.isEmpty()) {
                responseList.get().forEach(f -> {
                    assertFalse(f.getPrescription().getName().isEmpty());
                });
            } else Assertions.fail(); //If the responseList is empty the test fail
        } catch (UserNotFound | PersistentLayerException e) {
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
}
