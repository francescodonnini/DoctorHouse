package ispw.uniroma2.doctorhouse.navigation.login;

import ispw.uniroma2.doctorhouse.navigation.ViewController;

import java.io.IOException;

public interface LoginControllerFactory {
    ViewController createRegisterViewController() throws IOException;
    ViewController createIrrecoverableErrorController() throws IOException;
    ViewController createLoginController() throws IOException;
    ViewController createPatientHomePage() throws IOException;
    ViewController createDoctorHomePage() throws IOException;
}
