package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.model.ClockInterval;
import ispw.uniroma2.doctorhouse.model.TimeInterval;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AskTimeDialog extends Dialog<LocalTime> {
    @FXML
    private Slider intervalSlider;
    @FXML
    private Label allowedRangeLbl;
    @FXML
    private TextField startTimeTxtFld;
    private final TimeInterval allowedRange;
    @FXML
    private Label timeIntervalLbl;
    private final Duration specialtyDuration;
    private final ObjectProperty<LocalTime> selectedTime;

    public AskTimeDialog(TimeInterval interval, Duration specialtyDuration) {
        this.specialtyDuration = specialtyDuration;
        allowedRange = new ClockInterval(interval.getStart(), interval.getEnd().minus(specialtyDuration));
        selectedTime = new SimpleObjectProperty<>();
    }

    @FXML
    private void initialize() {
        ButtonType cancel = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType confirm = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(cancel, confirm);
        getDialogPane().lookupButton(confirm).setDisable(true);
        allowedRangeLbl.setText(String.format("%s and %s", allowedRange.getStart(), allowedRange.getEnd()));
        selectedTime.addListener((observable, oldVal, newVal) -> update(newVal));
        intervalSlider.setMin(allowedRange.getStart().toSecondOfDay());
        intervalSlider.setMax(allowedRange.getEnd().toSecondOfDay());
        intervalSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
            long val = newVal.longValue();
            if (val % 60 != 0) {
                val = (val / 60) * 60;
            }
            getDialogPane().lookupButton(confirm).setDisable(false);
            update(val);
        });
        update(Math.round(intervalSlider.getValue()));
        setResultConverter(c -> {
            if (!c.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                return null;
            }
            setResult(selectedTime.get());
            return getResult();
        });
    }

    private void update(long seconds) {
        selectedTime.set(LocalTime.ofSecondOfDay(seconds));
    }

    private void update(LocalTime from) {
        LocalTime to = from.plus(specialtyDuration);
        timeIntervalLbl.setText(String.format("%s - %s", from.format(DateTimeFormatter.ISO_LOCAL_TIME), to.format(DateTimeFormatter.ISO_LOCAL_TIME)));
    }
}
