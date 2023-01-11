package ispw.uniroma2.doctorhouse.requestprescription;

import ispw.uniroma2.doctorhouse.Dispatcher;
import ispw.uniroma2.doctorhouse.EndPoint;
import ispw.uniroma2.doctorhouse.notification.NotificationJFX;
import ispw.uniroma2.doctorhouse.patienthomepage.HomePageJFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Properties;

public class RequestPrescriptionJFX implements EndPoint {

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
    void enter() {
        //This method will be implemented
    }

    @FXML
    void goToHomePage() {
        dispatcher.tryForward(HomePageJFX.class, null);
    }

    @FXML
    void goToNotification() {
        dispatcher.tryForward(NotificationJFX.class, null);
    }
    @Override
    public void onEnter(Properties properties) {
        //this class does not implement this method
    }
}
