package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.auth.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.auth.beans.UserRegistrationRequestBean;

import java.util.Optional;

public interface UserDao {
    Optional<User> get(LoginRequestBean loginRequest);
    boolean create(UserRegistrationRequestBean registrationRequest);
}
