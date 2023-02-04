package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.DoctorApplicationControllersFactory;
import ispw.uniroma2.doctorhouse.PatientApplicationControllersFactory;
import ispw.uniroma2.doctorhouse.auth.LoginFactory;
import ispw.uniroma2.doctorhouse.auth.RegisterUserFactory;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.login.LoginControllerFactory;
import ispw.uniroma2.doctorhouse.navigation.login.LoginNavigator;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class LoginControllerFactoryImpl implements LoginControllerFactory {
    private LoginNavigator loginNavigator;
    private final LoginFactory loginFactory;
    private final RegisterUserFactory registerUserFactory;
    private final PatientApplicationControllersFactory patientFactory;

    private final DoctorApplicationControllersFactory doctorApplicationControllersFactory;

    public LoginControllerFactoryImpl(LoginFactory loginFactory, RegisterUserFactory registerUserFactory, PatientApplicationControllersFactory patientFactory, DoctorApplicationControllersFactory doctorApplicationControllersFactory) {
        this.loginFactory = loginFactory;
        this.registerUserFactory = registerUserFactory;
        this.patientFactory = patientFactory;
        this.doctorApplicationControllersFactory = doctorApplicationControllersFactory;
    }

    public void setLoginNavigator(LoginNavigator loginNavigator) {
        this.loginNavigator = loginNavigator;
    }

    @Override
    public ViewController createRegisterViewController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("register-user-page.fxml"));
        loader.setControllerFactory(f -> new RegisterUserPage(loginNavigator, registerUserFactory.create()));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createIrrecoverableErrorController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("irrecoverable-error-page.fxml"));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createLoginController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login-user-page.fxml"));
        loader.setControllerFactory(f -> new LoginUserPage(loginNavigator, loginFactory.create()));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createPatientHomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("patient-home-page.fxml"));
        HomePage homePage = new HomePage(createRearrangeAppointmentPage(), createDoRearrangeAppointmentPage(), createRequestPrescriptionPage());
        loader.setControllerFactory(f -> homePage);
        loader.load();
        return loader.getController();
    }

    private ViewController createRearrangeAppointmentPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ask-page.fxml"));
        loader.setControllerFactory(f -> new AskPage(patientFactory.createAskForRearrange()));
        loader.load();
        return loader.getController();
    }

    private ViewController createRequestPrescriptionPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("patient-request-page.fxml"));
        loader.setControllerFactory(f -> new PatientRequestPrescriptionGraphicController(patientFactory.createRequestPrescription()));
        loader.load();
        return loader.getController();
    }

    private ViewController createDoRearrangeAppointmentPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("do-rearrange-page.fxml"));
        loader.setControllerFactory(f -> new DoRearrangePage(patientFactory.createDoRearrange()));
        loader.load();
        return loader.getController();
    }

    private ViewController createResponsePage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("doctor_response_page.fxml"));
        loader.setControllerFactory(f-> new ResponseRequestGraphicController(doctorApplicationControllersFactory.createResponseRequest()));
        loader.load();
        return loader.getController();
    }


    @Override
    public ViewController createDoctorHomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("doctor-home-page.fxml"));
        DoctorHomePage homePage = new DoctorHomePage(createRearrangeAppointmentPage(), createDoRearrangeAppointmentPage(), createRequestPrescriptionPage(), createResponsePage());
        loader.setControllerFactory(f -> homePage);
        loader.load();
        return loader.getController();
    }
}
