package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.PrescriptionRequestBean;
import ispw.uniroma2.doctorhouse.model.Session;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;
import ispw.uniroma2.doctorhouse.view.exception.NullMessage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class RequestPrescriptionGraphicController implements ViewController {
    @FXML
    private Button rearrangeAppointmentBtn;

    @FXML
    private Button requestPrescriptionBtn;

    @FXML
    private Button sendRequest;

    @FXML
    private TextField textRequest;

    @FXML
    private BorderPane view;
    private final PatientNavigator navigator;

    private final RequestPrescription requestPrescription;

    public RequestPrescriptionGraphicController(PatientNavigator navigator, RequestPrescription requestPrescription) {
        this.navigator = navigator;
        this.requestPrescription = requestPrescription;
    }

    @Override
    public Parent getView() {
        return view;
    }

    public void rearrange() {
        //
    }

    public void request() {
        //
    }

    public void sendRequest() throws NullMessage{
        String message = textRequest.getText();
        if(message.trim().isEmpty()) {
            throw new NullMessage();
        } else {
            PrescriptionRequestBean requestBean = new PrescriptionRequestBean();
            requestBean.setMessage(message);
            requestBean.setPatient(Session.getSession().getUser());
            requestPrescription.sendPrescriptionRequest(requestBean);
        }
    }
}
