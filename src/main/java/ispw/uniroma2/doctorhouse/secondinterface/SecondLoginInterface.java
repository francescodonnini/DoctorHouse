package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.auth.Login;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

import java.util.Optional;


public class SecondLoginInterface implements State {

    private final Login login;

    public SecondLoginInterface(Login login) {
        this.login = login;
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
        for(int i = command.indexOf("-u"); i < command.length(); i++)
            if ((command.charAt(i) != '-') && !(command.charAt(i) == 'p' && command.charAt(i - 1) == '-') && i > command.indexOf("-p"))
                password = password.concat(String.valueOf(command.charAt(i)));
        return password;
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException {
        String email = "";
        String password = "";
        if( ! command.contains("Login") && !command.contains("-u") && !command.contains("-p")) {
            commandLine.setResponse(Optional.of("Insert a valid command"));
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
                commandLine.setState(new DoctorHomePageState());
            } else if (userBean instanceof UserBean) {
                commandLine.setState(new UserHomePageState());
            }
        } catch (UserNotFound u){
            throw new UserNotFound();
        } catch (PersistentLayerException p) {
            throw new PersistentLayerException(p);
        }
    }
}

