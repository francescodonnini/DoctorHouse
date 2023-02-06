package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;


import java.util.Optional;

public class UserHomePageState implements State {

    @Override
    public void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException {
        commandLine.setResponse(Optional.of("On user home page"));
        if(command.contains("Request")) {
           commandLine.setResponse(Optional.of("On request prescription page - Enter a message or the command 'See response'"));
        }
    }
}
