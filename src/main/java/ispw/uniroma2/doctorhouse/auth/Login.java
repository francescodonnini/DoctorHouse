package ispw.uniroma2.doctorhouse.auth;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.dao.UserDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Session;
import ispw.uniroma2.doctorhouse.model.User;

import java.util.Optional;

public class Login {
    private final UserDao dao;

    public Login(UserDao dao) {
        this.dao = dao;
    }

    public void login(LoginRequestBean loginRequest) throws UserNotFound, PersistentLayerException {
        Optional<User> user = dao.get(loginRequest);
        if (user.isEmpty()) {
            throw new UserNotFound();
        } else {
            Session.init((user.get()));
        }
    }
}
