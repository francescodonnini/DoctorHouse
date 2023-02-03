package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.dao.RequestDaoFactory;
import ispw.uniroma2.doctorhouse.dao.ResponseDaoFactory;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientControllerFactory;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrangeFactory;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrangeFactory;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class PatientControllerFactoryImpl implements PatientControllerFactory {
    private RequestDaoFactory requestDaoFactory;
    private ResponseDaoFactory responseDaoFactory;
    private final AskForRearrangeFactory askFactory;
    private final DoRearrangeFactory doRearrangeFactory;

    public PatientControllerFactoryImpl(AskForRearrangeFactory askFactory, DoRearrangeFactory doRearrangeFactory) {
        this.askFactory = askFactory;
        this.doRearrangeFactory = doRearrangeFactory;
    }


    public void setRequestDaoFactory(RequestDaoFactory requestDaoFactory) {
        this.requestDaoFactory = requestDaoFactory;
    }

    public void setResponseDaoFactory(ResponseDaoFactory responseDaoFactory) {
        this.responseDaoFactory = responseDaoFactory;
    }

    @Override
    public ViewController createHomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("patient-home-page.fxml"));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createRearrangeAppointmentPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ask-page.fxml"));
        loader.setControllerFactory(f -> new AskPage(askFactory.create()));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createRequestPrescriptionPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("patient-request-page.fxml"));
        loader.setControllerFactory(f -> new PatientRequestPrescriptionGraphicController(new RequestPrescription(requestDaoFactory.create(), responseDaoFactory.create())));
        loader.load();
        return loader.getController();

    }

    @Override
    public ViewController createNoDestinationPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("no-destination-page.fxml"));
        loader.load();
        return loader.getController();
    }

    @Override
    public ViewController createDoRearrangeAppointmentPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("do-rearrange-page.fxml"));
        loader.setControllerFactory(f -> new DoRearrangePage(doRearrangeFactory.create()));
        loader.load();
        return loader.getController();
    }
}
