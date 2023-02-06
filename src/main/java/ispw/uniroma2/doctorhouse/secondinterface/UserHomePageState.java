package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;


public class UserHomePageState implements State {

    private final StateFactory stateFactory;
    public UserHomePageState(StateFactory stateFactory) {
        this.stateFactory = stateFactory;
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException {
        if(command.equals("Request prescription")) {
           commandLine.setResponse("On request prescription page - Enter a message or the command 'See response'");
           commandLine.setState(stateFactory.createRequestPrescriptionState());
        } else if(command.equals("Help")) {
            commandLine.setResponse("possible commands: " + "Request prescription");
        }
    }
}
