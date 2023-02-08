package ispw.uniroma2.doctorhouse.secondinterface.responserequest;

import ispw.uniroma2.doctorhouse.Logout;
import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.beans.VisitPrescriptionBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import ispw.uniroma2.doctorhouse.secondinterface.CommandLine;
import ispw.uniroma2.doctorhouse.secondinterface.State;
import ispw.uniroma2.doctorhouse.secondinterface.StateFactory;

import java.util.List;
import java.util.Optional;

public class CreateVisitPrescriptionState implements State {

    private final ResponseRequest responseRequest;
    private final StateFactory stateFactory;
    private final UserBean loggedUser;
    private final Logout logout;

    public CreateVisitPrescriptionState(ResponseRequest responseRequest, StateFactory stateFactory, UserBean loggedUser, Logout logout) {
        this.responseRequest = responseRequest;
        this.stateFactory = stateFactory;
        this.loggedUser = loggedUser;
        this.logout = logout;
    }

    private String getName(String command) {
        String name = "";
        for (int i = command.indexOf("-n"); i < command.length(); i++)
            if ((command.charAt(i) != '-') && !(command.charAt(i) == 'n' && command.charAt(i - 1) == '-'))
                name = name.concat(String.valueOf(command.charAt(i)));
        return name.trim();
    }

    @Override
    public void onEnter(CommandLine context) {
        // fill if necessary
    }

    //add home page and undo
    @Override
    public void enter(CommandLine commandLine, String command) throws PersistentLayerException {
        if(command.contains("-n")) {
            VisitPrescriptionBean visitPrescriptionBean = new VisitPrescriptionBean();
            visitPrescriptionBean.setName(getName(command));
            responseRequest.insertVisitPrescriptionResponse(ResponsePrescriptionState.responseBean, visitPrescriptionBean);
            commandLine.setState(stateFactory.createResponsePrescriptionState(loggedUser, logout));
            commandLine.setResponse("WELCOME TO RESPONSE REQUEST PAGE- possible command: " + "\n" +
                    "1)show request " + "\n" +
                    "2)response to -i requestId -m message" + "\n" +
                    "3)home page" +"\n" +
                    "4)logout");
        } else if(command.equals("see request")) {
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
            commandLine.setResponse("Welcome to RESPONSE REQUEST - possible command: " + "\n" +
                    "1)show request" + "\n" +
                    "2)response to -i requestId -m message -k kind" + "\n" +
                    "3)logout"
            );
        } else if(command.equals("help")) {
            commandLine.setResponse("You chose to response to request number " + ResponsePrescriptionState.responseBean.getRequestId() + " with a visit prescription. Enter -n name " + "\n" +
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
