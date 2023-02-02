package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDaoFactory;
import ispw.uniroma2.doctorhouse.dao.OfficeDaoFactory;
import ispw.uniroma2.doctorhouse.dao.RequestDaoFactory;
import ispw.uniroma2.doctorhouse.dao.ResponseDaoFactory;
import ispw.uniroma2.doctorhouse.dao.slot.SlotDaoFactory;
import ispw.uniroma2.doctorhouse.navigation.ViewController;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorControllerFactory;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorNavigator;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrangeImpl;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrangeImpl;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class DoctorControllerFactoryImpl implements DoctorControllerFactory {
    private DoctorNavigator navigator;
    private AppointmentDaoFactory appointmentDaoFactory;
    private OfficeDaoFactory officeDaoFactory;
    private ResponseDaoFactory responseDaoFactory;
    private SlotDaoFactory slotDaoFactory;

    public void setAppointmentDaoFactory(AppointmentDaoFactory appointmentDaoFactory) {
        this.appointmentDaoFactory = appointmentDaoFactory;
    }

    public void setOfficeDaoFactory(OfficeDaoFactory officeDaoFactory) {
        this.officeDaoFactory = officeDaoFactory;
    }

    private RequestDaoFactory requestDaoFactory;

    public void setResponseDaoFactory(ResponseDaoFactory responseDaoFactory) {
        this.responseDaoFactory = responseDaoFactory;
    }

    public void setRequestDaoFactory(RequestDaoFactory requestDaoFactory) {
        this.requestDaoFactory = requestDaoFactory;
    }

    public void setSlotDaoFactory(SlotDaoFactory slotDaoFactory) {
        this.slotDaoFactory = slotDaoFactory;
    }

    public void setNavigator(DoctorNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public ViewController createResponsePrescription() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("doctor_response_page.fxml"));
        loader.setControllerFactory(f -> new ResponseRequestGraphicController(new ResponseRequest(responseDaoFactory.create(), requestDaoFactory.create())));
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
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }

    @Override
    public ViewController createDoRearrangeAppointment() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("do-rearrange-page.fxml"));
        loader.setControllerFactory(f -> new DoctorDoRearrange(new DoRearrangeImpl(appointmentDaoFactory.create()), navigator));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ViewController createRearrangeAppointment() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ask-page.fxml"));
        loader.setControllerFactory(f -> new DoctorAskPage(new AskForRearrangeImpl(appointmentDaoFactory.create(), slotDaoFactory.create(), officeDaoFactory.create()), navigator));
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

    @Override
    public ViewController createRequestPrescription() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("patient-request-page.fxml"));
        loader.setControllerFactory(f -> new DoctorRequestPrescriptionGraphicController(new RequestPrescription(requestDaoFactory.create(), responseDaoFactory.create())));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }
}
