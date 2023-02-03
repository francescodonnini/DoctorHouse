package ispw.uniroma2.doctorhouse.navigation.patient;

import ispw.uniroma2.doctorhouse.navigation.ViewController;

import java.io.IOException;

public interface PatientControllerFactory {
    ViewController createHomePage() throws IOException;
    ViewController createDoRearrangeAppointmentPage() throws IOException;
    ViewController createRearrangeAppointmentPage() throws IOException;
    ViewController createRequestPrescriptionPage() throws IOException;
    ViewController createNoDestinationPage() throws IOException;
}
