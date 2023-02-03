package ispw.uniroma2.doctorhouse.navigation.doctor;

import ispw.uniroma2.doctorhouse.navigation.ViewController;

import java.io.IOException;

public interface DoctorControllerFactory {
    ViewController createResponsePrescription() throws IOException;
    ViewController createHomePage() throws IOException;
    ViewController createDoRearrangeAppointment() throws IOException;
    ViewController createRearrangeAppointment() throws IOException;
    ViewController createIrrecoverableErrorController() throws IOException;
    ViewController createRequestPrescription() throws IOException;
}
