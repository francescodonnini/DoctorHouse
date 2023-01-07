package ispw.uniroma2.doctorhouse.auth.registration;

import ispw.uniroma2.doctorhouse.auth.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.dao.UserDao;

public class RegisterUser {
    private final UserDao dao;

    public RegisterUser(UserDao dao) {
        this.dao = dao;
    }

    boolean register(UserRegistrationRequestBean request) throws DuplicateEmail {
        return dao.create(request);
    }
}
