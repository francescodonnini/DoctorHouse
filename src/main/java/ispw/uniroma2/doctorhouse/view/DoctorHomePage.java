package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorDestination;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorNavigator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class DoctorHomePage implements ViewController {
    @FXML
    private Button rearrangeAppointmentBtn;

    @FXML
    private Button requestPrescriptionBtn;

    @FXML
    private BorderPane view;

    private final DoctorNavigator navigator;

    public DoctorHomePage(DoctorNavigator navigator) {
        this.navigator = navigator;
    }

    public void rearrange() {
        navigator.navigate(DoctorDestination.REARRANGE_APPOINTMENT);
    }

    public void request() {
        navigator.navigate(DoctorDestination.RESPONSE_PRESCRIPTION_REQUEST);
    }

    @Override
    public Parent getView() {
        return view;
    }
}
