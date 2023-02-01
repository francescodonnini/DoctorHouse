package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDaoFactory;
import ispw.uniroma2.doctorhouse.dao.OfficeDaoFactory;
import ispw.uniroma2.doctorhouse.dao.RequestDaoFactory;
import ispw.uniroma2.doctorhouse.dao.ResponseDaoFactory;
import ispw.uniroma2.doctorhouse.dao.slot.SlotDaoFactory;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientControllerFactory;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientNavigator;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrangeImpl;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrangeImpl;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class PatientControllerFactoryImpl implements PatientControllerFactory {
    private PatientNavigator navigator;
    private AppointmentDaoFactory appointmentDaoFactory;
    private OfficeDaoFactory officeDaoFactory;
    private RequestDaoFactory requestDaoFactory;
    private SlotDaoFactory slotDaoFactory;

    private ResponseDaoFactory responseDaoFactory;

    public void setAppointmentDaoFactory(AppointmentDaoFactory appointmentDaoFactory) {
        this.appointmentDaoFactory = appointmentDaoFactory;
    }

    public void setOfficeDaoFactory(OfficeDaoFactory officeDaoFactory) {
        this.officeDaoFactory = officeDaoFactory;
    }

    public void setRequestDaoFactory(RequestDaoFactory requestDaoFactory) {
        this.requestDaoFactory = requestDaoFactory;
    }

    public void setResponseDaoFactory(ResponseDaoFactory responseDaoFactory) {
        this.responseDaoFactory = responseDaoFactory;
    }
    public void setSlotDaoFactory(SlotDaoFactory slotDaoFactory) {
        this.slotDaoFactory = slotDaoFactory;
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ask-page.fxml"));
        loader.setControllerFactory(f -> new PatientAskPage(new AskForRearrangeImpl(appointmentDaoFactory.create(), slotDaoFactory.create(), officeDaoFactory.create()), navigator));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public ViewController createRequestPrescriptionPage() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("patient-request-page.fxml"));
        loader.setControllerFactory(f -> new PatientRequestPrescriptionGraphicController(navigator, new RequestPrescription(requestDaoFactory.create(), responseDaoFactory.create())));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }

    @Override
    public ViewController createDoRearrangeAppointmentPage() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("do-rearrange-page.fxml"));
        loader.setControllerFactory(f -> new PatientDoRearrange(new DoRearrangeImpl(appointmentDaoFactory.create()), navigator));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        }
    }
}
