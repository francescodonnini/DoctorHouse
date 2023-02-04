package ispw.uniroma2.doctorhouse;


import ispw.uniroma2.doctorhouse.model.Session;

public class Logout {
    public void destroySession() {
        Session.invalidateSession();
    }
}
