package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;


public class CommandLine {
    @FXML
    private TextField command;
    @FXML
    private Label msg;
    @FXML
    private Label response;

    private State state;

    public CommandLine(State state) {
        this.state = state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setResponse(String result) {
        response.setText(result);
    }

    @FXML
    public void enterCommand() {
        command.setOnKeyPressed( event -> {
            if(event.getCode() == KeyCode.ENTER) {
                if(!command.getText().isEmpty()) {
                    try {
                        state.enter(this, command.getText());
                    } catch (UserNotFound u) {
                        msg.setText("User not found");
                    } catch (PersistentLayerException p) {
                        msg.setText("Persistent layer exception");
                    }
                } else {
                    msg.setTextFill(Paint.valueOf("RED"));
                    msg.setText("This field is required");
                }
                command.setText("");
            } else {
                msg.setText("When you finish to type the command click enter");
            }
        });
    }
}
