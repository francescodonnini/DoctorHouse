package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.Logout;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.login.LoginDestination;
import ispw.uniroma2.doctorhouse.navigation.login.LoginNavigator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class HomePage implements ViewController {
    @FXML
    private Parent view;
    @FXML
    private TabPane tab;
    @FXML
    private Button logout;
    private final ViewController rearrangeAppointment;
    private final ViewController doRearrangeAppointment;
    private final ViewController requestPrescription;
    private final Logout logoutController;

    private final LoginNavigator navigator;


    public HomePage(ViewController rearrangeAppointment, ViewController doRearrangeAppointment, ViewController requestPrescription, Logout logoutController, LoginNavigator navigator) {
        this.rearrangeAppointment = rearrangeAppointment;
        this.doRearrangeAppointment = doRearrangeAppointment;
        this.requestPrescription = requestPrescription;
        this.logoutController = logoutController;
        this.navigator = navigator;
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    private void initialize() {
        tab.getTabs().addAll(
                new Tab("Rearrange", rearrangeAppointment.getView()),
                new Tab("Do Rearrange", doRearrangeAppointment.getView()),
                new Tab("Request Prescription", requestPrescription.getView())
        );
    }

    public void logout() {
        logoutController.destroySession();
        navigator.navigate(LoginDestination.LOGIN);
    }
}