package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.dao.*;
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
        NavigatorController navigatorController = loader.getController();
        SpecialtyDaoFactory specialtyDaoFactory = new SpecialtyDatabaseFactory(ConnectionFactory.getConnection());
        SpecialtyDao specialtyDao = specialtyDaoFactory.create();
        ShiftDaoFactory shiftDaoFactory = new ShiftDatabaseFactory(ConnectionFactory.getConnection());
        ShiftDao shiftDao = shiftDaoFactory.create();
        OfficeDaoFactory officeDaoFactory = new OfficeDatabaseFactory(ConnectionFactory.getConnection());
        officeDaoFactory.setShiftDao(shiftDao);
        officeDaoFactory.setSpecialtyDao(specialtyDao);
        UserDaoFactory userDaoFactory = new UserDatabaseFactoryImpl(ConnectionFactory.getConnection());
        userDaoFactory.setOfficeDao(officeDaoFactory.create());
        LoginControllerFactoryImpl loginFactory = new LoginControllerFactoryImpl();
        PatientControllerFactoryImpl patientFactory = new PatientControllerFactoryImpl();
        LoginNavigator loginNavigator = new LoginNavigator(navigatorController, loginFactory);
        PatientNavigator patientNavigator = new PatientNavigator(navigatorController, patientFactory);
        DoctorControllerFactoryImpl doctorControllerFactory = new DoctorControllerFactoryImpl();
        DoctorNavigator doctorNavigator = new DoctorNavigator(navigatorController, doctorControllerFactory);
        loginFactory.setUserDaoFactory(userDaoFactory);
        loginFactory.setLoginNavigator(loginNavigator);
        RequestDaoFactory requestDaoFactory = new RequestDaoFactoryImpl(ConnectionFactory.getConnection());
        patientFactory.setRequestDaoFactory(requestDaoFactory);
        ResponseDaoFactory responseDaoFactory = new ResponseDaoFactoryImpl(ConnectionFactory.getConnection());
        doctorControllerFactory.setResponseDaoFactory(responseDaoFactory);
        loginFactory.setPatientNavigator(patientNavigator);
        patientFactory.setNavigator(patientNavigator);
        doctorControllerFactory.setNavigator(doctorNavigator);
        loginFactory.setDoctorNavigator(doctorNavigator);
        loginNavigator.navigate(LoginDestination.LOGIN);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}