package ispw.uniroma2.doctorhouse.secondinterface.responserequest;

import ispw.uniroma2.doctorhouse.Logout;
import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.beans.PrescriptionBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.NotValidRequest;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import ispw.uniroma2.doctorhouse.secondinterface.CommandLine;
import ispw.uniroma2.doctorhouse.secondinterface.State;
import ispw.uniroma2.doctorhouse.secondinterface.StateFactory;

import java.util.List;
import java.util.Optional;

public class CreateDrugPrescriptionState implements State {
    private final ResponseRequest responseRequest;
    private final StateFactory stateFactory;
    private final UserBean loggedUser;

    private final Logout logout;

    private static final String COMMAND = "Welcome to RESPONSE REQUEST - possible command: " + "\n" +
            "1)show request" + "\n" +
            "2)response to -i requestId -m message -k kind" + "\n" +
            "3)logout";

    public CreateDrugPrescriptionState(ResponseRequest responseRequest, StateFactory stateFactory, UserBean loggedUser, Logout logout) {
        this.responseRequest = responseRequest;
        this.stateFactory = stateFactory;
        this.loggedUser = loggedUser;
        this.logout = logout;
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
    public void onEnter(CommandLine context) {
        // fill if necessary
    }

    //add home page and undo
    @Override
    public void enter(CommandLine commandLine, String command) throws PersistentLayerException, NotValidRequest {
        if(command.contains("-n") && command.contains("-q")) {
            PrescriptionBean prescriptionBean = new PrescriptionBean();
            prescriptionBean.setName(getName(command));
            prescriptionBean.setQuantity(getQuantity(command));
            responseRequest.insertResponse(ResponsePrescriptionState.responseBean, prescriptionBean);
            commandLine.setState(stateFactory.createResponsePrescriptionState(loggedUser, logout));
            commandLine.setState(stateFactory.createResponsePrescriptionState(loggedUser, logout));
            commandLine.setResponse(COMMAND);
        } else if(command.equals("see request")){
            StringBuilder builder = new StringBuilder();
            Optional<List<DoctorRequestBean>> bean = responseRequest.getRequest();
            if (bean.isPresent())
                bean.get().forEach(f -> builder.append("ID : ").append(f.getId()).append(" patient : ").append(f.getPatient()).append(" message : ").append(f.getMessage()).append("\n"));
            commandLine.setResponse(builder.toString());
        } else if(command.equals("home page")){
            commandLine.setState(stateFactory.createDoctorHomePageState(loggedUser));
        } else if(command.equals("logout")) {
            logout.destroySession();
            commandLine.setState(stateFactory.createLoginState());
        } else if(command.equals("undo")) {
            commandLine.setState(stateFactory.createResponsePrescriptionState(loggedUser, logout));
            commandLine.setResponse(COMMAND);
        } else if(command.equals("help")) {
            commandLine.setResponse("You chose to response to request number " + ResponsePrescriptionState.responseBean.getRequestId() + " with a drug prescription. Enter -n name and -q quantity" + "\n" +
            "If you want change your choose enter one of the following command: " + "\n" +
            "1)see request" + "\n" +
            "2)undo" + "\n" +
            "3)logout" + "\n" +
            "4)home page");
        } else {
            commandLine.setResponse("Insert a valid command");
        }
    }
}
