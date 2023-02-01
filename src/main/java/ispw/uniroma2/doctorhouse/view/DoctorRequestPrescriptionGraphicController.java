package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorDestination;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;

public class DoctorRequestPrescriptionGraphicController extends RequestPrescriptionGraphicController<DoctorDestination> {
    public DoctorRequestPrescriptionGraphicController(Navigator<DoctorDestination> navigator, RequestPrescription requestPrescription) {
        super(navigator, requestPrescription);
    }

    @Override
    public void rearrange() {
        navigator.navigate(DoctorDestination.DO_REARRANGE_APPOINTMENT);
    }
}
