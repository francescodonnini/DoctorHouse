package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.dao.RequestDaoFactory;
import ispw.uniroma2.doctorhouse.dao.ResponseDaoFactory;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorControllerFactory;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrangeFactory;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrangeFactory;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class DoctorControllerFactoryImpl implements DoctorControllerFactory {
    private final AskForRearrangeFactory askFactory;
    private final DoRearrangeFactory doRearrangeFactory;
    private ResponseDaoFactory responseDaoFactory;
    private RequestDaoFactory requestDaoFactory;

    public DoctorControllerFactoryImpl(AskForRearrangeFactory askFactory, DoRearrangeFactory doRearrangeFactory) {
        this.askFactory = askFactory;
        this.doRearrangeFactory = doRearrangeFactory;
    }


    public void setResponseDaoFactory(ResponseDaoFactory responseDaoFactory) {
        this.responseDaoFactory = responseDaoFactory;
    }

    public void setRequestDaoFactory(RequestDaoFactory requestDaoFactory) {
        this.requestDaoFactory = requestDaoFactory;
    }

    @Override
    public ViewController createResponsePrescription() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("doctor_response_page.fxml"));
        loader.setControllerFactory(f -> new ResponseRequestGraphicController(new ResponseRequest(responseDaoFactory.create(), requestDaoFactory.create())));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createHomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("doctor-home-page.fxml"));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createDoRearrangeAppointment() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("do-rearrange-page.fxml"));
        loader.setControllerFactory(f -> new DoRearrangePage(doRearrangeFactory.create()));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createRearrangeAppointment() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ask-page.fxml"));
        loader.setControllerFactory(f -> new AskPage(askFactory.create()));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createIrrecoverableErrorController() throws IOException {
        throw new IOException();
    }

    @Override
    public ViewController createRequestPrescription() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("patient-request-page.fxml"));
        loader.setControllerFactory(f -> new DoctorRequestPrescriptionGraphicController(new RequestPrescription(requestDaoFactory.create(), responseDaoFactory.create())));
        loader.load();
        return loader.getController();
    }
}
