package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

public class DoctorHomePageState implements State{

    private final StateFactory stateFactory;

    public DoctorHomePageState(StateFactory stateFactory) {
        this.stateFactory = stateFactory;
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException {
        if(command.equals("Response request")) {
            commandLine.setState(stateFactory.createResponsePrescriptionState());
            commandLine.setResponse("On response request - possible command: " + "\n" +
                    "1)Show request" + "\n" +
                    "2)Response to -i requestId -m message");
        } else if(command.equals("Request prescription")) {
            commandLine.setState(stateFactory.createRequestPrescriptionState());
            commandLine.setResponse("On request prescription page - Enter a message or the command 'See response'");
        }
    }
}
