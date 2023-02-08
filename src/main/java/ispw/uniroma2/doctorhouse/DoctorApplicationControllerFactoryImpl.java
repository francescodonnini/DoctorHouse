package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.dao.requests.RequestDao;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDao;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.slot.SlotDao;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrange;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrangeImpl;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrange;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrangeImpl;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;

public class DoctorApplicationControllerFactoryImpl implements DoctorApplicationControllersFactory {

    private final AppointmentDao appointmentDao;
    private final SlotDao slotDao;
    private final OfficeDao officeDao;
    private final ResponseDao responseDao;
    private final RequestDao requestDao;

    public DoctorApplicationControllerFactoryImpl(AppointmentDao appointmentDao, SlotDao slotDao, OfficeDao officeDao, ResponseDao responseDao, RequestDao requestDao) {
        this.appointmentDao = appointmentDao;
        this.slotDao = slotDao;
        this.officeDao = officeDao;
        this.responseDao = responseDao;
        this.requestDao = requestDao;
    }

    @Override
    public AskForRearrange createAskForRearrange() {
        return new AskForRearrangeImpl(appointmentDao, slotDao, officeDao);
    }

    @Override
    public DoRearrange createDoRearrange() {
        return new DoRearrangeImpl(appointmentDao);
    }

    @Override
    public RequestPrescription createRequestPrescription() {
        return new RequestPrescription(requestDao, responseDao);
    }

    @Override
    public ResponseRequest createResponseRequest() {
        return new ResponseRequest(responseDao, requestDao);
    }

}
