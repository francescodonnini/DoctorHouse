package ispw.uniroma2.doctorhouse.requestprescription;

import ispw.uniroma2.doctorhouse.Dispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class RequestPrescriptionJFX {
    @FXML
    private Button enter;

    @FXML
    private Button home;

    @FXML
    private TextField message;

    @FXML
    private Button notification;

    @FXML
    private Button prescription;

    @FXML
    private Text text;

    private final Dispatcher dispatcher;

    public RequestPrescriptionJFX(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @FXML
    void enter(ActionEvent event) {
        //this method will be implemented
    }

    @FXML
    void goToHomePage(ActionEvent event) {
        //this method will be implemented
    }

    @FXML
    void goToNotification(ActionEvent event) {
        //this method will be implemented
    }

}
