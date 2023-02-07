package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Optional;

public class ResponseRequestGraphicController implements ViewController {
    @FXML
    private Button showBtn;

    @FXML
    private TableView<DoctorRequestBean> table;

    @FXML
    private BorderPane view;

    @FXML
    private TableColumn<DoctorRequestBean, Integer> col1;

    @FXML
    private TableColumn<DoctorRequestBean, String> col2;

    @FXML
    private TableColumn<DoctorRequestBean, String> col3;

    @FXML
    private Button acceptBtn;

    @FXML
    private Button rejectBtn;

    @FXML
    private TextField idTxtFld;

    @FXML
    private TextField nameFld;

    @FXML
    private Label quantityLbl;

    @FXML
    private Label label;
    @FXML
    private Button prescriptionButton;
    @FXML
    private Label errLbl;
    @FXML
    private Label msgPatient;
    @FXML
    private TextField msgFld;
    @FXML
    private Label secErrLbl;
    @FXML
    private Label thirdErrLbl;
    @FXML
    private Button sendReject;

    private final ResponseRequest responseRequest;
    private int requestId;

    public ResponseRequestGraphicController(ResponseRequest responseRequest) {
        this.responseRequest = responseRequest;
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    public void  showRequest() {
        initialize();
        ObservableList<DoctorRequestBean> request = FXCollections.observableArrayList();
        table.setItems(request);
        col1.setCellValueFactory(new PropertyValueFactory<>("Id"));
        col2.setCellValueFactory(new PropertyValueFactory<>("patient"));
        col3.setCellValueFactory(new PropertyValueFactory<>("message"));
        try {
            Optional<List<DoctorRequestBean>> requestBean = responseRequest.getRequest();
            if (requestBean.isPresent() && !requestBean.get().isEmpty()) {
                request.addAll(requestBean.orElseThrow());
                label.setText("Please insert the id of request you want reply to");
                label.setVisible(true);
                acceptBtn.setVisible(true);
                rejectBtn.setVisible(true);
                idTxtFld.setVisible(true);
            }
        } catch (PersistentLayerException e) {
            thirdErrLbl.setText("Persistent layer exception");
        }
    }

    @FXML
    public void accept() {
        if(!idTxtFld.getText().trim().isEmpty()) {
            errLbl.setVisible(false);
            requestId = Integer.parseInt(idTxtFld.getText());
            idTxtFld.setText("");
            acceptBtn.setVisible(false);
            rejectBtn.setVisible(false);
            quantityLbl.setVisible(true);
            prescriptionButton.setVisible(true);
            nameFld.setVisible(true);
            showBtn.setText("Choose another prescription");
            label.setText("Drug's or visit name");
            quantityLbl.setText("Quantity");
            msgFld.setVisible(true);
            msgPatient.setVisible(true);
        } else {
            errLbl.setVisible(true);
        }
        idTxtFld.setText("");
    }

    @FXML
    public void reject() {
        if(!idTxtFld.getText().trim().isEmpty()) {
            errLbl.setVisible(false);
            requestId = Integer.parseInt(idTxtFld.getText());
            idTxtFld.setText("");
            acceptBtn.setVisible(false);
            rejectBtn.setVisible(false);
            showBtn.setText("Choose another request");
            label.setVisible(false);
            idTxtFld.setVisible(false);
            msgFld.setVisible(true);
            msgPatient.setVisible(true);
            sendReject.setVisible(true);
        } else {
            errLbl.setVisible(true);
        }
        idTxtFld.setText("");
    }

    @FXML
    public void sendReject() throws PersistentLayerException {
        ResponseBean bean = new ResponseBean();
        bean.setRequestId(requestId);
        if(msgFld.getText().trim().isEmpty()) {
            msgPatient.setText("This field is required");
            msgPatient.setTextFill(Paint.valueOf("Red"));
        } else {
            msgPatient.setText("Specify a message for the patient");
            msgPatient.setTextFill(Paint.valueOf("Black"));
            bean.setMessage(msgFld.getText());
            responseRequest.insertRejection(bean);
        }
        msgFld.setText("");
        initialize();
        showRequest();
    }

    @FXML
    public void send() throws PersistentLayerException {
        String name = nameFld.getText();
        String message = msgFld.getText();
        if(name.isEmpty()) {
            secErrLbl.setVisible(true);
            secErrLbl.setText("This field is required");
        } else if(idTxtFld.getText().isEmpty()) { //idTextFld now get the value of quantity
            ResponseBean responseBean = new ResponseBean();
            responseBean.setRequestId(requestId);
            responseBean.setMessage(message);
            VisitPrescriptionBean visitPrescriptionBean = new VisitPrescriptionBean();
            visitPrescriptionBean.setName(name);
            responseRequest.insertVisitPrescriptionResponse(responseBean, visitPrescriptionBean);
        } else {
            int quantity = Integer.parseInt(idTxtFld.getText());
            ResponseBean responseBean = new ResponseBean();
            responseBean.setRequestId(requestId);
            responseBean.setMessage(message);
            DrugPrescriptionBean drugPrescriptionBean = new DrugPrescriptionBean();
            drugPrescriptionBean.setName(name);
            drugPrescriptionBean.setQuantity(quantity);
            responseRequest.insertDrugPrescriptionResponse(responseBean, drugPrescriptionBean);
        }
        initialize();
        msgFld.setText("");
        showRequest();
    }


    @FXML
    public void initialize() {
        showBtn.setText("Show prescription request");
        label.setVisible(false);
        acceptBtn.setVisible(false);
        rejectBtn.setVisible(false);
        idTxtFld.setVisible(false);
        quantityLbl.setVisible(false);
        prescriptionButton.setVisible(false);
        nameFld.setVisible(false);
        errLbl.setVisible(false);
        msgFld.setVisible(false);
        msgPatient.setVisible(false);
        secErrLbl.setVisible(false);
        thirdErrLbl.setVisible(false);
        sendReject.setVisible(false);
    }
}

