package ispw.uniroma2.doctorhouse.auth.registration;

import ispw.uniroma2.doctorhouse.auth.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.dao.UserDao;

public class RegisterUser {
    private final UserDao dao;

    public RegisterUser(UserDao dao) {
        this.dao = dao;
    }

    void register(UserRegistrationRequestBean request) throws Exception {
        if (!dao.create(request)) {
            throw new Exception();
        }
    }
}
