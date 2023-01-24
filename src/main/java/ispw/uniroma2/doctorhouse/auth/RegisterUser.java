package ispw.uniroma2.doctorhouse.auth;

import ispw.uniroma2.doctorhouse.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.dao.UserDao;

public class RegisterUser {
    private final UserDao dao;

    public RegisterUser(UserDao dao) {
        this.dao = dao;
    }

    public void register(UserRegistrationRequestBean request) throws DuplicateEmail {
        dao.create(request);
    }
}
