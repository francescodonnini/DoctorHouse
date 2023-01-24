package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> get(LoginRequestBean loginRequest) throws UserNotFound;
    void create(UserRegistrationRequestBean registrationRequest) throws DuplicateEmail;
}
