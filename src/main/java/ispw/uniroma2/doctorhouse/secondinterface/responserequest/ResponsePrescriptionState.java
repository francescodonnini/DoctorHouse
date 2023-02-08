package ispw.uniroma2.doctorhouse.secondinterface.responserequest;

import ispw.uniroma2.doctorhouse.Logout;
import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.beans.ResponseBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import ispw.uniroma2.doctorhouse.secondinterface.CommandLine;
import ispw.uniroma2.doctorhouse.secondinterface.State;
import ispw.uniroma2.doctorhouse.secondinterface.StateFactory;

import java.util.List;
import java.util.Optional;

public class ResponsePrescriptionState implements State {
    private final ResponseRequest responseRequest;
    private final StateFactory stateFactory;
    protected static ResponseBean responseBean = new ResponseBean();
    private final UserBean loggedUser;

    private final Logout logout;

    public ResponsePrescriptionState(ResponseRequest responseRequest, StateFactory stateFactory, UserBean loggedUser, Logout logout) {
        this.responseRequest = responseRequest;
        this.stateFactory = stateFactory;
        this.loggedUser = loggedUser;
        this.logout = logout;
    }


    private String getId(String command) {
        String id = "";
        for (int i = command.indexOf("-i"); i < command.indexOf("-m"); i++)
            if ((command.charAt(i) != '-') && !(command.charAt(i) == 'i' && command.charAt(i - 1) == '-'))
                id = id.concat(String.valueOf(command.charAt(i)));
        return id.trim();
    }

    private String getMessage(String command) {
        String message = "";
        for (int i = command.indexOf("-m"); i < command.indexOf("-k"); i++)
            if ((command.charAt(i) != '-') && !(command.charAt(i) == 'm' && command.charAt(i - 1) == '-'))
                message = message.concat(String.valueOf(command.charAt(i)));
        return message.trim();
    }

    private String getKind(String command) {
        String kind = "";
        for (int i = command.indexOf("-k"); i < command.length(); i++)
            if ((command.charAt(i) != '-') && !(command.charAt(i) == 'k' && command.charAt(i - 1) == '-'))
                kind = kind.concat(String.valueOf(command.charAt(i)));
        return kind.trim();
    }

    @Override
    public void onEnter(CommandLine context) {
        // fill if necessary
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws PersistentLayerException {
        StringBuilder builder = new StringBuilder();
        if (command.equals("show request")) {
            Optional<List<DoctorRequestBean>> bean = responseRequest.getRequest();
            bean.ifPresent(doctorRequestBeans -> doctorRequestBeans.forEach(f -> builder.append("ID : ").append(f.getId()).append(" patient : ").append(f.getPatient()).append(" message : ").append(f.getMessage()).append("\n")));
            commandLine.setResponse(builder.toString());
        } else if (command.startsWith("response to") && command.contains("-i") && command.contains("-m") && command.contains("-k")) {
            responseBean.setRequestId(Integer.parseInt(getId(command)));
            responseBean.setMessage(getMessage(command));
            if (getKind(command).equals("drug")) {
                commandLine.setState(stateFactory.drugPrescriptionState(responseRequest, loggedUser, logout));
                commandLine.setResponse("You chose to response to request number " + responseBean.getRequestId() + " with a drug prescription. Enter -n name and -q quantity");
            } else if (getKind(command).equals("visit")) {
                commandLine.setState(stateFactory.visitPrescriptionState(responseRequest, loggedUser, logout));
                commandLine.setResponse("You chose to response to request number " + responseBean.getRequestId() + " with a visit prescription. Enter -n name");
            } else if(getKind(command).equals("reject")) {
                responseBean.setRequestId(Integer.parseInt(getId(command)));
                responseBean.setMessage(getMessage(command));
                responseRequest.insertRejection(responseBean);
            }
        } else if (command.equals("help")) {
            commandLine.setResponse("WELCOME TO RESPONSE REQUEST PAGE - possible command: " + "\n" +
                    "1)show request" + "\n" +
                    "2)response to -i requestId -m message" + "\n" +
                    "3)home page");
        } else if(command.equals("home page")) {
            commandLine.setState(stateFactory.createDoctorHomePageState(loggedUser));
        } else if(command.equals("logout")) {
            logout.destroySession();
            commandLine.setState(stateFactory.createLoginState());
        } else {
            commandLine.setResponse("Insert a valid command");
        }
    }
}

