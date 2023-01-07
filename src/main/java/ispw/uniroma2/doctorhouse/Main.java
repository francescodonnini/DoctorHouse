package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.auth.login.Login;
import ispw.uniroma2.doctorhouse.auth.login.LoginJFX;
import ispw.uniroma2.doctorhouse.auth.registration.RegisterUser;
import ispw.uniroma2.doctorhouse.auth.registration.RegisterUserJFX;
import ispw.uniroma2.doctorhouse.dao.UserDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, InvalidDestination, URISyntaxException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("dispatcher.fxml"));
        BorderPane root = loader.load();
        Dispatcher dispatcher = loader.getController();
        final Properties loginCredentials = new Properties();
        File file = new File(Objects.requireNonNull(getClass().getResource("credentials/u_login")).toURI());
        loginCredentials.load(new FileReader(file));
        final Properties patientCredentials = new Properties();
        file = new File(Objects.requireNonNull(getClass().getResource("credentials/u_patient")).toURI());
        patientCredentials.load(new FileReader(file));
        dispatcher.add(RegisterUserJFX.class, c -> new RegisterUserJFX(dispatcher, new RegisterUser(UserDatabase.getInstance(loginCredentials))));
        dispatcher.add(LoginJFX.class, c -> new LoginJFX(dispatcher, new Login(UserDatabase.getInstance(patientCredentials))));
        dispatcher.tryForward(LoginJFX.class, null);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}