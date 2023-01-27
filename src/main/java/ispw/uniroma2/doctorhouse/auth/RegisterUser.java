package ispw.uniroma2.doctorhouse.auth;

import ispw.uniroma2.doctorhouse.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.dao.UserDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

public class RegisterUser {
    private final UserDao dao;

    public RegisterUser(UserDao dao) {
        this.dao = dao;
    }

    public void register(UserRegistrationRequestBean request) throws DuplicateEmail, PersistentLayerException {
        dao.create(request);
    }
}
