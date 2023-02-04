package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.dao.RequestDao;
import ispw.uniroma2.doctorhouse.dao.ResponseDao;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.slot.SlotDao;
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
    private final SlotDao slotDao;

    public PatientApplicationControllersFactoryImpl(AppointmentDao appointmentDao, RequestDao requestDao, ResponseDao responseDao, OfficeDao officeDao, SlotDao slotDao) {
        this.appointmentDao = appointmentDao;
        this.requestDao = requestDao;
        this.responseDao = responseDao;
        this.officeDao = officeDao;
        this.slotDao = slotDao;
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
}
