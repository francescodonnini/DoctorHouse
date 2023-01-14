package ispw.uniroma2.doctorhouse;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class Dispatcher {
    private final Map<Class<?>, Callback<Class<?>, Object>> factories;
    @FXML
    private BorderPane root;

    public Dispatcher() {
        factories = new HashMap<>();
    }

    public void add(Class<?> action, Callback<Class<?>, Object> factory) {
        factories.put(action, factory);
    }

    public void tryForward(Class<?> controllerClass, Properties args) {
        //load the fxml file with the same name of graphic controller
        load(controllerClass, args);
    }

    private void load(Class<?> controllerClass, Properties args) {
        try {
            String fxml = controllerClass.getPackageName()
                    .replace(Dispatcher.class.getPackageName(), "")
                    .substring(1)
                    .replace(".", "/")
                    .concat("/")
                    .concat(controllerClass.getSimpleName())
                    .concat(".fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Objects.requireNonNull(getClass().getResource(fxml)));
            if (factories.containsKey(controllerClass)) {
                loader.setControllerFactory(factories.get(controllerClass));
            }
            Node view = loader.load();
            root.setCenter(view);
            EndPoint controller = loader.getController();
            controller.onEnter(args);
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        } catch (IrrecoverableError e) {
            Platform.exit();
        }
    }
}
