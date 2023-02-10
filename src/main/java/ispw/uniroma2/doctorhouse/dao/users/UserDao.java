package ispw.uniroma2.doctorhouse.dao.users;

import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> get(LoginRequestBean loginRequest) throws PersistentLayerException;
    void create(UserRegistrationRequestBean registrationRequest) throws DuplicateEmail, PersistentLayerException;
    Optional<User> getUser(String email) throws PersistentLayerException;
    Optional<Doctor> getDoctor(String email) throws PersistentLayerException;
}
