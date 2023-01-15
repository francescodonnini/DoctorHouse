package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.dao.UserDao;
import ispw.uniroma2.doctorhouse.dao.UserDaoFactory;
import ispw.uniroma2.doctorhouse.dao.UserDaoFactoryImpl;
import ispw.uniroma2.doctorhouse.dao.UserDatabase;
import ispw.uniroma2.doctorhouse.navigation.NavigatorController;
import ispw.uniroma2.doctorhouse.navigation.login.LoginControllerFactory;
import ispw.uniroma2.doctorhouse.navigation.login.LoginDestination;
import ispw.uniroma2.doctorhouse.navigation.login.LoginNavigator;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import ispw.uniroma2.doctorhouse.view.LoginControllerFactoryImpl;
import ispw.uniroma2.doctorhouse.view.NavigatorControllerImpl;
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
        UserDaoFactory userDaoFactory = new UserDaoFactoryImpl();
        LoginControllerFactoryImpl factory = new LoginControllerFactoryImpl();
        PatientControllerFactoryImpl patientFactory = new PatientControllerFactoryImpl();
        LoginNavigator loginNavigator = new LoginNavigator(navigatorController, factory);
        PatientNavigator patientNavigator = new PatientNavigator(navigatorController, patientFactory);
        factory.setUserDaoFactory(userDaoFactory);
        factory.setLoginNavigator(loginNavigator);
        factory.setPatientNavigator(patientNavigator);
        loginNavigator.navigate(LoginDestination.Login);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}