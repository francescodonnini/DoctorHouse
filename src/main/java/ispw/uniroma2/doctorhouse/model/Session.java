package ispw.uniroma2.doctorhouse.model;

public class Session {
    private final User user;
    private static Session session = null;

    private Session(User user) {
        this.user = user;
    }

    public static void init(User user) {
        if (session == null)
            session = new Session(user);
    }

    public static Session getSession() {
        if(session == null) {
            throw new IllegalStateException();
        } else return session;
    }

    public User getUser() {
        return user;
    }

}
