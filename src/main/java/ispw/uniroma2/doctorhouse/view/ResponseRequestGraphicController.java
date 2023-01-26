package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
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
    private Label label;

    private final DoctorNavigator navigator;
    private final ResponseRequest responseRequest;


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
        ObservableList<DoctorRequestBean> request = FXCollections.observableArrayList();
        table.setItems(request);
        col1.setCellValueFactory(new PropertyValueFactory<>("Id"));
        col2.setCellValueFactory(new PropertyValueFactory<>("patient"));
        col3.setCellValueFactory(new PropertyValueFactory<>("message"));
        Optional<List<DoctorRequestBean>> requestBean = responseRequest.getRequest();
        if(requestBean.isPresent() && !requestBean.get().isEmpty()) {
            request.addAll(requestBean.orElseThrow());
            label.setText("Please insert the id of prescription you want reply to");
        }
    }

    @FXML
    public void accept() {
        //
    }

    @FXML
    public void reject() {
        //
    }

    @FXML
    public void initialize() {
        label.managedProperty().bind(label.textProperty().isNotEmpty());
        label.visibleProperty().bind(label.managedProperty());
        acceptBtn.managedProperty().bind(label.textProperty().isNotEmpty());
        acceptBtn.visibleProperty().bind(label.managedProperty());
        rejectBtn.managedProperty().bind(label.textProperty().isNotEmpty());
        rejectBtn.managedProperty().bind(label.managedProperty());
        idTxtFld.managedProperty().bind(label.textProperty().isNotEmpty());
        idTxtFld.visibleProperty().bind(label.managedProperty());
    }
}
