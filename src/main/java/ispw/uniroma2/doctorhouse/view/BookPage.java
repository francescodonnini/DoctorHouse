package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.book.BookAppointment;
import ispw.uniroma2.doctorhouse.model.Session;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.time.LocalDateTime;

public class BookPage implements ViewController {
    @FXML
    private ListView<LocalDateTime> dateList;
    private final ObservableList<LocalDateTime> timeSlots;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private ComboBox<SpecialtyBean> specialtyMenu;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private BorderPane view;
    private final BookAppointment controller;
    private final OfficeBean office;
    private final DoctorBean doctor;
    private final UserBean user;

    public BookPage(BookAppointment controller, OfficeBean office, DoctorBean doctor) {
        this.controller = controller;
        this.office = office;
        this.doctor = doctor;
        user = new UserBean();
        user.setEmail(Session.getSession().getUser().getEmail());
        timeSlots = FXCollections.observableArrayList();
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    private void initialize() {
        dateList.setItems(timeSlots);
        dateList.setOnMouseClicked(this::onTimeSlotClicked);
    }

    private void onTimeSlotClicked(MouseEvent mouseEvent) {
        LocalDateTime dateTime = dateList.getSelectionModel().getSelectedItem();
        if (dateTime != null) {
            Dialog<ButtonType> confirmDialog = new Dialog<>();
            final ButtonType confirm = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            final ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmDialog.getDialogPane().getButtonTypes().add(confirm);
            confirmDialog.getDialogPane().getButtonTypes().add(cancel);
            confirmDialog.showAndWait().ifPresent(v -> {
                if (v.getButtonData().equals(confirm.getButtonData())) {
                    AppointmentBean appointmentBean = new AppointmentBean();
                    appointmentBean.setDateTime(dateTime);
                    appointmentBean.setDoctor(doctor);
                    appointmentBean.setOffice(office);
                    appointmentBean.setPatient(user);
                    appointmentBean.setSpecialty(specialtyMenu.getValue());
                    controller.book(appointmentBean);
                }
            });
        }
    }
}
