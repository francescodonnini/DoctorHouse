package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.PrescriptionRequestBean;
import ispw.uniroma2.doctorhouse.beans.ResponsePatientBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;

import java.util.List;
import java.util.Optional;


public class RequestPrescriptionState implements State{
    private final RequestPrescription requestPrescription;

    private final StateFactory stateFactory;

    public RequestPrescriptionState(RequestPrescription requestPrescription, StateFactory stateFactory) {
        this.requestPrescription = requestPrescription;
        this.stateFactory = stateFactory;
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException {
        StringBuilder result = new StringBuilder();
        if(command.isEmpty()) {
            commandLine.setResponse("Specify a message for the doctor");
        } else if(!command.equals("See response") && !command.equals("Home page")) {
            PrescriptionRequestBean bean = new PrescriptionRequestBean();
            bean.setMessage(command);
            requestPrescription.sendPrescriptionRequest(bean);
        } else if(command.equals("See response")) {
            Optional<List<ResponsePatientBean>> returnValue =  requestPrescription.getResponse();
            returnValue.orElseThrow().forEach(f -> result.append("Message : ").append(f.getMessage()).append(" Kind : ").append(f.getKind()).append(" Name : ").append(f.getName()).append(" Quantity : ").append(f.getQuantity()).append("\n"));
            commandLine.setResponse(result.toString());
        } else if(command.equals("Help")) {
            commandLine.setResponse("Specify a message for the doctor or enter the command 'See response'");
        } else if(command.equals("Home page")) {
            commandLine.setState(stateFactory.createUserHomePageState());
            commandLine.setResponse("On user home page");
        }
    }
}
