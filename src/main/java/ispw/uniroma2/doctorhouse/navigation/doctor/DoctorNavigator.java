package ispw.uniroma2.doctorhouse.navigation.doctor;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.NavigatorController;
import ispw.uniroma2.doctorhouse.navigation.ViewController;


public class DoctorNavigator extends Navigator<DoctorDestination> {

    private final DoctorControllerFactory factory;

    public DoctorNavigator(NavigatorController controller, DoctorControllerFactory factory) {
        super(controller);
        this.factory = factory;
    }

    @Override
    public void navigate(DoctorDestination destination) {
        controller.push(makeViewController(destination));
    }

    private ViewController makeViewController(DoctorDestination destination) {
        switch (destination) {
            case DOCTOR_HOME_PAGE:
                return factory.createHomePage();
            case RESPONSE_PRESCRIPTION_REQUEST:
                return factory.createResponsePrescription();
            case REARRANGE_APPOINTMENT:
                return factory.createRearrangeAppointment();
            default:
                return factory.createIrrecoverableErrorController();
        }
    }
}
