package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.AppointmentBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.DateTimeInterval;
import ispw.uniroma2.doctorhouse.navigation.Navigator;
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

public abstract class AskPage<D> implements ViewController {
    private final AskForRearrange controller;
    protected final Navigator<D> navigator;
    @FXML
    private BorderPane view;
    @FXML
    private Button goBack;
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
    private TableColumn<AppointmentBean, String> otherTblCol;

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

    protected AskPage(AskForRearrange controller, Navigator<D> navigator) {
        this.controller = controller;
        this.navigator = navigator;
        beans = FXCollections.observableArrayList();
        slots = FXCollections.observableArrayList();
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
        slotListView.setItems(slots);
        otherTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getDoctor().getEmail()));
        countryTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getCountry()));
        provinceTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getProvince()));
        cityTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getCity()));
        addressTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getOffice().getAddress()));
        dateTblCol.setCellValueFactory(col -> new SimpleStringProperty(dateFmt.format(col.getValue().getDateTime())));
        startTimeTblCol.setCellValueFactory(col -> new SimpleStringProperty(timeFmt.format(col.getValue().getDateTime())));
        endTimeTblCol.setCellValueFactory(col -> new SimpleStringProperty(timeFmt.format(col.getValue().getDateTime().plus(col.getValue().getSpecialty().getDuration()))));
        serviceTblCol.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().getSpecialty().getName()));
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
        slotListView.setOnMouseClicked(this::onMouseClicked);
        try {
            beans.addAll(controller.getIncomingAppointments());
        } catch (PersistentLayerException e) {
            throw new RuntimeException(e);
        }
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
                } catch (InvalidTimeSlot e) {
                    throw new RuntimeException(e);
                } catch (PersistentLayerException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    private void searchFreeSlots(ActionEvent event) {
        BooleanProperty cannotSubmit = new SimpleBooleanProperty(false);
        cannotSubmit.bind(
                fromDateErrorLbl.textProperty().isNotEmpty().or(toDateErrorLbl.textProperty().isNotEmpty())
        );
        AppointmentBean selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        LocalDate date = selected.getDateTime().toLocalDate();
        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();
        if (from == null) {
            fromDateErrorLbl.setText("This field is required");
        } else {
            if (from.isBefore(date)) {
                from = date;
            }
            fromDateErrorLbl.setText("");
        }
        if (to == null) {
            toDateErrorLbl.setText("This field is required");
        } else if (to.isBefore(from)) {
            toDateErrorLbl.setText("This date is not valid, please try with another one");
        } else {
            toDateErrorLbl.setText("");
        }
        if (!cannotSubmit.get()) {
            slots.clear();
            try {
                slots.addAll(controller.getFreeSlots(selected, from, to));
            } catch (PersistentLayerException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
