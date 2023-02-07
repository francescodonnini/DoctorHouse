package ispw.uniroma2.doctorhouse.secondinterface.responserequest;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.VisitPrescriptionBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import ispw.uniroma2.doctorhouse.secondinterface.CommandLine;
import ispw.uniroma2.doctorhouse.secondinterface.State;
import ispw.uniroma2.doctorhouse.secondinterface.StateFactory;

public class CreateVisitPrescriptionState implements State {

    private final ResponseRequest responseRequest;
    private final StateFactory stateFactory;

    public CreateVisitPrescriptionState(ResponseRequest responseRequest, StateFactory stateFactory) {
        this.responseRequest = responseRequest;
        this.stateFactory = stateFactory;
    }

    private String getName(String command) {
        String name = "";
        for (int i = command.indexOf("-n"); i < command.length(); i++)
            if ((command.charAt(i) != '-') && !(command.charAt(i) == 'n' && command.charAt(i - 1) == '-'))
                name = name.concat(String.valueOf(command.charAt(i)));
        return name.trim();
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException {
        if(command.contains("-n")) {
            VisitPrescriptionBean visitPrescriptionBean = new VisitPrescriptionBean();
            visitPrescriptionBean.setName(getName(command));
            responseRequest.insertVisitPrescriptionResponse(ResponsePrescriptionState.responseBean, visitPrescriptionBean);
            commandLine.setState(stateFactory.createResponsePrescriptionState());
            commandLine.setResponse("On response request - possible command: Show request, Response to -i requestId -m message");
        } else {
            commandLine.setResponse("Insert a valid command");
        }
    }
}
