package ispw.uniroma2.doctorhouse.secondinterface.responserequest;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.beans.ResponseBean;
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

    public ResponsePrescriptionState(ResponseRequest responseRequest, StateFactory stateFactory) {
        this.responseRequest = responseRequest;
        this.stateFactory = stateFactory;
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
    public void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException {
        StringBuilder builder = new StringBuilder();
        if (command.equals("Show request")) {
            Optional<List<DoctorRequestBean>> bean = responseRequest.getRequest();
            if (bean.isPresent())
                bean.get().forEach(f -> builder.append("ID : ").append(f.getId()).append(" patient : ").append(f.getPatient()).append(" message : ").append(f.getMessage()).append("\n"));
            commandLine.setResponse(builder.toString());
        } else if (command.contains("Response to") && command.contains("-i") && command.contains("-m") && command.contains("-k")) {
            responseBean.setRequestId(Integer.parseInt(getId(command)));
            responseBean.setMessage(getMessage(command));
            if (getKind(command).equals("Drug") || getKind(command).equals("D")) {
                commandLine.setState(stateFactory.drugPrescriptionState(responseRequest));
                commandLine.setResponse("You chose to response to request number " + responseBean.getRequestId() + " with a drug prescription. Enter -n name and -q quantity");
            } else if (getKind(command).equals("Visit") || getKind(command).equals("V")) {
                commandLine.setState(stateFactory.visitPrescriptionState(responseRequest));
                commandLine.setResponse("You chose to response to request number " + responseBean.getRequestId() + " with a visit prescription. Enter -n name");
            }
        } else if (command.equals("Help")) {
            commandLine.setResponse("On response request - possible command: " + "\n" +
                    "1)Show request" + "\n" +
                    "2)Response to -i requestId -m message" + "\n" +
                    "3)Home page");
        } else if(command.equals("Home page")) {
            commandLine.setState(stateFactory.createDoctorHomePageState());
        } else {
            commandLine.setResponse("Insert a valid command");
        }
    }
}

