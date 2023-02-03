package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.ToolBarController;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientDestination;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class PatientToolBarController implements ToolBarController {
    private PatientNavigator navigator;
    @FXML
    private GridPane view;

    @FXML
    private Button incomingBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button pendingBtn;

    @FXML
    private Button requestBtn;

    private final ObjectProperty<Button> highlightedBtn;

    public PatientToolBarController() {
        highlightedBtn = new SimpleObjectProperty<>();
    }

    @FXML
    private void initialize() {
        highlightedBtn.addListener((obs, oldVal, newVal) -> {
            if (oldVal != null) {
                oldVal.setStyle("-fx-text-fill: black;");
            }
            newVal.setStyle("-fx-text-fill: red;");
        });
    }

    @Override
    public Parent getView() {
        return view;
    }

    public void setNavigator(PatientNavigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    void goToIncoming(ActionEvent ignored) {
        highlightedBtn.set(incomingBtn);
        navigator.navigate(PatientDestination.REARRANGE);
    }

    @FXML
    void goToLogout(ActionEvent ignored) {
        throw new UnsupportedOperationException();
    }

    @FXML
    void goToPending(ActionEvent ignored) {
        highlightedBtn.set(pendingBtn);
        navigator.navigate(PatientDestination.DO_REARRANGE);
    }

    @FXML
    void goToRequest(ActionEvent ignored) {
        highlightedBtn.set(requestBtn);
        navigator.navigate(PatientDestination.REQUEST_PRESCRIPTION);
    }
}
