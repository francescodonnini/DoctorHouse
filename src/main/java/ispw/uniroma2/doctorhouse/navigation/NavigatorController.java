package ispw.uniroma2.doctorhouse.navigation;

public interface NavigatorController extends ViewController {
    void attach(ToolBarController toolBarController);
    void push(ViewController controller);
    void pop();
}
