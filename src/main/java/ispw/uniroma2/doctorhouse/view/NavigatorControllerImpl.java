package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.NavigatorController;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class NavigatorControllerImpl implements NavigatorController {
    @FXML
    private BorderPane view;

    @Override
    public void push(ViewController controller) {
        view.setCenter(controller.getView());
    }

    @Override
    public void pop() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Parent getView() {
        return view;
    }
}
