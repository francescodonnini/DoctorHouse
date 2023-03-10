package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.dao.requests.RequestDao;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDao;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrange;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrangeImpl;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrange;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrangeImpl;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;

public class PatientApplicationControllersFactoryImpl implements PatientApplicationControllersFactory {
    private final AppointmentDao appointmentDao;
    private final RequestDao requestDao;
    private final ResponseDao responseDao;
    private final OfficeDao officeDao;

    public PatientApplicationControllersFactoryImpl(AppointmentDao appointmentDao, RequestDao requestDao, ResponseDao responseDao, OfficeDao officeDao) {
        this.appointmentDao = appointmentDao;
        this.requestDao = requestDao;
        this.responseDao = responseDao;
        this.officeDao = officeDao;
    }

    @Override
    public AskForRearrange createAskForRearrange() {
        return new AskForRearrangeImpl(appointmentDao, officeDao);
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
    public Logout createLogout() {
        return new Logout();
    }
}
