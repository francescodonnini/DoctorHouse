package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.auth.LoginFactory;
import ispw.uniroma2.doctorhouse.auth.LoginFactoryImpl;
import ispw.uniroma2.doctorhouse.auth.RegisterUserFactory;
import ispw.uniroma2.doctorhouse.auth.RegisterUserFactoryImpl;
import ispw.uniroma2.doctorhouse.dao.*;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDaoFactory;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentFileFactory;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDaoFactory;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDaoFactory;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionFileFactory;
import ispw.uniroma2.doctorhouse.dao.requests.RequestDaoFactory;
import ispw.uniroma2.doctorhouse.dao.requests.RequestDaoFactoryImpl;
import ispw.uniroma2.doctorhouse.dao.requests.RequestFileFactory;
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
import ispw.uniroma2.doctorhouse.dao.users.UserFileFactory;
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
import java.sql.Connection;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, PersistentLayerException {
        // creating all the factories needed to create DAOs
        SpecialtyDaoFactory specialtyDaoFactory = new SpecialtyDatabaseFactory(ConnectionFactory.getConnection());
        SpecialtyDao specialtyDao = specialtyDaoFactory.create();

        ShiftDaoFactory shiftDaoFactory = new ShiftDatabaseFactory(ConnectionFactory.getConnection());
        ShiftDao shiftDao = shiftDaoFactory.create();

        OfficeDaoFactory officeDaoFactory = new OfficeDatabaseFactory(ConnectionFactory.getConnection(), specialtyDao, shiftDao);
        OfficeDao officeDao = officeDaoFactory.create();

        UserDaoFactory userDaoFactory;
        if (System.currentTimeMillis() % 2 == 0) {
            userDaoFactory = new UserDatabaseFactoryImpl(officeDao, ConnectionFactory.getConnection());
        } else {
            userDaoFactory = new UserFileFactory(officeDao);
        }
        UserDao userDao = userDaoFactory.create();
        AppointmentDaoFactory appointmentDaoFactory;
        if (System.currentTimeMillis() % 2 == 0) {
            appointmentDaoFactory = new AppointmentDatabaseFactory(ConnectionFactory.getConnection(), officeDao, specialtyDao, userDao);
        } else {
            appointmentDaoFactory = new AppointmentFileFactory(officeDao, specialtyDao, userDao);
        }
        AppointmentDao appointmentDao = appointmentDaoFactory.create();

        PrescriptionDaoFactory prescriptionDaoFactory;
        RequestDaoFactory requestDaoFactory;
        ResponseDaoFactory responseDaoFactory;

      if(System.currentTimeMillis() % 2 == 0) {
            requestDaoFactory = new RequestDaoFactoryImpl(ConnectionFactory.getConnection());
            prescriptionDaoFactory = new PrescriptionDatabaseFactory(ConnectionFactory.getConnection());
            responseDaoFactory = new ResponseDaoFactoryImpl(ConnectionFactory.getConnection(), prescriptionDaoFactory.create());
       } else {
            requestDaoFactory = new RequestFileFactory();
            prescriptionDaoFactory = new PrescriptionFileFactory();
            responseDaoFactory = new ResponseFileFactory(prescriptionDaoFactory.create());

       }

        SlotDaoFactory slotDaoFactory = new SlotDatabaseFactory(ConnectionFactory.getConnection(), appointmentDao);
        SlotDao slotDao = slotDaoFactory.create();
        FXMLLoader loader = new FXMLLoader();
        Scene scene;
        NavigatorController navigatorController = null;
        PatientApplicationControllersFactory patientApplicationControllersFactory = new PatientApplicationControllersFactoryImpl(appointmentDao, requestDaoFactory.create(), responseDaoFactory.create(), officeDao, slotDao);
        LoginFactory loginControllerFactory = new LoginFactoryImpl(userDao);
        RegisterUserFactory registerUserFactory = new RegisterUserFactoryImpl(userDao);
        DoctorApplicationControllersFactory doctorApplicationControllersFactory = new DoctorApplicationControllerFactoryImpl(appointmentDao, slotDao, officeDao, responseDaoFactory.create(), requestDaoFactory.create());
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

    public static void main(String[] args) {
        launch(args);
    }
}