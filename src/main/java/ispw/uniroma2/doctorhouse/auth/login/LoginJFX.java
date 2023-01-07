package ispw.uniroma2.doctorhouse.auth.login;

import ispw.uniroma2.doctorhouse.Dispatcher;
import ispw.uniroma2.doctorhouse.EndPoint;
import ispw.uniroma2.doctorhouse.InvalidDestination;
import ispw.uniroma2.doctorhouse.auth.beans.EmailBean;
import ispw.uniroma2.doctorhouse.auth.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.auth.exceptions.EmailNotValid;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.auth.registration.RegisterUserJFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Properties;

public class LoginJFX implements EndPoint {
    private final Login login;
    @FXML
    private TextField emailTxtFld;
    @FXML
    private TextField passwordTxtFld;
    @FXML
    private Label errorLbl;
    @FXML
    private Button loginBtn;
    @FXML
    private Button recoverPasswordBtn;
    @FXML
    private Button signUpBtn;

    private final Dispatcher dispatcher;

    public LoginJFX(Dispatcher dispatcher, Login login) {
        this.dispatcher = dispatcher;
        this.login = login;
    }

    @FXML
    private void initialize() {
        errorLbl.managedProperty().bind(errorLbl.textProperty().isNotEmpty());
        errorLbl.visibleProperty().bind(errorLbl.managedProperty());
    }

    @FXML
    private void login() {
        try {
            EmailBean email = new EmailBean();
            email.setEmail(emailTxtFld.getText());
            LoginRequestBean loginRequest = new LoginRequestBean();
            loginRequest.setEmail(email);
            loginRequest.setPassword(passwordTxtFld.getText());
            login.login(loginRequest);
        } catch (EmailNotValid emailNotValid) {
            errorLbl.textProperty().set("Email entered is not valid!");
        } catch (UserNotFound ex) {
            errorLbl.textProperty().set("Wrong email or password!");
        }
    }

    @FXML
    private void signup() {
        try {
            dispatcher.tryForward(RegisterUserJFX.class, null);
        } catch (InvalidDestination e) {
            errorLbl.setText("Cannot go to panel for registration!");
        }
    }

    @Override
    public void onEnter(Properties properties) {

    }
}
