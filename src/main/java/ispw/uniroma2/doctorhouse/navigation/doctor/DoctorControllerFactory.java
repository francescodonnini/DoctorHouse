package ispw.uniroma2.doctorhouse.navigation.doctor;

import ispw.uniroma2.doctorhouse.navigation.ViewController;

public interface DoctorControllerFactory {
    ViewController createResponsePrescription();
    ViewController createHomePage();

    ViewController createRearrangeAppointment();

    ViewController createIrrecoverableErrorController();
}
