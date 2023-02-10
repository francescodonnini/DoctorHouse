package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.auth.LoginFactory;
import ispw.uniroma2.doctorhouse.auth.LoginFactoryImpl;
import ispw.uniroma2.doctorhouse.auth.RegisterUserFactory;
import ispw.uniroma2.doctorhouse.auth.RegisterUserFactoryImpl;
import ispw.uniroma2.doctorhouse.dao.ConnectionFactory;
import ispw.uniroma2.doctorhouse.dao.appointment.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDaoFactory;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDao;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDaoFactory;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionFileFactory;
import ispw.uniroma2.doctorhouse.dao.requests.RequestDao;
import ispw.uniroma2.doctorhouse.dao.requests.RequestDaoFactory;
import ispw.uniroma2.doctorhouse.dao.requests.RequestDaoFactoryImpl;
import ispw.uniroma2.doctorhouse.dao.requests.RequestFileFactory;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDao;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDaoFactory;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDaoFactoryImpl;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseFileFactory;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDao;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDaoFactory;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.slot.SlotDao;
import ispw.uniroma2.doctorhouse.dao.slot.SlotDaoFactory;
import ispw.uniroma2.doctorhouse.dao.slot.SlotDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDaoFactory;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.users.UserDao;
import ispw.uniroma2.doctorhouse.dao.users.UserDaoFactory;
import ispw.uniroma2.doctorhouse.dao.users.UserDatabaseFactoryImpl;
import ispw.uniroma2.doctorhouse.navigation.NavigatorController;
import ispw.uniroma2.doctorhouse.navigation.login.LoginDestination;
import ispw.uniroma2.doctorhouse.navigation.login.LoginNavigator;
import ispw.uniroma2.doctorhouse.secondinterface.CommandLine;
import ispw.uniroma2.doctorhouse.secondinterface.StateFactory;
import ispw.uniroma2.doctorhouse.view.LoginControllerFactoryImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;

public class Main extends Application {
    public static final String APP_DIR_PATH = System.getProperty("user.home") + "/.DoctorHouse";

    private AppointmentDao appointmentDao;
    private OfficeDao officeDao;
    private RequestDao requestDao;
    private ResponseDao responseDao;
    private SlotDao slotDao;
    private UserDao userDao;

    @Override
    public void start(Stage stage) throws IOException, PersistentLayerException {
        initConfigurationDirectory();
        initDAOs();
        FXMLLoader loader = new FXMLLoader();
        Scene scene;
        NavigatorController navigatorController = null;
        PatientApplicationControllersFactory patientApplicationControllersFactory = new PatientApplicationControllersFactoryImpl(appointmentDao, requestDao, responseDao, officeDao, slotDao);
        LoginFactory loginControllerFactory = new LoginFactoryImpl(userDao);
        RegisterUserFactory registerUserFactory = new RegisterUserFactoryImpl(userDao);
        DoctorApplicationControllersFactory doctorApplicationControllersFactory = new DoctorApplicationControllerFactoryImpl(appointmentDao, slotDao, officeDao, responseDao, requestDao);
        LoginControllerFactoryImpl loginFactory = new LoginControllerFactoryImpl(loginControllerFactory, registerUserFactory, patientApplicationControllersFactory, doctorApplicationControllersFactory);
       if(System.currentTimeMillis() % 2 == 0) {
            loader.setLocation(getClass().getResource("view/navigator.fxml"));
            scene = new Scene(loader.load());
            navigatorController = loader.getController();
            LoginNavigator loginNavigator = new LoginNavigator(navigatorController, loginFactory);
            loginFactory.setLoginNavigator(loginNavigator);
            loginNavigator.navigate(LoginDestination.LOGIN);
       } else {

            loader.setLocation(getClass().getResource("view/command_line.fxml"));
            StateFactory factory = new StateFactory(loginControllerFactory, patientApplicationControllersFactory, doctorApplicationControllersFactory);
            loader.setControllerFactory(f -> new CommandLine(factory.createLoginState()));
            scene = new Scene(loader.load());
       }

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Connection connection = ConnectionFactory.getConnection();
        connection.close();
    }

    private void initConfigurationDirectory() throws IOException {
        Path path = Path.of(APP_DIR_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private void initDAOs() throws PersistentLayerException {
        SpecialtyDaoFactory specialtyDaoFactory = new SpecialtyDatabaseFactory(ConnectionFactory.getConnection());
        SpecialtyDao specialtyDao = specialtyDaoFactory.create();
        ShiftDaoFactory shiftDaoFactory = new ShiftDatabaseFactory(ConnectionFactory.getConnection());
        ShiftDao shiftDao = shiftDaoFactory.create();
        OfficeDaoFactory officeDaoFactory = new OfficeDatabaseFactory(ConnectionFactory.getConnection(), specialtyDao, shiftDao);
        officeDao = officeDaoFactory.create();
        UserDaoFactory userDaoFactory = new UserDatabaseFactoryImpl(officeDao, ConnectionFactory.getConnection());
        userDao = userDaoFactory.create();
        AppointmentDaoFactory appointmentDaoFactory;
        if (System.currentTimeMillis() % 2 == 0) {
            appointmentDaoFactory = new AppointmentDatabaseFactory(ConnectionFactory.getConnection(), officeDao, specialtyDao, userDao);
        } else {
            appointmentDaoFactory = new AppointmentFileFactory(officeDao, specialtyDao, userDao);
        }
        appointmentDao = appointmentDaoFactory.create();
        PrescriptionDaoFactory prescriptionDaoFactory;
        RequestDaoFactory requestDaoFactory;
        ResponseDaoFactory responseDaoFactory;
        PrescriptionDao prescriptionDao;
        if(System.currentTimeMillis() % 2 == 0) {
            requestDaoFactory = new RequestDaoFactoryImpl(ConnectionFactory.getConnection());
            requestDao = requestDaoFactory.create();
            prescriptionDaoFactory = new PrescriptionDatabaseFactory(ConnectionFactory.getConnection());
            prescriptionDao = prescriptionDaoFactory.create();
            responseDaoFactory = new ResponseDaoFactoryImpl(ConnectionFactory.getConnection(), prescriptionDao);
            responseDao = responseDaoFactory.create();
            */

       } else {
        prescriptionDaoFactory = new PrescriptionFileFactory();
        prescriptionDao = prescriptionDaoFactory.create();
        responseDaoFactory = new ResponseFileFactory(prescriptionDao);
        responseDao = responseDaoFactory.create();
        requestDaoFactory = new RequestFileFactory(responseDao);
        requestDao = requestDaoFactory.create();

        }
        SlotDaoFactory slotDaoFactory = new SlotDatabaseFactory(ConnectionFactory.getConnection(), appointmentDao);
        slotDao = slotDaoFactory.create();
    }

    public static void main(String[] args) {
        launch(args);
    }
}