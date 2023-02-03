package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.dao.*;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDaoFactory;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.slot.SlotDaoFactory;
import ispw.uniroma2.doctorhouse.dao.slot.SlotDatabaseFactory;
import ispw.uniroma2.doctorhouse.navigation.NavigatorController;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorNavigator;
import ispw.uniroma2.doctorhouse.navigation.login.LoginDestination;
import ispw.uniroma2.doctorhouse.navigation.login.LoginNavigator;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
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

        OfficeDaoFactory officeDaoFactory = new OfficeDatabaseFactory(ConnectionFactory.getConnection());
        officeDaoFactory.setSpecialtyDao(specialtyDao);
        officeDaoFactory.setShiftDao(shiftDao);
        OfficeDao officeDao = officeDaoFactory.create();

        UserDaoFactory userDaoFactory;
        if (System.currentTimeMillis() % 2 == 0) {
            userDaoFactory = new UserDatabaseFactoryImpl(ConnectionFactory.getConnection());
        } else {
            userDaoFactory = new UserFileFactory();
        }
        userDaoFactory.setOfficeDao(officeDao);
        UserDao userDao = userDaoFactory.create();

        AppointmentDaoFactory appointmentDaoFactory = new AppointmentDatabaseFactory(ConnectionFactory.getConnection());
        appointmentDaoFactory.setOfficeDao(officeDao);
        appointmentDaoFactory.setSpecialtyDao(specialtyDao);
        appointmentDaoFactory.setUserDao(userDao);
        AppointmentDao appointmentDao = appointmentDaoFactory.create();

        PrescriptionDaoFactory prescriptionDaoFactory = new PrescriptionDatabaseFactory(ConnectionFactory.getConnection());

        RequestDaoFactory requestDaoFactory = new RequestDaoFactoryImpl(ConnectionFactory.getConnection());
        ResponseDaoFactory responseDaoFactory = new ResponseDaoFactoryImpl(ConnectionFactory.getConnection(), prescriptionDaoFactory.create());

        SlotDaoFactory slotDaoFactory = new SlotDatabaseFactory(ConnectionFactory.getConnection());
        slotDaoFactory.setAppointmentDao(appointmentDao);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/navigator.fxml"));
        Scene scene = new Scene(loader.load());
        NavigatorController navigatorController = loader.getController();

        LoginControllerFactoryImpl loginFactory = new LoginControllerFactoryImpl();

        PatientControllerFactoryImpl patientFactory = new PatientControllerFactoryImpl();
        patientFactory.setRequestDaoFactory(new RequestDaoFactoryImpl(ConnectionFactory.getConnection()));

        FXMLLoader toolbarLoader = new FXMLLoader();
        toolbarLoader.setLocation(getClass().getResource("view/patient-toolbar.fxml"));
        toolbarLoader.load();
        PatientNavigator patientNavigator = new PatientNavigator(navigatorController, toolbarLoader.getController(), patientFactory);
        patientFactory.setAppointmentDaoFactory(appointmentDaoFactory);
        patientFactory.setOfficeDaoFactory(officeDaoFactory);
        patientFactory.setRequestDaoFactory(requestDaoFactory);
        patientFactory.setSlotDaoFactory(slotDaoFactory);
        patientFactory.setRequestDaoFactory(requestDaoFactory);
        patientFactory.setResponseDaoFactory(responseDaoFactory);
        patientFactory.setNavigator(patientNavigator);

        FXMLLoader doctorToolbarLoader = new FXMLLoader();
        doctorToolbarLoader.setLocation(getClass().getResource("view/doctor-toolbar.fxml"));
        doctorToolbarLoader.load();
        DoctorControllerFactoryImpl doctorControllerFactory = new DoctorControllerFactoryImpl();
        DoctorNavigator doctorNavigator = new DoctorNavigator(navigatorController, doctorToolbarLoader.getController(), doctorControllerFactory);
        doctorControllerFactory.setAppointmentDaoFactory(appointmentDaoFactory);
        doctorControllerFactory.setOfficeDaoFactory(officeDaoFactory);
        doctorControllerFactory.setResponseDaoFactory(responseDaoFactory);
        doctorControllerFactory.setSlotDaoFactory(slotDaoFactory);
        doctorControllerFactory.setResponseDaoFactory(responseDaoFactory);
        doctorControllerFactory.setRequestDaoFactory(new RequestDaoFactoryImpl(ConnectionFactory.getConnection()));
        doctorControllerFactory.setNavigator(doctorNavigator);

        LoginNavigator loginNavigator = new LoginNavigator(navigatorController, loginFactory);
        loginFactory.setUserDaoFactory(userDaoFactory);
        loginFactory.setLoginNavigator(loginNavigator);
        loginFactory.setPatientNavigator(patientNavigator);
        loginFactory.setDoctorNavigator(doctorNavigator);
        loginNavigator.navigate(LoginDestination.LOGIN);

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}