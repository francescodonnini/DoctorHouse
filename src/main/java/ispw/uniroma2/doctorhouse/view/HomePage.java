package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.ViewController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class HomePage implements ViewController {
    @FXML
    private Parent view;
    @FXML
    private TabPane tab;
    private final ViewController rearrangeAppointment;
    private final ViewController doRearrangeAppointment;
    private final ViewController requestPrescription;

    public HomePage(ViewController rearrangeAppointment, ViewController doRearrangeAppointment, ViewController requestPrescription) {
        this.rearrangeAppointment = rearrangeAppointment;
        this.doRearrangeAppointment = doRearrangeAppointment;
        this.requestPrescription = requestPrescription;
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
}