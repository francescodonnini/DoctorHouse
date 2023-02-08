package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.Logout;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

public class DoctorHomePageState implements State{
    private static final String COMMANDS = "incoming\nlogout\npending\nshow request\nrequest prescription\nresponse request\n";
    private final StateFactory stateFactory;
    private final Logout logout;
    private final UserBean loggedUser;

    public DoctorHomePageState(StateFactory stateFactory, UserBean loggedUser) {
        this.stateFactory = stateFactory;
        this.loggedUser = loggedUser;
        logout = new Logout();
    }

    @Override
    public void onEnter(CommandLine context) {
        context.setResponse("Welcome to the DOCTOR HOME PAGE\nType help for info\n");
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws PersistentLayerException {
        command = command.trim();
        switch (command) {
            case "response request":
                commandLine.setState(stateFactory.createResponsePrescriptionState(loggedUser, logout));
                commandLine.setResponse("Welcome to RESPONSE REQUEST - possible command: " + "\n" +
                        "1)show request" + "\n" +
                        "2)response to -i requestId -m message -k kind" + "\n" +
                        "3)logout"
                );
                break;
            case "logout":
                logout.destroySession();
                commandLine.setState(stateFactory.createLoginState());
                break;
            case "request prescription":
                commandLine.setState(stateFactory.createRequestPrescriptionState(loggedUser, logout));
                commandLine.setResponse("WELCOME TO REQUEST PRESCRIPTION HOME PAGE - possible command :" + "\n" +
                        "1)see response" + "\n" +
                        "2)home page" + "\n" +
                        "3)logout" + "\n" +
                        "4)otherwise the content is interpreted as a message");
                break;
            case "incoming":
                commandLine.setState(stateFactory.createAskState(loggedUser));
                break;
            case "pending":
                commandLine.setState(stateFactory.createDoRearrange(loggedUser));
                break;
            case "help":
                commandLine.setResponse(COMMANDS);
                break;
            default:
                commandLine.setResponse("invalid command " + command);
                break;
        }
    }
}
