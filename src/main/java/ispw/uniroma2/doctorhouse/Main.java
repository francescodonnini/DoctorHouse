package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.auth.LoginFactory;
import ispw.uniroma2.doctorhouse.auth.LoginFactoryImpl;
import ispw.uniroma2.doctorhouse.auth.RegisterUserFactory;
import ispw.uniroma2.doctorhouse.auth.RegisterUserFactoryImpl;
import ispw.uniroma2.doctorhouse.dao.*;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDaoFactory;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDaoFactory;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDatabaseFactory;
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
import ispw.uniroma2.doctorhouse.secondinterface.SecondLoginInterface;
import ispw.uniroma2.doctorhouse.secondinterface.StateFactory;
import ispw.uniroma2.doctorhouse.view.LoginControllerFactoryImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
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

        AppointmentDaoFactory appointmentDaoFactory = new AppointmentDatabaseFactory(ConnectionFactory.getConnection(), officeDao, specialtyDao, userDao);
        AppointmentDao appointmentDao = appointmentDaoFactory.create();

        PrescriptionDaoFactory prescriptionDaoFactory = new PrescriptionDatabaseFactory(ConnectionFactory.getConnection());

        RequestDaoFactory requestDaoFactory = new RequestDaoFactoryImpl(ConnectionFactory.getConnection());
        ResponseDaoFactory responseDaoFactory = new ResponseDaoFactoryImpl(ConnectionFactory.getConnection(), prescriptionDaoFactory.create());

        SlotDaoFactory slotDaoFactory = new SlotDatabaseFactory(ConnectionFactory.getConnection(), appointmentDao);
        SlotDao slotDao = slotDaoFactory.create();
        FXMLLoader loader = new FXMLLoader();
        Scene scene = null;
        NavigatorController navigatorController = null;
        PatientApplicationControllersFactory patientApplicationControllersFactory = new PatientApplicationControllersFactoryImpl(appointmentDao, requestDaoFactory.create(), responseDaoFactory.create(), officeDao, slotDao);
        LoginFactory loginControllerFactory = new LoginFactoryImpl(userDao);
        RegisterUserFactory registerUserFactory = new RegisterUserFactoryImpl(userDao);
        DoctorApplicationControllersFactory doctorApplicationControllersFactory = new DoctorApplicationControllerFactoryImpl(appointmentDao, slotDao, officeDao, responseDaoFactory.create(), requestDaoFactory.create());
        LoginControllerFactoryImpl loginFactory = new LoginControllerFactoryImpl(loginControllerFactory, registerUserFactory, patientApplicationControllersFactory, doctorApplicationControllersFactory);
        if(System.currentTimeMillis() %2 == 0) {
            loader.setLocation(getClass().getResource("view/navigator.fxml"));
            scene = new Scene(loader.load());
            navigatorController = loader.getController();
            LoginNavigator loginNavigator = new LoginNavigator(navigatorController, loginFactory);
            loginFactory.setLoginNavigator(loginNavigator);
            loginNavigator.navigate(LoginDestination.LOGIN);
        } else {

            loader.setLocation(getClass().getResource("view/command_line.fxml"));
            loader.setControllerFactory(f -> new CommandLine(new SecondLoginInterface(loginControllerFactory.create(), new StateFactory(patientApplicationControllersFactory, doctorApplicationControllersFactory))));
            scene = new Scene(loader.load());
        //}

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}