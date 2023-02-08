package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.beans.PrescriptionRequestBean;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.ResponsePatientBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.requestprescription.RequestPrescription;

import java.util.List;
import java.util.Optional;


public class RequestPrescriptionState implements State{
    private final RequestPrescription requestPrescription;
    private final StateFactory stateFactory;
    private final UserBean loggedUser;

    public RequestPrescriptionState(RequestPrescription requestPrescription, StateFactory stateFactory, UserBean loggedUser) {
        this.requestPrescription = requestPrescription;
        this.stateFactory = stateFactory;
        this.loggedUser = loggedUser;
    }

    @Override
    public void onEnter(CommandLine context) {
        // fill if necessary
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws PersistentLayerException {
        StringBuilder result = new StringBuilder();
        if(command.isEmpty()) {
            commandLine.setResponse("Specify a message for the doctor");
        } else if(!command.equals("See response") && !command.equals("Home page") && !command.equals("Help")) {
            requestPrescription.sendPrescriptionRequest(command);
        } else if(command.equals("See response")) {
            Optional<List<ResponsePatientBean>> returnValue =  requestPrescription.getResponse();
            returnValue.orElseThrow().forEach(f -> result.append("Message : ").append(f.getMessage()).append(" Kind : ").append(f.getKind()).append(" Name : ").append(f.getName()).append(" Quantity : ").append(f.getQuantity()).append("\n"));
            commandLine.setResponse(result.toString());
        } else if(command.equals("Home page")) {
            commandLine.setState(stateFactory.createUserHomePageState(loggedUser));
            commandLine.setResponse("On home page");
        } else if(command.equals("Help")) {
            commandLine.setResponse("Possible command : " + "\n" +
                    "1)See response" + "\n" +
                    "2)Home page" + "\n" +
                    "3)Request prescription" + "\n"
            );
        }
    }
}
