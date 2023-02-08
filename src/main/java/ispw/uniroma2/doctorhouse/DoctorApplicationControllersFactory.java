package ispw.uniroma2.doctorhouse;

import ispw.uniroma2.doctorhouse.rearrange.AskForRearrange;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrange;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;

public interface DoctorApplicationControllersFactory {
    AskForRearrange createAskForRearrange();
    DoRearrange createDoRearrange();
    RequestPrescription createRequestPrescription();
    ResponseRequest createResponseRequest();

}
