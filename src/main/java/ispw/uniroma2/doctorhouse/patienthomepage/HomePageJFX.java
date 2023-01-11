package ispw.uniroma2.doctorhouse.patienthomepage;

import ispw.uniroma2.doctorhouse.Dispatcher;
import ispw.uniroma2.doctorhouse.EndPoint;
import ispw.uniroma2.doctorhouse.notification.NotificationJFX;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescriptionJFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.util.Properties;

public class HomePageJFX implements EndPoint {

    @FXML
    private BorderPane root;

    @FXML
    private Button notification;

    @FXML
    private Button prescription;

    private final Dispatcher dispatcher;

    public HomePageJFX(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }


    @FXML
    void goToNotification() {
        dispatcher.tryForward(NotificationJFX.class, null);
    }

    @FXML
    void goToPrescription() {
        dispatcher.tryForward(RequestPrescriptionJFX.class, null);
    }

    @Override
    public void onEnter(Properties properties) {
        //This class do not implement this method
    }


}