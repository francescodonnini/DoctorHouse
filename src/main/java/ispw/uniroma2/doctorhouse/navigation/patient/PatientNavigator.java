package ispw.uniroma2.doctorhouse.navigation.patient;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.NavigatorController;
import ispw.uniroma2.doctorhouse.navigation.ToolBarController;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.view.PatientToolBarController;
import javafx.application.Platform;

import java.io.IOException;

public class PatientNavigator extends Navigator<PatientDestination> {
    private final PatientControllerFactory factory;
    private final ToolBarController toolBarController;

    public PatientNavigator(NavigatorController controller, PatientToolBarController toolBarController, PatientControllerFactory factory) {
        super(controller);
        toolBarController.setNavigator(this);
        this.toolBarController = toolBarController;
        this.factory = factory;
    }

    @Override
    public void navigate(PatientDestination destination) {
        controller.attach(toolBarController);
        try {
            controller.push(makeViewController(destination));
        } catch (IOException e) {
            Platform.exit();
        }
    }

    private ViewController makeViewController(PatientDestination destination) throws IOException {
        switch (destination) {
            case REARRANGE:
                return factory.createRearrangeAppointmentPage();
            case DO_REARRANGE:
                return factory.createDoRearrangeAppointmentPage();
            case REQUEST_PRESCRIPTION:
                return factory.createRequestPrescriptionPage();
            case HOME_PAGE:
                return factory.createHomePage();
            default:
                return factory.createNoDestinationPage();
        }
    }
}
