package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.dao.RequestDaoFactory;
import ispw.uniroma2.doctorhouse.dao.ResponseDaoFactory;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorControllerFactory;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorNavigator;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class DoctorControllerFactoryImpl implements DoctorControllerFactory {

    private DoctorNavigator navigator;

    private ResponseDaoFactory responseDaoFactory;

    private RequestDaoFactory requestDaoFactory;

    public void setResponseDaoFactory(ResponseDaoFactory responseDaoFactory) {
        this.responseDaoFactory = responseDaoFactory;
    }

    public void setRequestDaoFactory(RequestDaoFactory requestDaoFactory) {
        this.requestDaoFactory = requestDaoFactory;
    }

    public void setNavigator(DoctorNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public ViewController createResponsePrescription() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("doctor_response_page.fxml"));
        loader.setControllerFactory(f -> new ResponseRequestGraphicController(navigator, new ResponseRequest(responseDaoFactory.create(), requestDaoFactory.create())));
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
