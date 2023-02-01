package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.PendingAppointmentBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrange;
import ispw.uniroma2.doctorhouse.rearrange.What;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.time.format.DateTimeFormatter;

public abstract class DoRearrangePage<D> implements ViewController {
    private final DoRearrange controller;
    protected final Navigator<D> navigator;
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
    private TableColumn<PendingAppointmentBean, String> otherTblCol;

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

    protected DoRearrangePage(DoRearrange controller, Navigator<D> navigator) {
        this.controller = controller;
        this.navigator = navigator;
        beans = FXCollections.observableArrayList();
        dateFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        timeFmt = DateTimeFormatter.ofPattern("hh:mm");
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    protected abstract void goBack(ActionEvent ignored);

    @FXML
    private void initialize() {
        table.setItems(beans);
        otherTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getDoctor().getEmail()));
        countryTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getCountry()));
        provinceTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getProvince()));
        cityTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getCity()));
        addressTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getAddress()));
        dateTblCol.setCellValueFactory(col -> new SimpleStringProperty(dateFmt.format(col.getValue().getDateTime())));
        startTimeTblCol.setCellValueFactory(col -> new SimpleStringProperty(timeFmt.format(col.getValue().getDateTime())));
        endTimeTblCol.setCellValueFactory(col -> new SimpleStringProperty(timeFmt.format(col.getValue().getDateTime().plus(col.getValue().getSpecialty().getDuration()))));
        serviceTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getSpecialty().getName()));
        try {
            beans.addAll(controller.getPendingAppointments());
        } catch (PersistentLayerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onTableRowClicked(MouseEvent ignored) {
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType reject = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType confirm = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Rearrange Appointment");
        dialog.setContentText("Do you want to accept the request?");
        dialog.getDialogPane().getButtonTypes().addAll(cancel, reject, confirm);
        dialog.showAndWait().ifPresent(a -> {
            PendingAppointmentBean bean = table.getSelectionModel().getSelectedItem();
            try {
                if (a.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                    controller.submit(bean, What.CONFIRM);
                } else if (a.getButtonData().equals(ButtonBar.ButtonData.NO)) {
                    controller.submit(bean, What.CANCEL);
                }
                beans.remove(bean);
            } catch (InvalidTimeSlot | PersistentLayerException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
