package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.PrescriptionRequestBean;
import ispw.uniroma2.doctorhouse.beans.ResponsePatientBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Session;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientDestination;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.List;
import java.util.Optional;


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

    @FXML
    private Label errLbl;

    @FXML
    private Button showResponse;

    @FXML
    private TableView<ResponsePatientBean> responseTable;
    @FXML
    private TableColumn<ResponsePatientBean, String> col1;
    @FXML
    private TableColumn<ResponsePatientBean, String> col2;
    @FXML
    private TableColumn<ResponsePatientBean, String> col3;
    @FXML
    private TableColumn<ResponsePatientBean, Integer> col4;
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
        navigator.navigate(PatientDestination.REARRANGE);
    }

    public void sendRequest() {
        String message = textRequest.getText();
        if(message.trim().isEmpty()) {
            errLbl.setText("This field is required");
        } else {
            PrescriptionRequestBean requestBean = new PrescriptionRequestBean();
            requestBean.setMessage(message);
            requestBean.setPatient(Session.getSession().getUser());
            try {
                requestPrescription.sendPrescriptionRequest(requestBean);
                textRequest.setText("");
            } catch (PersistentLayerException e) {
                // add visual clue to notify user that an error occurred
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void showResponse() {
        ObservableList<ResponsePatientBean> response = FXCollections.observableArrayList();
        responseTable.setItems(response);
        col1.setCellValueFactory(new PropertyValueFactory<>("message"));
        col2.setCellValueFactory(new PropertyValueFactory<>("kind"));
        col3.setCellValueFactory(new PropertyValueFactory<>("name"));
        col4.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        try {
            Optional<List<ResponsePatientBean>> bean = requestPrescription.getResponse();
            if(bean.isPresent() && !bean.get().isEmpty()) {
                responseTable.setVisible(true);
                response.addAll(bean.get());
            }
        } catch (PersistentLayerException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        errLbl.managedProperty().bind(errLbl.textProperty().isNotEmpty());
        errLbl.visibleProperty().bind(errLbl.managedProperty());
        responseTable.setVisible(false);
    }

}
