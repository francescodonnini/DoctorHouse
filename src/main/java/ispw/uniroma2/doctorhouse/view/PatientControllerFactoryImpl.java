package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientControllerFactory;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class PatientControllerFactoryImpl implements PatientControllerFactory {
    private PatientNavigator navigator;

    public void setNavigator(PatientNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public ViewController createHomePage() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("patient-home-page.fxml"));
        loader.setControllerFactory(f -> new HomePage(navigator));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }

    @Override
    public ViewController createRearrangeAppointmentPage() {
        return null;
    }

    @Override
    public ViewController createRequestPrescriptionPage() {
        return null;
    }
}
