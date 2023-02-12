import ispw.uniroma2.doctorhouse.auth.Login;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.ConnectionFactory;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDaoFactory;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDao;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDaoFactory;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDaoFactory;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.users.UserDao;
import ispw.uniroma2.doctorhouse.dao.users.UserDaoFactory;
import ispw.uniroma2.doctorhouse.dao.users.UserDatabaseFactoryImpl;
import ispw.uniroma2.doctorhouse.model.Specialty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

class LoginTest {
    private Connection connection;
    private Login login;

    @BeforeEach
    void setup() {
        connection = ConnectionFactory.getConnection();
        SpecialtyDaoFactory specialtyDaoFactory = new SpecialtyDatabaseFactory(connection);
        SpecialtyDao specialtyDao = specialtyDaoFactory.create();
        ShiftDaoFactory shiftDaoFactory = new ShiftDatabaseFactory(connection);
        ShiftDao shiftDao = shiftDaoFactory.create();
        OfficeDaoFactory officeDaoFactory = new OfficeDatabaseFactory(connection, specialtyDao, shiftDao);
        OfficeDao officeDao = officeDaoFactory.create();
        UserDaoFactory userDaoFactory = new UserDatabaseFactoryImpl(officeDao, connection);
        UserDao userDao = userDaoFactory.create();
        login = new Login(userDao);
    }

    @Test
    void testLogin() throws UserNotFound, PersistentLayerException {
        LoginRequestBean requestBean = new LoginRequestBean();
        requestBean.setEmail("theadora.quinta@email.it");
        requestBean.setPassword("1234");
        UserBean bean = login.login(requestBean);
        Assertions.assertEquals("theadora.quinta@email.it", bean.getEmail());
    }

    @AfterEach
    void close() throws SQLException {
        connection.close();
    }
}
