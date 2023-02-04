package ispw.uniroma2.doctorhouse.navigation;

public interface NavigatorController extends ViewController {
    void push(ViewController controller);
    void pop();
}
