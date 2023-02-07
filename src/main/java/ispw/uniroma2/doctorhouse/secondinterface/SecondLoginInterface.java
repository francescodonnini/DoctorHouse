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
    public void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException {
        String email = "";
        String password = "";
        if(command.equals("Help")) {
            commandLine.setResponse("possible commands : " + "Login -u Email -p Password");
        } else if( !command.contains("Login") && !command.contains("-u") && !command.contains("-p")) {
            commandLine.setResponse("Insert a valid command");
        } else {
            email = getEmail(command);
            password = getPassword(command);
        }

        LoginRequestBean loginRequestBean = new LoginRequestBean();
        loginRequestBean.setEmail(email.trim());
        loginRequestBean.setPassword(password.trim());
        try {
            UserBean userBean = login.login(loginRequestBean);
            if(userBean instanceof DoctorBean) {
                commandLine.setState(stateFactory.createDoctorHomePageState());
                commandLine.setResponse("On doctor home page - you can enter one of the following command :" + "\n" +
                        " 1)Response request" + "\n" +
                        "2)Request prescription");
            } else if (userBean instanceof UserBean) {
                commandLine.setState(stateFactory.createUserHomePageState());
                commandLine.setResponse("On user home page - you can enter one of the following command 1)Request prescription");
            }
        } catch (UserNotFound u){
            throw new UserNotFound();
        } catch (PersistentLayerException p) {
            throw new PersistentLayerException(p);
        }
    }
}

