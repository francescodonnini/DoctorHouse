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
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.Map;

public class DoctorHomePage implements ViewController {
    @FXML
    private BorderPane view;
    @FXML
    private TabPane tab;
    @FXML
    private Button logout;
    private final Map<Tab, ViewController> controllerMap;
    private final ViewController rearrangeAppointment;
    private final ViewController doRearrangeAppointment;
    private final ViewController requestPrescription;
    private final ViewController responsePrescription;
    private final Logout logoutController;
    private final LoginNavigator navigator;

    public DoctorHomePage(ViewController rearrangeAppointment, ViewController doRearrangeAppointment, ViewController requestPrescription, ViewController responsePrescription, Logout logoutController, LoginNavigator navigator) {
        this.rearrangeAppointment = rearrangeAppointment;
        this.doRearrangeAppointment = doRearrangeAppointment;
        this.requestPrescription = requestPrescription;
        this.responsePrescription = responsePrescription;
        this.logoutController = logoutController;
        this.navigator = navigator;
        controllerMap = new HashMap<>();
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    private void initialize() {
        Tab scheduledAppointmentsTab = new Tab("Scheduled Appointments", rearrangeAppointment.getView());
        controllerMap.put(scheduledAppointmentsTab, rearrangeAppointment);
        tab.getSelectionModel().selectedItemProperty().addListener(((observable, oldTab, newTab) -> {
            if (newTab != null && controllerMap.containsKey(newTab)) {
                ViewController controller = controllerMap.get(newTab);
                controller.update();
            }
        }));
        tab.getTabs().addAll(
                scheduledAppointmentsTab,
                new Tab("Requests of Rearrangement", doRearrangeAppointment.getView()),
                new Tab("Request Prescription", requestPrescription.getView()),
                new Tab("Response Prescription", responsePrescription.getView())
        );
    }

    @FXML
    public void logout() {
        logoutController.destroySession();
        navigator.navigate(LoginDestination.LOGIN);
    }
}
