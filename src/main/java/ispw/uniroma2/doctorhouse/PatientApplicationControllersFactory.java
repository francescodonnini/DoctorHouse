package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.rearrange.AskForRearrange;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrange;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;

public interface PatientApplicationControllersFactory {
    AskForRearrange createAskForRearrange();
    DoRearrange createDoRearrange();
    RequestPrescription createRequestPrescription();

    Logout createLogout();
}
