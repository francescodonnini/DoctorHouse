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
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorNavigator;
import ispw.uniroma2.doctorhouse.navigation.login.LoginDestination;
import ispw.uniroma2.doctorhouse.navigation.login.LoginNavigator;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrangeFactory;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrangeFactoryImpl;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrangeFactory;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrangeFactoryImpl;
import ispw.uniroma2.doctorhouse.view.DoctorControllerFactoryImpl;
import ispw.uniroma2.doctorhouse.view.LoginControllerFactoryImpl;
import ispw.uniroma2.doctorhouse.view.PatientControllerFactoryImpl;
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
        loader.setLocation(getClass().getResource("view/navigator.fxml"));
        Scene scene = new Scene(loader.load());
        NavigatorController navigatorController = loader.getController();

        AskForRearrangeFactory askFactory = new AskForRearrangeFactoryImpl(appointmentDao, officeDao, slotDao);
        DoRearrangeFactory doRearrangeFactory = new DoRearrangeFactoryImpl(appointmentDao);

        PatientControllerFactoryImpl patientFactory = new PatientControllerFactoryImpl(askFactory, doRearrangeFactory);
        patientFactory.setRequestDaoFactory(new RequestDaoFactoryImpl(ConnectionFactory.getConnection()));

        FXMLLoader toolbarLoader = new FXMLLoader();
        toolbarLoader.setLocation(getClass().getResource("view/patient-toolbar.fxml"));
        toolbarLoader.load();
        PatientNavigator patientNavigator = new PatientNavigator(navigatorController, toolbarLoader.getController(), patientFactory);
        patientFactory.setRequestDaoFactory(requestDaoFactory);
        patientFactory.setResponseDaoFactory(responseDaoFactory);

        FXMLLoader doctorToolbarLoader = new FXMLLoader();
        doctorToolbarLoader.setLocation(getClass().getResource("view/doctor-toolbar.fxml"));
        doctorToolbarLoader.load();
        DoctorControllerFactoryImpl doctorControllerFactory = new DoctorControllerFactoryImpl(askFactory, doRearrangeFactory);
        DoctorNavigator doctorNavigator = new DoctorNavigator(navigatorController, doctorToolbarLoader.getController(), doctorControllerFactory);
        doctorControllerFactory.setResponseDaoFactory(responseDaoFactory);
        doctorControllerFactory.setResponseDaoFactory(responseDaoFactory);
        doctorControllerFactory.setRequestDaoFactory(new RequestDaoFactoryImpl(ConnectionFactory.getConnection()));

        LoginFactory loginAppControllerFactory = new LoginFactoryImpl(userDao);
        RegisterUserFactory registerUserFactory = new RegisterUserFactoryImpl(userDao);
        LoginControllerFactoryImpl loginFactory = new LoginControllerFactoryImpl(loginAppControllerFactory, registerUserFactory, patientNavigator, doctorNavigator);
        LoginNavigator loginNavigator = new LoginNavigator(navigatorController, loginFactory);
        loginFactory.setLoginNavigator(loginNavigator);
        loginNavigator.navigate(LoginDestination.LOGIN);

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}