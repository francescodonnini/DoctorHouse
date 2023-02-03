package ispw.uniroma2.doctorhouse.navigation.doctor;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.NavigatorController;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.view.DoctorToolbarController;
import javafx.application.Platform;

import java.io.IOException;


public class DoctorNavigator extends Navigator<DoctorDestination> {
    private final DoctorControllerFactory factory;
    private final DoctorToolbarController toolbarController;

    public DoctorNavigator(NavigatorController controller, DoctorToolbarController toolbarController, DoctorControllerFactory factory) {
        super(controller);
        toolbarController.setNavigator(this);
        this.toolbarController = toolbarController;
        this.factory = factory;
    }

    @Override
    public void navigate(DoctorDestination destination) {
        controller.attach(toolbarController);
        try {
            controller.push(makeViewController(destination));
        } catch (IOException e) {
            Platform.exit();
        }
    }

    private ViewController makeViewController(DoctorDestination destination) throws IOException {
        switch (destination) {
            case HOME_PAGE:
                return factory.createHomePage();
            case RESPONSE_PRESCRIPTION_REQUEST:
                return factory.createResponsePrescription();
            case REARRANGE_APPOINTMENT:
                return factory.createRearrangeAppointment();
            case DO_REARRANGE_APPOINTMENT:
                return factory.createDoRearrangeAppointment();
            case REQUEST_PRESCRIPTION:
                return factory.createRequestPrescription();
            default:
                return factory.createIrrecoverableErrorController();
        }
    }
}
