package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.auth.RegisterUser;
import ispw.uniroma2.doctorhouse.auth.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.auth.beans.GenderBean;
import ispw.uniroma2.doctorhouse.auth.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.login.LoginDestination;
import ispw.uniroma2.doctorhouse.navigation.login.LoginNavigator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.Predicate;

public class RegisterUserPage implements ViewController {
    private static final String FIELD_REQUIRED_MESSAGE = "This field is required!";
    private final RegisterUser register;
    private final LoginNavigator navigator;
    @FXML
    private Parent view;
    @FXML
    private Label birthDateErrorLbl;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private Label confirmPasswordErrorLbl;
    @FXML
    private TextField confirmPasswordTxtFld;
    @FXML
    private Label emailErrorLbl;
    @FXML
    private TextField emailTxtFld;
    @FXML
    private Label fiscalCodeErrorLbl;
    @FXML
    private TextField fiscalCodeTxtFld;
    @FXML
    private ChoiceBox<GenderBean> genderPicker;
    @FXML
    private Label lastNameRequiredLbl;
    @FXML
    private TextField lastNameTxtFld;
    @FXML
    private Label nameRequiredLbl;
    @FXML
    private TextField nameTxtFld;
    @FXML
    private Label passwordErrorLbl;
    @FXML
    private TextField passwordTxtFld;
    @FXML
    private ComboBox<DoctorBean> familyDoctorBox;
    @FXML
    private Button signUpBtn;
    @FXML
    private Label signUpErrorLbl;

    public RegisterUserPage(LoginNavigator navigator, RegisterUser register) {
        this.navigator = navigator;
        this.register = register;
    }

    @Override
    public Parent getView() {
        return view;
    }

