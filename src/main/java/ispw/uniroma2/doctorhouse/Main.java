package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.dao.*;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDaoFactory;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.OfficeDaoFactory;
import ispw.uniroma2.doctorhouse.dao.OfficeDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.ShiftDao;
import ispw.uniroma2.doctorhouse.dao.ShiftDaoFactory;
import ispw.uniroma2.doctorhouse.dao.ShiftDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.SpecialtyDaoFactory;
import ispw.uniroma2.doctorhouse.dao.SpecialtyDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.UserDao;
import ispw.uniroma2.doctorhouse.dao.UserDaoFactory;
import ispw.uniroma2.doctorhouse.dao.UserDatabaseFactoryImpl;
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/navigator.fxml"));
        Scene scene = new Scene(loader.load());
        // creating all the factories needed to create DAOs
        SpecialtyDaoFactory specialtyDaoFactory = new SpecialtyDatabaseFactory(ConnectionFactory.getConnection());
        SpecialtyDao specialtyDao = specialtyDaoFactory.create();

        ShiftDaoFactory shiftDaoFactory = new ShiftDatabaseFactory(ConnectionFactory.getConnection());
        ShiftDao shiftDao = shiftDaoFactory.create();

        OfficeDaoFactory officeDaoFactory = new OfficeDatabaseFactory(ConnectionFactory.getConnection());
        officeDaoFactory.setSpecialtyDao(specialtyDao);
        officeDaoFactory.setShiftDao(shiftDao);
        OfficeDao officeDao = officeDaoFactory.create();

        UserDaoFactory userDaoFactory = new UserDatabaseFactoryImpl(ConnectionFactory.getConnection());
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

        NavigatorController navigatorController = loader.getController();

        LoginControllerFactoryImpl loginFactory = new LoginControllerFactoryImpl();

        PatientControllerFactoryImpl patientFactory = new PatientControllerFactoryImpl();
        patientFactory.setRequestDaoFactory(new RequestDaoFactoryImpl(ConnectionFactory.getConnection()));

        PatientNavigator patientNavigator = new PatientNavigator(navigatorController, patientFactory);
        patientFactory.setAppointmentDaoFactory(appointmentDaoFactory);
        patientFactory.setOfficeDaoFactory(officeDaoFactory);
        patientFactory.setRequestDaoFactory(requestDaoFactory);
        patientFactory.setSlotDaoFactory(slotDaoFactory);
        patientFactory.setNavigator(patientNavigator);

        DoctorControllerFactoryImpl doctorControllerFactory = new DoctorControllerFactoryImpl();
        DoctorNavigator doctorNavigator = new DoctorNavigator(navigatorController, doctorControllerFactory);
        doctorControllerFactory.setAppointmentDaoFactory(appointmentDaoFactory);
        doctorControllerFactory.setOfficeDaoFactory(officeDaoFactory);
        doctorControllerFactory.setResponseDaoFactory(responseDaoFactory);
        doctorControllerFactory.setSlotDaoFactory(slotDaoFactory);
        doctorControllerFactory.setNavigator(doctorNavigator);

        LoginNavigator loginNavigator = new LoginNavigator(navigatorController, loginFactory);
        loginFactory.setUserDaoFactory(userDaoFactory);
        loginFactory.setLoginNavigator(loginNavigator);
        patientFactory.setRequestDaoFactory(requestDaoFactory);
        patientFactory.setResponseDaoFactory(responseDaoFactory);
        doctorControllerFactory.setResponseDaoFactory(responseDaoFactory);
        doctorControllerFactory.setRequestDaoFactory(new RequestDaoFactoryImpl(ConnectionFactory.getConnection()));
        loginFactory.setPatientNavigator(patientNavigator);
        loginFactory.setDoctorNavigator(doctorNavigator);
        loginNavigator.navigate(LoginDestination.LOGIN);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}