package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.auth.login.Login;
import ispw.uniroma2.doctorhouse.auth.login.LoginJFX;
import ispw.uniroma2.doctorhouse.auth.registration.RegisterUser;
import ispw.uniroma2.doctorhouse.auth.registration.RegisterUserJFX;
import ispw.uniroma2.doctorhouse.dao.UserDatabase;
import ispw.uniroma2.doctorhouse.notification.NotificationJFX;
import ispw.uniroma2.doctorhouse.patienthomepage.HomePageJFX;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescriptionJFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("dispatcher.fxml"));
        BorderPane root = loader.load();
        Dispatcher dispatcher = loader.getController();
        try {
            Properties p = new Properties();
            p.load(getClass().getResourceAsStream("credentials/u_login"));
            dispatcher.add(RegisterUserJFX.class, c -> new RegisterUserJFX(dispatcher, new RegisterUser(UserDatabase.getInstance(p))));
            dispatcher.add(LoginJFX.class, c -> new LoginJFX(dispatcher, new Login(UserDatabase.getInstance(p))));
            dispatcher.add(HomePageJFX.class, c -> new HomePageJFX(dispatcher));
            dispatcher.add(RequestPrescriptionJFX.class, c -> new RequestPrescriptionJFX(dispatcher));
            dispatcher.add(NotificationJFX.class, c -> new NotificationJFX(dispatcher));
        } catch (IOException e) {
            Platform.exit();
        }
        dispatcher.tryForward(LoginJFX.class, null);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}