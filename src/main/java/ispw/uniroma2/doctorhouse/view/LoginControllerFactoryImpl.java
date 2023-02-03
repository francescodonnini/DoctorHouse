package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.auth.Login;
import ispw.uniroma2.doctorhouse.auth.RegisterUser;
import ispw.uniroma2.doctorhouse.dao.users.UserDaoFactory;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorNavigator;
import ispw.uniroma2.doctorhouse.navigation.login.LoginControllerFactory;
import ispw.uniroma2.doctorhouse.navigation.login.LoginNavigator;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class LoginControllerFactoryImpl implements LoginControllerFactory {
    private LoginNavigator loginNavigator;
    private PatientNavigator patientNavigator;
    private DoctorNavigator doctorNavigator;
    private UserDaoFactory userDaoFactory;

    public void setLoginNavigator(LoginNavigator loginNavigator) {
        this.loginNavigator = loginNavigator;
    }

    public void setPatientNavigator(PatientNavigator patientNavigator) {
        this.patientNavigator = patientNavigator;
    }

    public void setUserDaoFactory(UserDaoFactory userDaoFactory) {
        this.userDaoFactory = userDaoFactory;
    }

    public void setDoctorNavigator(DoctorNavigator doctorNavigator) {
        this.doctorNavigator = doctorNavigator;
    }

    @Override
    public ViewController createRegisterViewController() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("register-user-page.fxml"));
        loader.setControllerFactory(f -> new RegisterUserPage(loginNavigator, new RegisterUser(userDaoFactory.create())));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }

    @Override
    public ViewController createIrrecoverableErrorController() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("irrecoverable-error-page.fxml"));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }

    @Override
    public ViewController createLoginController() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login-user-page.fxml"));
        loader.setControllerFactory(f -> new LoginUserPage(loginNavigator, patientNavigator, doctorNavigator, new Login(userDaoFactory.create())));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }
}
