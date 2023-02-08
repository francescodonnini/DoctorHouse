package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

public interface State {
    void onEnter(CommandLine context);
    void enter(CommandLine context, String command) throws PersistentLayerException;
}
