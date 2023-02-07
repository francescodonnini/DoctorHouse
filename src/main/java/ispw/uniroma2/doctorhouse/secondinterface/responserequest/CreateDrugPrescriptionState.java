package ispw.uniroma2.doctorhouse.secondinterface.responserequest;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.DrugPrescriptionBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import ispw.uniroma2.doctorhouse.secondinterface.CommandLine;
import ispw.uniroma2.doctorhouse.secondinterface.State;
import ispw.uniroma2.doctorhouse.secondinterface.StateFactory;

public class CreateDrugPrescriptionState implements State {
    private final ResponseRequest responseRequest;
    private final StateFactory stateFactory;

    public CreateDrugPrescriptionState(ResponseRequest responseRequest, StateFactory stateFactory) {
        this.responseRequest = responseRequest;
        this.stateFactory = stateFactory;
    }

    private String getName(String command) {
        String name = "";
        for(int i = command.indexOf("-n"); i<command.indexOf("-q"); i++)
            if ((command.charAt(i) != '-') && !(command.charAt(i) == 'n' && command.charAt(i - 1) == '-'))
                name = name.concat(String.valueOf(command.charAt(i)));
        return name;
    }

    private int getQuantity(String command) {
        String quantity = "";
        for(int i = command.indexOf("-q"); i<command.length(); i++)
            if ((command.charAt(i) != '-') && !(command.charAt(i) == 'q' && command.charAt(i - 1) == '-'))
                quantity = quantity.concat(String.valueOf(command.charAt(i)));
        return Integer.parseInt(quantity.trim());
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException {
        if(command.contains("-n") && command.contains("-q")) {
            DrugPrescriptionBean drugPrescriptionBean = new DrugPrescriptionBean();
            drugPrescriptionBean.setName(getName(command));
            drugPrescriptionBean.setQuantity(getQuantity(command));
            responseRequest.insertDrugPrescriptionResponse(ResponsePrescriptionState.responseBean, drugPrescriptionBean);
            commandLine.setState(stateFactory.createResponsePrescriptionState());
            commandLine.setResponse("On response request - possible command: Show request, Response to -i requestId -m message -k Kind");
        } else {
            commandLine.setResponse("Insert a valid command");
        }
    }
}
