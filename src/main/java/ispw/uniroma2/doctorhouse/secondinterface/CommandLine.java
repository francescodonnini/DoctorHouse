package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.dao.exceptions.NotValidRequest;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;


public class CommandLine implements CommandLineInterface{
    @FXML
    private TextField command;
    @FXML
    private Label msg;
    @FXML
    private Label response;
    private final StringProperty responseProperty;

    private State state;

    public CommandLine(State state) {
        this.state = state;
        responseProperty = new SimpleStringProperty();
        state.onEnter(this);
    }

    public void setState(State state) {
        this.state = state;
        state.onEnter(this);
    }

    public void setResponse(String result) {
        responseProperty.set(result);
    }

    public void quit() {
        Platform.exit();
    }
    @FXML
    public void enterCommand() {
        command.setOnKeyPressed( event -> {
            try {
                if(event.getCode() == KeyCode.ENTER) {
                    if(!command.getText().isEmpty()) {
                        state.enter(this, command.getText());
                        command.clear();
                    }
                } else {
                    msg.setText("When you finish to type the command click enter");
                }
            } catch (PersistentLayerException ignored) {
                msg.setText("error");
            } catch (NotValidRequest e) {
                msg.setText("Insert a valid request id - enter show request for see the request");
            }
        });
    }

    @FXML
    private void initialize() {
        response.textProperty().bind(responseProperty);
    }
}
