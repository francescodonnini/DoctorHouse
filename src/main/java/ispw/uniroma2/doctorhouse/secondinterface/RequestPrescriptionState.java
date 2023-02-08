package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.Logout;
import ispw.uniroma2.doctorhouse.beans.DoctorBean;
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
    private final Logout logout;

    public RequestPrescriptionState(RequestPrescription requestPrescription, StateFactory stateFactory, UserBean loggedUser, Logout logout) {
        this.requestPrescription = requestPrescription;
        this.stateFactory = stateFactory;
        this.loggedUser = loggedUser;
        this.logout = logout;
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
        } else if(command.equals("see response")) {
            Optional<List<ResponsePatientBean>> returnValue =  requestPrescription.getResponse();
            returnValue.orElseThrow().forEach(f -> result.append("Message : ").append(f.getMessage()).append(" Kind : ").append(f.getKind()).append(" Name : ").append(f.getName()).append(" Quantity : ").append(f.getQuantity()).append("\n"));
            commandLine.setResponse(result.toString());
        } else if(command.equals("home page")) {
            if(loggedUser instanceof DoctorBean) {
                commandLine.setState(stateFactory.createDoctorHomePageState(loggedUser));
            } else commandLine.setState(stateFactory.createUserHomePageState(loggedUser));
        } else if(command.equals("help")) {
            commandLine.setResponse("Possible command : " + "\n" +
                    "1)see response" + "\n" +
                    "2)home page" + "\n" +
                    "3)logout"+ "\n" +
                    "4)otherwise the content is interpreted as a message"
            );
        } else if(command.equals("logout")) {
            logout.destroySession();
            commandLine.setState(stateFactory.createLoginState());
        } else requestPrescription.sendPrescriptionRequest(command);
    }
}
