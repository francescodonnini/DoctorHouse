package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.dao.RequestDaoFactory;
import ispw.uniroma2.doctorhouse.dao.ResponseDaoFactory;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientControllerFactory;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class PatientControllerFactoryImpl implements PatientControllerFactory {
    private PatientNavigator navigator;

    private RequestDaoFactory requestDaoFactory;

    private ResponseDaoFactory responseDaoFactory;


    public void setRequestDaoFactory(RequestDaoFactory requestDaoFactory) {
        this.requestDaoFactory = requestDaoFactory;
    }

    public void setResponseDaoFactory(ResponseDaoFactory responseDaoFactory) {
        this.responseDaoFactory = responseDaoFactory;
    }

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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("patient-request-page.fxml"));
        loader.setControllerFactory(f -> new RequestPrescriptionGraphicController(navigator, new RequestPrescription(requestDaoFactory.create(), responseDaoFactory.create())));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }
}
