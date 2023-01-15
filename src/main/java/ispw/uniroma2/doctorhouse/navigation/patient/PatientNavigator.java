package ispw.uniroma2.doctorhouse.navigation.patient;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.NavigatorController;
import ispw.uniroma2.doctorhouse.navigation.ViewController;

public class PatientNavigator extends Navigator<PatientDestination> {
    private final PatientControllerFactory factory;

    public PatientNavigator(NavigatorController controller, PatientControllerFactory factory) {
        super(controller);
        this.factory = factory;
    }

    @Override
    public void navigate(PatientDestination destination) {
        controller.push(makeViewController(destination));
    }

    private ViewController makeViewController(PatientDestination destination) {
        switch (destination) {
            case REARRANGE:
                return factory.createRearrangeAppointmentPage();
            case REQUEST_PRESCRIPTION:
                return factory.createRequestPrescriptionPage();
            case HOME_PAGE:
                return factory.createHomePage();
            default:
                throw new UnsupportedOperationException();
        }
    }
}
