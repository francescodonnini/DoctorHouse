package ispw.uniroma2.doctorhouse.navigation.patient;

import ispw.uniroma2.doctorhouse.navigation.ViewController;

public interface PatientControllerFactory {
    ViewController createHomePage();
    ViewController createDoRearrangeAppointmentPage();
    ViewController createRearrangeAppointmentPage();
    ViewController createRequestPrescriptionPage();
}
