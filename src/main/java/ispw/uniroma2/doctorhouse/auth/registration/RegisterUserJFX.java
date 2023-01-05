package ispw.uniroma2.doctorhouse.auth.registration;

import ispw.uniroma2.doctorhouse.auth.beans.EmailBean;
import ispw.uniroma2.doctorhouse.auth.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.auth.exceptions.EmailNotValid;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

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
    private ChoiceBox<Integer> genderPicker;

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
        genderPicker.getItems().addAll(0, 1, 2);
        genderPicker.setValue(0);
        genderPicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(Integer i) {
                switch (i) {
                    case 0:
                        return "Not specified";
                    case 1:
                        return "Male";
                    case 2:
                        return "Female";
                    default:
                        return "";
                }
            }

            @Override
            public Integer fromString(String i) {
                switch (i) {
                    case "Not specified":
                        return 0;
                    case "Male":
                        return 1;
                    case "Female":
                        return 2;
                    default:
                        return 9;
                }
            }
        });
        nameRequiredLbl.managedProperty().bind(nameRequiredLbl.textProperty().isNotEmpty());
        nameRequiredLbl.visibleProperty().bind(nameRequiredLbl.managedProperty());
        emailErrorLbl.managedProperty().bind(emailErrorLbl.textProperty().isNotEmpty());
        emailErrorLbl.visibleProperty().bind(emailErrorLbl.managedProperty());
    }

    @FXML
    void register(ActionEvent event) {
        boolean cannotSubmit = false;
        String name = nameTxtFld.getText();
        if (name.trim().isEmpty()) {
            nameRequiredLbl.textProperty().set("This field is required!");
            cannotSubmit = true;
        } else {
            nameRequiredLbl.textProperty().set("");
        }
        String email = emailTxtFld.getText();
        if (email.trim().isEmpty()) {
            emailErrorLbl.textProperty().set("This field is required");
            cannotSubmit = true;
        } else {
            emailErrorLbl.textProperty().set("");
        }
        EmailBean emailBean = new EmailBean();
        try {
            emailBean.setEmail(email);
        } catch (EmailNotValid e) {
            emailErrorLbl.textProperty().set("Invalid email!");
            cannotSubmit = true;
        }
        if (!cannotSubmit) {
            UserRegistrationRequestBean request = new UserRegistrationRequestBean();
            request.setFirstName(name);
            request.setEmail(emailBean);
            register.register(request);
        }
    }
}
