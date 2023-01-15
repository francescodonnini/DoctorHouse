package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.ViewController;
import javafx.fxml.FXML;
import javafx.scene.Parent;

public class IrrecoverableErrorPage implements ViewController {
    @FXML
    private Parent view;

    @Override
    public Parent getView() {
        return view;
    }
}
