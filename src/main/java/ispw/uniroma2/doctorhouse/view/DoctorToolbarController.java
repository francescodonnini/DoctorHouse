package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.ToolBarController;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorDestination;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorNavigator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class DoctorToolbarController implements ToolBarController {
    private DoctorNavigator doctorNavigator;
    private final ObjectProperty<Button> highlightedBtn;
    @FXML
    private Button incomingBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button pendingBtn;

    @FXML
    private Button requestBtn;

    @FXML
    private Button responseBtn;

    @FXML
    private GridPane view;

    public DoctorToolbarController() {
        highlightedBtn = new SimpleObjectProperty<>();
    }

    public void setNavigator(DoctorNavigator navigator) {
        this.doctorNavigator = navigator;
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    private void initialize() {
        highlightedBtn.addListener((obs, oldVal, newVal) -> {
            if (oldVal != null) {
                oldVal.setStyle("-fx-text-fill: black;");
            }
            newVal.setStyle("-fx-text-fill: red");
        });
    }

    @FXML
    private void goToIncoming(ActionEvent ignored) {
        highlightedBtn.set(incomingBtn);
        doctorNavigator.navigate(DoctorDestination.REARRANGE_APPOINTMENT);
    }

    @FXML
    private void goToLogout(ActionEvent ignored) {
        throw new UnsupportedOperationException();
    }

    @FXML
    private void goToPending(ActionEvent ignored) {
        highlightedBtn.set(pendingBtn);
        doctorNavigator.navigate(DoctorDestination.DO_REARRANGE_APPOINTMENT);
    }

    @FXML
    private void goToRequest(ActionEvent ignored) {
        highlightedBtn.set(requestBtn);
        doctorNavigator.navigate(DoctorDestination.REQUEST_PRESCRIPTION);
    }

    @FXML
    private void goToResponse(ActionEvent ignored) {
        highlightedBtn.set(responseBtn);
        doctorNavigator.navigate(DoctorDestination.RESPONSE_PRESCRIPTION_REQUEST);
    }
}
