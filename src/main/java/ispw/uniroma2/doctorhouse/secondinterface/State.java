package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

public interface State {

    void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException;
}
