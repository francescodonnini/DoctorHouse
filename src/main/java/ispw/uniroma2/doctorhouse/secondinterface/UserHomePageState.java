package ispw.uniroma2.doctorhouse.secondinterface;

public class UserHomePageState implements State {

    private final StateFactory stateFactory;
    public UserHomePageState(StateFactory stateFactory) {
        this.stateFactory = stateFactory;
    }

    @Override
    public void enter(CommandLine commandLine, String command) {
        if(command.equals("Request prescription")) {
           commandLine.setResponse("On request prescription page - Enter a message or the command 'See response'");
           commandLine.setState(stateFactory.createRequestPrescriptionState());
        } else if(command.equals("Help")) {
            commandLine.setResponse("On user home page - possible commands: " + "Request prescription");
        } else commandLine.setResponse("Insert a valid command");
    }
}
