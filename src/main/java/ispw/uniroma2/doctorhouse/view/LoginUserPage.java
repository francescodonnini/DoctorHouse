package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.auth.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.auth.Login;
import ispw.uniroma2.doctorhouse.model.Session;
import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.doctor.Doctor;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.login.LoginDestination;
import ispw.uniroma2.doctorhouse.navigation.login.LoginNavigator;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientDestination;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginUserPage implements ViewController {
    private final LoginNavigator loginNavigator;
    private final PatientNavigator patientNavigator;
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

    public LoginUserPage(LoginNavigator loginNavigator, PatientNavigator patientNavigator, Login login) {
        this.loginNavigator = loginNavigator;
        this.patientNavigator = patientNavigator;
        this.login = login;
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    private void initialize() {
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
            login.login(loginRequest);
            User user = Session.getSession().getUser();
            if (user instanceof Doctor) {
                throw new UnsupportedOperationException();
            } else if (user != null) {
                patientNavigator.navigate(PatientDestination.HOME_PAGE);
            }
        } catch (UserNotFound ex) {
            errorLbl.textProperty().set("Wrong email or password!");
        }
    }

    @FXML
    private void signup() {
        loginNavigator.navigate(LoginDestination.SIGNUP);
    }
}
