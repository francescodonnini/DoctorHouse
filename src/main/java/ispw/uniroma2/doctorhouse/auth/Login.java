package ispw.uniroma2.doctorhouse.auth;

import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.users.UserDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.Session;
import ispw.uniroma2.doctorhouse.model.User;

import java.util.Optional;

public class Login {
    private final UserDao dao;

    public Login(UserDao dao) {
        this.dao = dao;
    }

    public UserBean login(LoginRequestBean loginRequest) throws UserNotFound, PersistentLayerException {
        Optional<User> optional = dao.get(loginRequest);
        if (optional.isEmpty()) {
            throw new UserNotFound();
        } else {
            User user = optional.get();
            Session.init(user);
            if (user instanceof Doctor) {
                return new DoctorBean((Doctor) user);
            }
            return new UserBean(user);
        }
    }
}
