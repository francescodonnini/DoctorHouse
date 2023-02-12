package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.PendingAppointmentBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrange;
import ispw.uniroma2.doctorhouse.rearrange.What;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.time.format.DateTimeFormatter;

public class DoRearrangePage implements ViewController {
    private final DoRearrange controller;
    @FXML
    private BorderPane view;
    @FXML
    private TableColumn<PendingAppointmentBean, String> addressTblCol;

    @FXML
    private TableColumn<PendingAppointmentBean, String> cityTblCol;

    @FXML
    private TableColumn<PendingAppointmentBean, String> countryTblCol;

    @FXML
    private TableColumn<PendingAppointmentBean, String> dateTblCol;

    @FXML
    private TableColumn<PendingAppointmentBean, String> endTimeTblCol;

    @FXML
    private TableColumn<PendingAppointmentBean, String> doctorTblCol;

    @FXML
    private TableColumn<PendingAppointmentBean, String> patientTblCol;

    @FXML
    private TableColumn<PendingAppointmentBean, String> provinceTblCol;

    @FXML
    private TableColumn<PendingAppointmentBean, String> serviceTblCol;

    @FXML
    private TableColumn<PendingAppointmentBean, String> startTimeTblCol;

    @FXML
    private TableView<PendingAppointmentBean> table;
    private final ObservableList<PendingAppointmentBean> beans;
    private final DateTimeFormatter dateFmt;
    private final DateTimeFormatter timeFmt;

    @FXML
    private Label errorLbl;

    protected DoRearrangePage(DoRearrange controller) {
        this.controller = controller;
        beans = FXCollections.observableArrayList();
        dateFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        timeFmt = DateTimeFormatter.ofPattern("hh:mm");
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    private void initialize() {
        initTable();
        errorLbl.managedProperty().bind(errorLbl.textProperty().isNotEmpty());
        errorLbl.visibleProperty().bind(errorLbl.managedProperty());
        try {
            beans.addAll(controller.getPendingAppointments());
            clearError();
        } catch (PersistentLayerException e) {
            error("An error occurred while trying to recover your pending appointments. Please try again later or contact support if the problem persists.");
        }
        table.setOnMousePressed(e -> {
            PendingAppointmentBean selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                onTableRowClicked(selected);
            }
        });
    }

    private void initTable() {
        table.setItems(beans);
        doctorTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getDoctor().getEmail()));
        patientTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getPatient().getEmail()));
        countryTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getCountry()));
        provinceTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getProvince()));
        cityTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getCity()));
        addressTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getAddress()));
        dateTblCol.setCellValueFactory(col -> new SimpleStringProperty(dateFmt.format(col.getValue().getDateTime())));
        startTimeTblCol.setCellValueFactory(col -> new SimpleStringProperty(timeFmt.format(col.getValue().getDateTime())));
        endTimeTblCol.setCellValueFactory(col -> new SimpleStringProperty(timeFmt.format(col.getValue().getDateTime().plus(col.getValue().getSpecialty().getDuration()))));
        serviceTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getSpecialty().getName()));
    }

    private void onTableRowClicked(PendingAppointmentBean bean) {
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType reject = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType confirm = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Rearrange Appointment");
        dialog.setContentText("Do you want to accept the request?");
        dialog.getDialogPane().getButtonTypes().addAll(cancel, reject, confirm);
        dialog.showAndWait().ifPresent(a -> {
            try {
                if (a.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                    controller.submit(bean, What.CONFIRM);
                } else if (a.getButtonData().equals(ButtonBar.ButtonData.NO)) {
                    controller.submit(bean, What.CANCEL);
                } else {
                    return;
                }
                beans.remove(bean);
                clearError();
            } catch (InvalidTimeSlot e) {
                error("This time slot is not available anymore.");
            } catch (PersistentLayerException e) {
                error("An error occurred while trying to process your decision on the request of rearrangement. Please try again later or contact support if the problem persists.");
            }
        });
    }

    private void error(String errorMsg) {
        errorLbl.setText(errorMsg);
    }

    private void clearError() {
        errorLbl.setText("");
    }
}
