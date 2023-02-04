package ispw.uniroma2.doctorhouse.navigation.login;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.NavigatorController;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import javafx.application.Platform;

import java.io.IOException;

public class LoginNavigator extends Navigator<LoginDestination> {
    private final LoginControllerFactory factory;

    public LoginNavigator(NavigatorController controller, LoginControllerFactory factory) {
        super(controller);
        this.factory = factory;
    }

    @Override
    public void navigate(LoginDestination destination) {
        try {
            controller.push(makeViewController(destination));
        } catch (IOException e) {
            Platform.exit();
        }
    }
    private ViewController makeViewController(LoginDestination destination) throws IOException {
        switch (destination) {
            case LOGIN:
                return factory.createLoginController();
            case DOCTOR_HOME_PAGE:
                return factory.createDoctorHomePage();
            case PATIENT_HOME_PAGE:
                return factory.createPatientHomePage();
            case SIGNUP:
                return factory.createRegisterViewController();
            default:
                return factory.createIrrecoverableErrorController();
        }
    }
}
