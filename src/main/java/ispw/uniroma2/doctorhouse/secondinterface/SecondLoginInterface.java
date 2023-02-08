package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.auth.Login;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;


public class SecondLoginInterface implements State {
    private final Login login;

    private final StateFactory stateFactory;

    public SecondLoginInterface(Login login, StateFactory stateFactory) {
        this.login = login;
        this.stateFactory = stateFactory;
    }

    private String getEmail(String command) {
        String email = "";
        for(int i = command.indexOf("-u"); i < command.length(); i++)
            if ((command.charAt(i) != '-') && !(command.charAt(i) == 'u' && command.charAt(i - 1) == '-') && i < command.indexOf("-p"))
                email = email.concat(String.valueOf(command.charAt(i)));
        return email;
    }

    private String getPassword(String command) {
        String password = "";
        for(int i = command.indexOf("-p"); i < command.length(); i++)
            if ((command.charAt(i) != '-') && !(command.charAt(i) == 'p' && command.charAt(i - 1) == '-') && i > command.indexOf("-p"))
                password = password.concat(String.valueOf(command.charAt(i)));
        return password;
    }

    @Override
    public void onEnter(CommandLine commandLine) {
        commandLine.setResponse("Welcome to LOGIN PAGE. Type help to see usages.\n");
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws PersistentLayerException {
        command = command.trim();
        if(command.equals("help")) {
            commandLine.setResponse("login -u email -p password\n");
        } else if(command.startsWith("login")) {
            String email = getEmail(command);
            String password = getPassword(command);
            tryLogin(commandLine, email, password);
        } else {
            commandLine.setResponse("invalid command " + command);
        }
    }

    private void tryLogin(CommandLine context, String email, String password) {
        LoginRequestBean loginRequestBean = new LoginRequestBean();
        loginRequestBean.setEmail(email.trim());
        loginRequestBean.setPassword(password.trim());
        try {
            UserBean userBean = login.login(loginRequestBean);
            if(userBean instanceof DoctorBean) {
                context.setState(stateFactory.createDoctorHomePageState(userBean));
            } else if (userBean != null) {
                context.setState(stateFactory.createUserHomePageState(userBean));
            }
        } catch (UserNotFound u) {
            context.setResponse("user not found");
        } catch (PersistentLayerException p) {
            context.setResponse("impossible to finish the request, please try again later");
        }
    }
}
