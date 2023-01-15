package ispw.uniroma2.doctorhouse.navigation.login;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.NavigatorController;
import ispw.uniroma2.doctorhouse.navigation.ViewController;

public class LoginNavigator extends Navigator<LoginDestination> {
    private final LoginControllerFactory factory;

    public LoginNavigator(NavigatorController controller, LoginControllerFactory factory) {
        super(controller);
        this.factory = factory;
    }

    @Override
    public void navigate(LoginDestination destination) {
        controller.push(makeViewController(destination));
    }

    private ViewController makeViewController(LoginDestination destination) {
        switch (destination) {
            case LOGIN:
                return factory.createLoginController();
            case SIGNUP:
                return factory.createRegisterViewController();
            default:
                return factory.createIrrecoverableErrorController();
        }
    }
}
