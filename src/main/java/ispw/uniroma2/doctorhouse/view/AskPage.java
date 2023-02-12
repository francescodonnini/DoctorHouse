package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.AppointmentBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.DateTimeInterval;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrange;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AskPage implements ViewController {
    private final AskForRearrange controller;
    @FXML
    private BorderPane view;
    @FXML
    private TableColumn<AppointmentBean, String> addressTblCol;
    @FXML
    private TableColumn<AppointmentBean, String> cityTblCol;
    @FXML
    private TableColumn<AppointmentBean, String> countryTblCol;
    @FXML
    private TableColumn<AppointmentBean, String> dateTblCol;
    private final DateTimeFormatter dateFmt;
    @FXML
    private TableColumn<AppointmentBean, String> endTimeTblCol;
    private final DateTimeFormatter timeFmt;
    @FXML
    private TableColumn<AppointmentBean, String> doctorTblCol;
    @FXML
    private TableColumn<AppointmentBean, String> patientTblCol;
    @FXML
    private TableColumn<AppointmentBean, String> provinceTblCol;
    @FXML
    private TableColumn<AppointmentBean, String> serviceTblCol;
    @FXML
    private ListView<DateTimeInterval> slotListView;
    private final ObservableList<DateTimeInterval> slots;
    @FXML
    private TableColumn<AppointmentBean, String> startTimeTblCol;
    @FXML
    private TableView<AppointmentBean> table;
    private final ObservableList<AppointmentBean> beans;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private Label fromDateErrorLbl;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private Label toDateErrorLbl;
    @FXML
    private Button searchBtn;
    @FXML
    private Label persistentErrorLbl;

    public AskPage(AskForRearrange controller) {
        this.controller = controller;
        beans = FXCollections.observableArrayList();
        slots = FXCollections.observableArrayList();
        dateFmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        timeFmt = DateTimeFormatter.ofPattern("hh:mm");
    }

    @Override
    public Parent getView() {
        return view;
    }

    @Override
    public void update() {
        beans.clear();
        try {
            beans.addAll(controller.getIncomingAppointments());
        } catch (PersistentLayerException e) {
            persistentErrorLbl.setText("impossible to recover incoming appointments");
        }
    }

    @FXML
    private void initialize() {
        slotListView.setItems(slots);
        initTable();
        table.setOnMouseClicked(this::onClick);
        fromDateErrorLbl.managedProperty().bind(fromDateErrorLbl.textProperty().isNotEmpty());
        fromDateErrorLbl.visibleProperty().bind(fromDateErrorLbl.managedProperty());
        toDateErrorLbl.managedProperty().bind(fromDateErrorLbl.textProperty().isNotEmpty());
        toDateErrorLbl.visibleProperty().bind(toDateErrorLbl.managedProperty());
        fromDatePicker.valueProperty().addListener(((observable, oldVal, newVal) -> {
            if (newVal != null) {
                toDatePicker.setValue(newVal.plusDays(7));
            }
        }));
        persistentErrorLbl.managedProperty().bind(persistentErrorLbl.textProperty().isNotEmpty());
        persistentErrorLbl.visibleProperty().bind(persistentErrorLbl.managedProperty());
        slotListView.setOnMouseClicked(this::onMouseClicked);
        try {
            beans.addAll(controller.getIncomingAppointments());
            persistentErrorLbl.setText("");
        } catch (PersistentLayerException e) {
            persistentErrorLbl.setText("A problem occurred while trying to recover your incoming appointments. Try again later or call support if the problem persists.");
        }
    }

    private void initTable() {
        table.setItems(beans);
        patientTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getPatient().getEmail()));
        doctorTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getDoctor().getEmail()));
        countryTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getCountry()));
        provinceTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getProvince()));
        cityTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getCity()));
        addressTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getAddress()));
        dateTblCol.setCellValueFactory(col -> new SimpleStringProperty(dateFmt.format(col.getValue().getDateTime().toLocalDate())));
        startTimeTblCol.setCellValueFactory(col -> new SimpleStringProperty(timeFmt.format(col.getValue().getDateTime().toLocalTime())));
        endTimeTblCol.setCellValueFactory(col -> new SimpleStringProperty(timeFmt.format(col.getValue().getDateTime().plus(col.getValue().getSpecialty().getDuration()).toLocalTime())));
        serviceTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getSpecialty().getName()));
    }

    private void onMouseClicked(MouseEvent mouseEvent) {
        AppointmentBean selectedAppointment = table.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            return;
        }
        DateTimeInterval selectedInterval = slotListView.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(f -> new AskTimeDialog(selectedInterval.getInterval(), selectedAppointment.getSpecialty().getDuration()));
        loader.setLocation(getClass().getResource("ask-time-dialog.fxml"));
        try {
            Parent root = loader.load();
            AskTimeDialog dialog = loader.getController();
            dialog.setTitle("Choose what time you want to rearrange the appointment");
            dialog.setGraphic(root);
            dialog.showAndWait().ifPresent(a -> {
                try {
                    LocalDate date = selectedInterval.getDateTime().toLocalDate();
                    controller.ask(selectedAppointment, date.atTime(a));
                    beans.remove(selectedAppointment);
                    slots.clear();
                    persistentErrorLbl.setText("");
                } catch (InvalidTimeSlot e) {
                    persistentErrorLbl.setText("The selected time slot is not available anymore, please try another one.");
                    searchFreeSlots(null);
                } catch (PersistentLayerException e) {
                    persistentErrorLbl.setText("An error occurred while trying to rearrange the appointment. please try again later or contact support if the problem persists.");
                }
            });
        } catch (IOException e) {
            persistentErrorLbl.setText("An unexpected error occurred, please contact support.");
        }
    }

    private void onClick(MouseEvent mouseEvent) {
        AppointmentBean selectedAppointment = table.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            LocalDate date = selectedAppointment.getDateTime().toLocalDate();
            LocalDate pickerDate = fromDatePicker.getValue();
            if (pickerDate == null || pickerDate.isBefore(date)) {
                fromDatePicker.setValue(date);
            }
            toDatePicker.setValue(date.plusDays(7));
            fromDatePicker.requestFocus();
        }
    }

    @FXML
    private void searchFreeSlots(ActionEvent ignored) {
        BooleanProperty cannotSubmit = new SimpleBooleanProperty(false);
        cannotSubmit.bind(
                fromDateErrorLbl.textProperty().isNotEmpty().or(toDateErrorLbl.textProperty().isNotEmpty())
        );
        AppointmentBean selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();
        if (from == null) {
            fromDateErrorLbl.setText("This field is required");
        } else {
            if (from.isBefore(LocalDate.now())) {
                fromDateErrorLbl.setText("This date is not valid, please try with another one");
            } else {
                fromDateErrorLbl.setText("");
            }
        }
        if (to == null) {
            toDateErrorLbl.setText("This field is required");
        } else if (to.isBefore(from)) {
            toDateErrorLbl.setText("This date is not valid, please try with another one");
        } else {
            toDateErrorLbl.setText("");
        }
        slots.clear();
        if (!cannotSubmit.get()) {
            try {
                slots.addAll(controller.getFreeSlots(selected, from, to));
                persistentErrorLbl.setText("");
            } catch (PersistentLayerException e) {
                persistentErrorLbl.setText("An error occurred while trying to recover the free time slots on which rearrange the appointment. Try again or contact support if the problem persists.");
            }
        }
    }
}
