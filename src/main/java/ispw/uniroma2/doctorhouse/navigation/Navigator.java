package ispw.uniroma2.doctorhouse.navigation;

public abstract class Navigator<D> {
    protected final NavigatorController controller;

    protected Navigator(NavigatorController controller) {
        this.controller = controller;
    }

    public abstract void navigate(D destination);
}
