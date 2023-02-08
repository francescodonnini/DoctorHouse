package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.ResponsePatientBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
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


public abstract class RequestPrescriptionGraphicController implements ViewController {
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
    @FXML
    private Label persError;
    private final RequestPrescription requestPrescription;

    protected RequestPrescriptionGraphicController(RequestPrescription requestPrescription) {
        this.requestPrescription = requestPrescription;
    }

    @Override
    public Parent getView() {
        return view;
    }

    public void sendRequest() {
        String message = textRequest.getText();
        if(message.trim().isEmpty()) {
            errLbl.setText("This field is required");
        } else {
            try {
                requestPrescription.sendPrescriptionRequest(message);
                textRequest.setText("");
            } catch (PersistentLayerException e) {
                persError.setVisible(true);
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
            persError.setVisible(true);
        }
    }

    @FXML
    public void initialize() {
        errLbl.managedProperty().bind(errLbl.textProperty().isNotEmpty());
        errLbl.visibleProperty().bind(errLbl.managedProperty());
        responseTable.setVisible(false);
        persError.setVisible(false);
    }

}
