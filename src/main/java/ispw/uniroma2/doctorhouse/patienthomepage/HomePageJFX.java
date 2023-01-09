package ispw.uniroma2.doctorhouse.patienthomepage;

import ispw.uniroma2.doctorhouse.Dispatcher;
import ispw.uniroma2.doctorhouse.EndPoint;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Properties;

public class HomePageJFX implements EndPoint {

    @FXML
    private Button notification;

    @FXML
    private Button prescription;

    private final Dispatcher dispatcher;

    public HomePageJFX(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }


    @FXML
    void goToNotification(ActionEvent event) {
        //This method will be implemented
    }

    @FXML
    void goToPrescription(ActionEvent event) {

    }

    @Override
    public void onEnter(Properties properties) {
        //This class do not implement this method
    }


}
