import ispw.uniroma2.doctorhouse.Logout;
import ispw.uniroma2.doctorhouse.auth.Login;
import ispw.uniroma2.doctorhouse.auth.LoginFactory;
import ispw.uniroma2.doctorhouse.auth.LoginFactoryImpl;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.dao.ConnectionFactory;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDatabase;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDatabase;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDatabase;
import ispw.uniroma2.doctorhouse.dao.users.UserDatabase;
import ispw.uniroma2.doctorhouse.model.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class LogoutTest {
    //Gentili Emanuele : This test verify that when the user logout the session is destroyed
    @Test
    void testLogout() throws UserNotFound, PersistentLayerException {
        LoginFactory loginFactory = new LoginFactoryImpl(new UserDatabase(ConnectionFactory.getConnection(), new OfficeDatabase(ConnectionFactory.getConnection(), new SpecialtyDatabase(ConnectionFactory.getConnection()), new ShiftDatabase(ConnectionFactory.getConnection()))));
        Login login = loginFactory.create();
        LoginRequestBean loginRequestBean = new LoginRequestBean();
        loginRequestBean.setEmail("sheba.olympie@email.it");
        loginRequestBean.setPassword("1234");
        login.login(loginRequestBean);
        Logout logout = new Logout();
        logout.destroySession();
        try {
            Session.getSession();
            Assertions.fail();
        } catch (IllegalStateException ignored) {
        }
    }
}