    @FXML
    private void initialize() {
        genderPicker.getItems().addAll(GenderBean.NOT_KNOWN, GenderBean.MALE, GenderBean.FEMALE, GenderBean.NOT_APPLICABLE);
        genderPicker.setValue(genderPicker.getItems().get(0));
        genderPicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(GenderBean i) {
                switch (i) {
                    case NOT_KNOWN:
                        return "Not specified";
                    case MALE:
                        return "Male";
                    case FEMALE:
                        return "Female";
                    default:
                        return "Not applicable";
                }
            }

            @Override
            public GenderBean fromString(String i) {
                switch (i) {
                    case "Not specified":
                        return GenderBean.NOT_KNOWN;
                    case "Male":
                        return GenderBean.MALE;
                    case "Female":
                        return GenderBean.FEMALE;
                    default:
                        return GenderBean.NOT_APPLICABLE;
                }
            }
        });
        genderPicker.getItems();
        familyDoctorBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(DoctorBean doctorBean) {
                if (doctorBean == null) {
                    return "";
                } else {
                    return doctorBean.getEmail();
                }
            }

            @Override
            public DoctorBean fromString(String s) {
                if (s.isEmpty()) {
                    return null;
                } else {
                    DoctorBean bean = new DoctorBean();
                    bean.setEmail(s);
                    return bean;
                }
            }
        });
        nameRequiredLbl.managedProperty().bind(nameRequiredLbl.textProperty().isNotEmpty());
        nameRequiredLbl.visibleProperty().bind(nameRequiredLbl.managedProperty());
        emailErrorLbl.managedProperty().bind(emailErrorLbl.textProperty().isNotEmpty());
        emailErrorLbl.visibleProperty().bind(emailErrorLbl.managedProperty());
        passwordErrorLbl.managedProperty().bind(passwordErrorLbl.textProperty().isNotEmpty());
        passwordErrorLbl.visibleProperty().bind(passwordErrorLbl.managedProperty());
        lastNameRequiredLbl.managedProperty().bind(lastNameRequiredLbl.textProperty().isNotEmpty());
        lastNameRequiredLbl.visibleProperty().bind(lastNameRequiredLbl.managedProperty());
        fiscalCodeErrorLbl.managedProperty().bind(fiscalCodeErrorLbl.textProperty().isNotEmpty());
        fiscalCodeErrorLbl.visibleProperty().bind(fiscalCodeErrorLbl.managedProperty());
        confirmPasswordErrorLbl.managedProperty().bind(confirmPasswordErrorLbl.textProperty().isNotEmpty());
        confirmPasswordErrorLbl.visibleProperty().bind(confirmPasswordErrorLbl.managedProperty());
        birthDateErrorLbl.managedProperty().bind(birthDateErrorLbl.textProperty().isNotEmpty());
        birthDateErrorLbl.visibleProperty().bind(birthDateErrorLbl.managedProperty());
        signUpErrorLbl.managedProperty().bind(signUpErrorLbl.textProperty().isNotEmpty());
        signUpErrorLbl.visibleProperty().bind(signUpErrorLbl.managedProperty());
    }

    @FXML
    private void login() {
        navigator.navigate(LoginDestination.Login);
    }

    @FXML
    void register() {
        BooleanProperty cannotSubmit = new SimpleBooleanProperty();
        cannotSubmit.bind(nameRequiredLbl.textProperty().isNotEmpty()
                .or(passwordErrorLbl.textProperty().isNotEmpty())
                .or(lastNameRequiredLbl.textProperty().isNotEmpty())
                .or(emailErrorLbl.textProperty().isNotEmpty())
                .or(fiscalCodeErrorLbl.textProperty().isNotEmpty())
                .or(confirmPasswordErrorLbl.textProperty().isNotEmpty())
                .or(birthDateErrorLbl.textProperty().isNotEmpty()));
        String name = nameTxtFld.getText().trim();
        String password = passwordTxtFld.getText();
        String lastName = lastNameTxtFld.getText().trim();
        String email = emailTxtFld.getText().trim();
        String fiscalCode = fiscalCodeTxtFld.getText().trim();
        String confirmPassword = confirmPasswordTxtFld.getText();
        LocalDate birthDate = birthDatePicker.getValue();
        DoctorBean familyDoctor = familyDoctorBox.getValue();
        check(name, String::isEmpty, nameRequiredLbl, FIELD_REQUIRED_MESSAGE);
        check(lastName, String::isEmpty, lastNameRequiredLbl, FIELD_REQUIRED_MESSAGE);
        check(email, String::isEmpty, emailErrorLbl, FIELD_REQUIRED_MESSAGE);
        check(fiscalCode, String::isEmpty, fiscalCodeErrorLbl, FIELD_REQUIRED_MESSAGE);
        check(password, String::isEmpty, passwordErrorLbl, FIELD_REQUIRED_MESSAGE);
        check(confirmPassword, String::isEmpty, confirmPasswordErrorLbl, FIELD_REQUIRED_MESSAGE);
        if (confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
            confirmPasswordErrorLbl.textProperty().set("Passwords do not match");
        } else {
            confirmPasswordErrorLbl.textProperty().set("");
        }
        if (birthDate != null) {
            Period period = Period.between(birthDate, LocalDate.now().plusDays(1));
            if (period.getYears() < 18) {
                birthDateErrorLbl.textProperty().set("You must be at least 18 years old");
            } else {
                birthDateErrorLbl.textProperty().set("");
            }
        } else {
            birthDateErrorLbl.textProperty().set(FIELD_REQUIRED_MESSAGE);
        }
        if (!cannotSubmit.get()) {
            UserRegistrationRequestBean request = new UserRegistrationRequestBean();
            request.setBirthDate(birthDate);
            request.setFirstName(name);
            request.setEmail(email);
            request.setGender(genderPicker.getValue());
            request.setFiscalCode(fiscalCode);
            request.setLastName(lastName);
            request.setPassword(password);
            request.setFamilyDoctor(familyDoctor);
            try {
                // add visual cue that notify the user that the registration process was successful
                register.register(request);
                navigator.navigate(LoginDestination.Login);
            } catch (DuplicateEmail e) {
                signUpErrorLbl.setText("The email entered already exists! Try another one!");
            } catch (IrrecoverableError e) {
                navigator.navigate(LoginDestination.IrrecoverableError);
            }
        }
    }

    private void check(String s, Predicate<String> predicate, Label errorLbl, String errorMessage) {
        if (predicate.test(s)) {
            errorLbl.setText(errorMessage);
        } else {
            errorLbl.setText("");
        }
    }
}
