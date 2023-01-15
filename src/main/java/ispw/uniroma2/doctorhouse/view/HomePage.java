package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientDestination;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class HomePage implements ViewController {
    private final PatientNavigator navigator;
    @FXML
    private Parent view;
    @FXML
    private Button rearrangeAppointmentBtn;
    @FXML
    private Button requestPrescriptionBtn;

    public HomePage(PatientNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public Parent getView() {
       return view;
    }

    @FXML
    private void rearrange(ActionEvent ignored) {
        navigator.navigate(PatientDestination.REARRANGE);
    }

    @FXML
    private void request(ActionEvent ignored) {
        navigator.navigate(PatientDestination.REQUEST_PRESCRIPTION);
    }
}
