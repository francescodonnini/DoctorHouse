package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.ViewController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class DoctorHomePage implements ViewController {
    @FXML
    private BorderPane view;

    @Override
    public Parent getView() {
        return view;
    }
}
