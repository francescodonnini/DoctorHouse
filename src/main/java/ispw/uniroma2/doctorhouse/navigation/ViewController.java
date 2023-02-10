package ispw.uniroma2.doctorhouse.navigation;

import javafx.scene.Parent;

public interface ViewController {
    Parent getView();
    default void update() {}
}
