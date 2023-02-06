package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

public class DoctorHomePageState implements State{

    private final StateFactory stateFactory;

    public DoctorHomePageState(StateFactory stateFactory) {
        this.stateFactory = stateFactory;
    }

    @Override
    public void enter(CommandLine commandLine, String command) throws UserNotFound, PersistentLayerException {
        //
    }
}
