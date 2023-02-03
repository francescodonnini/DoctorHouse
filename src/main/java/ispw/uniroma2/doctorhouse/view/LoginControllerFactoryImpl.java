package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.auth.RegisterUserFactory;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorNavigator;
import ispw.uniroma2.doctorhouse.navigation.login.LoginControllerFactory;
import ispw.uniroma2.doctorhouse.auth.LoginFactory;
import ispw.uniroma2.doctorhouse.navigation.login.LoginNavigator;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class LoginControllerFactoryImpl implements LoginControllerFactory {
    private LoginNavigator loginNavigator;
    private final PatientNavigator patientNavigator;
    private final DoctorNavigator doctorNavigator;
    private final LoginFactory loginFactory;
    private final RegisterUserFactory registerUserFactory;

    public LoginControllerFactoryImpl(LoginFactory loginFactory, RegisterUserFactory registerUserFactory, PatientNavigator patientNavigator, DoctorNavigator doctorNavigator) {
        this.loginFactory = loginFactory;
        this.registerUserFactory = registerUserFactory;
        this.patientNavigator = patientNavigator;
        this.doctorNavigator = doctorNavigator;
    }

    public void setLoginNavigator(LoginNavigator loginNavigator) {
        this.loginNavigator = loginNavigator;
    }

    @Override
    public ViewController createRegisterViewController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("register-user-page.fxml"));
        loader.setControllerFactory(f -> new RegisterUserPage(loginNavigator, registerUserFactory.create()));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createIrrecoverableErrorController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("irrecoverable-error-page.fxml"));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createLoginController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login-user-page.fxml"));
        loader.setControllerFactory(f -> new LoginUserPage(loginNavigator, patientNavigator, doctorNavigator, loginFactory.create()));
        loader.load();
        return loader.getController();
    }
}
