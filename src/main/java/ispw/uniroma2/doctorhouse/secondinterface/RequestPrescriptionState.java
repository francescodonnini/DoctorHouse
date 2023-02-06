package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;

public class RequestPrescriptionState implements State{
    private final RequestPrescription requestPrescription;

    public RequestPrescriptionState(RequestPrescription requestPrescription) {
        this.requestPrescription = requestPrescription;
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException {
        //this method will be implementd
    }
}
