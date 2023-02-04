package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.auth.Login;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.login.LoginDestination;
import ispw.uniroma2.doctorhouse.navigation.login.LoginNavigator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginUserPage implements ViewController {
    private final LoginNavigator loginNavigator;
    private final Login login;
    @FXML
    private Parent view;
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

    public LoginUserPage(LoginNavigator loginNavigator, Login login) {
        this.loginNavigator = loginNavigator;
        this.login = login;
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    private void initialize() {
        passwordTxtFld.setOnKeyReleased(this::onEnter);
        errorLbl.managedProperty().bind(errorLbl.textProperty().isNotEmpty());
        errorLbl.visibleProperty().bind(errorLbl.managedProperty());
    }

    @FXML
    private void login() {
        try {
            String email = emailTxtFld.getText().trim();
            String password = passwordTxtFld.getText();
            LoginRequestBean loginRequest = new LoginRequestBean();
            loginRequest.setEmail(email);
            loginRequest.setPassword(password);
            UserBean bean = login.login(loginRequest);
            if (bean instanceof DoctorBean) {
                loginNavigator.navigate(LoginDestination.DOCTOR_HOME_PAGE);
            } else if (bean != null) {
                loginNavigator.navigate(LoginDestination.PATIENT_HOME_PAGE);
            }
        } catch (UserNotFound e) {
            errorLbl.setText("Wrong email or password!");
        } catch (PersistentLayerException e) {
            errorLbl.setText("Something went wrong. If the problem persists call the assistance.");
        }
    }

    @FXML
    private void signup() {
        loginNavigator.navigate(LoginDestination.SIGNUP);
    }

    private void onEnter(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            login();
        }
    }
}
