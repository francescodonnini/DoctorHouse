package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.Logout;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

public class UserHomePageState implements State {
    private static final String HELP_COMMANDS = "incoming\npending\nrefresh\nrequest prescription\nlogout\n";
    private final StateFactory stateFactory;
    private final UserBean loggedUser;
    private final Logout logout;

    public UserHomePageState(StateFactory stateFactory, UserBean loggedUser) {
        this.stateFactory = stateFactory;
        this.loggedUser = loggedUser;
        logout = new Logout();
    }

    @Override
    public void onEnter(CommandLine commandLine) {
        commandLine.setResponse("Welcome to the PATIENT HOMEPAGE\nType help to see usages\n");
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws PersistentLayerException {
        switch (command) {
            case "request prescription":
                commandLine.setResponse("WELCOME TO REQUEST PRESCRIPTION HOME PAGE - possible command :" + "\n" +
                        "1)see response" + "\n" +
                        "2)home page" + "\n" +
                        "3)logout" + "\n" +
                        "4)otherwise the content is interpreted as a message");
                commandLine.setState(stateFactory.createRequestPrescriptionState(loggedUser, logout));
                break;
            case "help":
                commandLine.setResponse(HELP_COMMANDS);
                break;
            case "incoming":
                commandLine.setState(stateFactory.createAskState(loggedUser));
                break;
            case "pending":
                commandLine.setState(stateFactory.createDoRearrange(loggedUser));
                break;
            case "logout":
                logout.destroySession();
                commandLine.setState(stateFactory.createLoginState());
                break;
            default:
                commandLine.setResponse("invalid command " + command);
                break;
        }
    }
}
