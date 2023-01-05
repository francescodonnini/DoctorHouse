package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.auth.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.auth.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.auth.registration.RegisterUser;
import ispw.uniroma2.doctorhouse.auth.registration.RegisterUserJFX;
import ispw.uniroma2.doctorhouse.dao.UserDao;
import ispw.uniroma2.doctorhouse.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getResource("auth/registration/user-registration.fxml")));
        loader.setControllerFactory(c -> new RegisterUserJFX(new RegisterUser(new UserDao() {
            @Override
            public Optional<User> get(LoginRequestBean loginRequest) {
                return Optional.empty();
            }

            @Override
            public boolean create(UserRegistrationRequestBean registrationRequest) {
                return false;
            }
        })));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}