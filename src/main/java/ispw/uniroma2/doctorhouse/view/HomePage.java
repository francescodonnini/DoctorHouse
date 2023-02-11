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

import java.util.HashMap;
import java.util.Map;

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
    private final Map<Tab, ViewController> tabMap;


    public HomePage(ViewController rearrangeAppointment, ViewController doRearrangeAppointment, ViewController requestPrescription, Logout logoutController, LoginNavigator navigator) {
        this.rearrangeAppointment = rearrangeAppointment;
        this.doRearrangeAppointment = doRearrangeAppointment;
        this.requestPrescription = requestPrescription;
        this.logoutController = logoutController;
        this.navigator = navigator;
        tabMap = new HashMap<>();
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    private void initialize() {
        Tab rearrangeTab = new Tab("Scheduled Appointments", rearrangeAppointment.getView());
        tabMap.put(rearrangeTab, rearrangeAppointment);
        tab.getTabs().addAll(
                rearrangeTab,
                new Tab("Requests of Rearrangement", doRearrangeAppointment.getView()),
                new Tab("Request Prescription", requestPrescription.getView())
        );
        tab.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null && tabMap.containsKey(newTab)) {
                ViewController controller = tabMap.get(newTab);
                controller.update();
            }
        });
    }

    public void logout() {
        logoutController.destroySession();
        navigator.navigate(LoginDestination.LOGIN);
    }
}