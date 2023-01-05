package ispw.uniroma2.doctorhouse.auth.registration;

import ispw.uniroma2.doctorhouse.auth.beans.EmailBean;
import ispw.uniroma2.doctorhouse.auth.beans.GenderBean;
import ispw.uniroma2.doctorhouse.auth.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.auth.exceptions.EmailNotValid;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.Period;

public class RegisterUserJFX extends RegisterUserGraphicController {
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
    private Button signUpBtn;

    public RegisterUserJFX(RegisterUser register) {
        super(register);
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
    }

    @FXML
    void register(ActionEvent event) {
        BooleanProperty cannotSubmit = new SimpleBooleanProperty();
        cannotSubmit.bind(nameRequiredLbl.textProperty().isNotEmpty()
                .and(passwordErrorLbl.textProperty().isNotEmpty())
                .and(lastNameRequiredLbl.textProperty().isNotEmpty())
                .and(emailErrorLbl.textProperty().isNotEmpty())
                .and(fiscalCodeErrorLbl.textProperty().isNotEmpty())
                .and(confirmPasswordErrorLbl.textProperty().isNotEmpty())
                .and(birthDateErrorLbl.textProperty().isNotEmpty())
                );
        //get text fields content
        String name = nameTxtFld.getText();
        String password = passwordTxtFld.getText();
        String lastName = lastNameTxtFld.getText();
        String email = emailTxtFld.getText();
        String fiscalCode = fiscalCodeTxtFld.getText();
        String confirmPassword = confirmPasswordTxtFld.getText();
        LocalDate dataPicker = birthDatePicker.getValue();
        //Check name text field content
        if (name.trim().isEmpty()) {
            nameRequiredLbl.textProperty().set("This field is required!");
        } else {
            nameRequiredLbl.textProperty().set("");
        }
        //Check email text field content
        if (email.trim().isEmpty()) {
            emailErrorLbl.textProperty().set("This field is required");
        } else {
            emailErrorLbl.textProperty().set("");
        }

        EmailBean emailBean = new EmailBean();
        try {
            emailBean.setEmail(email);
            emailErrorLbl.textProperty().set("");

        } catch (EmailNotValid e) {
            emailErrorLbl.textProperty().set("Invalid email!");

        }
        //check password text field content
        if (password.isEmpty()) {
            passwordErrorLbl.textProperty().set("This field is required");
        } else {
            passwordErrorLbl.textProperty().set("");
        }

        //check lastName text field content
        if (lastName.trim().isEmpty()) {
            lastNameRequiredLbl.textProperty().set("This field is required");
        } else {
            lastNameRequiredLbl.textProperty().set("");
        }
        //check fiscalCode text field content
        if (fiscalCode.trim().isEmpty()) {
            fiscalCodeErrorLbl.textProperty().set("This field is required");

        } else {
            fiscalCodeErrorLbl.textProperty().set("");
        }
        //check confirmPassword text field content
        if (confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
            confirmPasswordErrorLbl.textProperty().set("Password do not match");

        } else {
            confirmPasswordErrorLbl.textProperty().set("");
        }
        //check dataPicker content
        if (dataPicker != null) {
            Period period = Period.between(dataPicker, LocalDate.now().plusDays(1));
            if (period.getYears() < 18) {
                birthDateErrorLbl.textProperty().set("You must be at least 18 years old");

            } else {
                birthDateErrorLbl.textProperty().set("");
            }
        } else {
            birthDateErrorLbl.textProperty().set("This field is required");

        }
        if (!cannotSubmit.get()) {
            UserRegistrationRequestBean request = new UserRegistrationRequestBean();
            request.setFirstName(name);
            request.setEmail(emailBean);
            register.register(request);
        }
    }
}
