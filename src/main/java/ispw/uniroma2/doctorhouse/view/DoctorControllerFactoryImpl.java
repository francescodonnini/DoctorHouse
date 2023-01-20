package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorControllerFactory;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorNavigator;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class DoctorControllerFactoryImpl implements DoctorControllerFactory {

    private DoctorNavigator navigator;

    public void setNavigator(DoctorNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public ViewController createResponsePrescription() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("response-page.fxml"));
        loader.setControllerFactory(f -> new DoctorHomePage(navigator));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }

    @Override
    public ViewController createHomePage() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("doctor-home-page.fxml"));
        loader.setControllerFactory(f -> new DoctorHomePage(navigator));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }

    @Override
    public ViewController createRearrangeAppointment() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("doctor-rearrange-page.fxml"));
        loader.setControllerFactory(f -> new DoctorHomePage(navigator));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }

    @Override
    public ViewController createIrrecoverableErrorController() {
        return null;
    }
}
