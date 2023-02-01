package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientDestination;

import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;

public class PatientRequestPrescriptionGraphicController extends RequestPrescriptionGraphicController<PatientDestination> {
    public PatientRequestPrescriptionGraphicController(Navigator<PatientDestination> navigator, RequestPrescription requestPrescription) {
        super(navigator, requestPrescription);
    }

    @Override
    public void rearrange() {
        navigator.navigate(PatientDestination.REARRANGE);
    }

}
