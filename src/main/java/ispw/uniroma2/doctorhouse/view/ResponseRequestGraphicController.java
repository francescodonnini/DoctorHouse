package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.beans.DrugPrescriptionBean;
import ispw.uniroma2.doctorhouse.beans.ResponseBean;
import ispw.uniroma2.doctorhouse.beans.VisitPrescriptionBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorNavigator;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.List;
import java.util.Optional;

public class ResponseRequestGraphicController implements ViewController {

    @FXML
    private Button rearrangeAppointmentBtn;

    @FXML
    private Button requestPrescriptionBtn;

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

    private final DoctorNavigator navigator;
    private final ResponseRequest responseRequest;

    //Prescription field
    private int requestId;

    public ResponseRequestGraphicController(DoctorNavigator navigator, ResponseRequest responseRequest) {
        this.navigator = navigator;
        this.responseRequest = responseRequest;
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    void rearrange() {
        //
    }

    @FXML
    void request() {
        //
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
                label.setText("Please insert the id of prescription you want reply to");
                label.setVisible(true);
                acceptBtn.setVisible(true);
                rejectBtn.setVisible(true);
                idTxtFld.setVisible(true);
            }
        } catch (PersistentLayerException e) {
            // TODO: add visual clue that alerts the user an error occurred
            throw new RuntimeException(e);
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
        } else {
            errLbl.setVisible(true);
        }
    }

    @FXML
    public void reject() {
        //
    }

    @FXML
    public void prescription() {
        String name = nameFld.getText();
        if(idTxtFld.getText().isEmpty()) { //idTextFld now get the value of quantity
            ResponseBean responseBean = new ResponseBean();
            responseBean.setRequestId(requestId);
            VisitPrescriptionBean visitPrescriptionBean = new VisitPrescriptionBean();
            visitPrescriptionBean.setName(name);
            responseBean.setBean(visitPrescriptionBean);
            responseRequest.insertResponse(responseBean);
        } else {
            int quantity = Integer.parseInt(idTxtFld.getText());
            ResponseBean responseBean = new ResponseBean();
            responseBean.setRequestId(requestId);
            DrugPrescriptionBean drugPrescriptionBean = new DrugPrescriptionBean();
            drugPrescriptionBean.setName(name);
            drugPrescriptionBean.setQuantity(quantity);
            responseBean.setBean(drugPrescriptionBean);
            responseRequest.insertResponse(responseBean);
        }
        initialize();
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
    }
}

