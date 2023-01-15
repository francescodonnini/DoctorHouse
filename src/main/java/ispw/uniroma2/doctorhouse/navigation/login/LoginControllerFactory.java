package ispw.uniroma2.doctorhouse.navigation.login;

import ispw.uniroma2.doctorhouse.navigation.ViewController;

public interface LoginControllerFactory {
    ViewController createRegisterViewController();
    ViewController createIrrecoverableErrorController();
    ViewController createLoginController();
}
