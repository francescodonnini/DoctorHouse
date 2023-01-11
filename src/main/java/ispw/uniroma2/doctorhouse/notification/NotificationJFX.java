package ispw.uniroma2.doctorhouse.notification;

import ispw.uniroma2.doctorhouse.Dispatcher;
import ispw.uniroma2.doctorhouse.EndPoint;
import ispw.uniroma2.doctorhouse.patienthomepage.HomePageJFX;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescriptionJFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Properties;

public class NotificationJFX implements EndPoint {

    @FXML
    private Button homePage;

    @FXML
    private Button notification;

    @FXML
    private Button prescription;

    private final Dispatcher dispatcher;

    public NotificationJFX(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @FXML
    void goToHome() {
        dispatcher.tryForward(HomePageJFX.class, null);
    }

    @FXML
    void goToPrescription() {
        dispatcher.tryForward(RequestPrescriptionJFX.class, null);
    }
    @Override
    public void onEnter(Properties properties) {
        //This class does not implement this method
    }
}
