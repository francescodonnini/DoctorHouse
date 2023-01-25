package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorNavigator;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import ispw.uniroma2.doctorhouse.view.exception.RequestNotFound;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    void request() throws RequestNotFound {
        //
    }

    @FXML
    public void  showRequest() throws RequestNotFound {
        ObservableList<DoctorRequestBean> request = FXCollections.observableArrayList();
        table.setItems(request);
        col1.setCellValueFactory(new PropertyValueFactory<>("Id"));
        col2.setCellValueFactory(new PropertyValueFactory<>("patient"));
        col3.setCellValueFactory(new PropertyValueFactory<>("message"));
        Optional<List<DoctorRequestBean>> requestBean = responseRequest.getRequest();
        request.addAll(requestBean.orElseThrow());

    }
}
