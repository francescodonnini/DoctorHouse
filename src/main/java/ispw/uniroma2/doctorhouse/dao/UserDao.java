package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> get(LoginRequestBean loginRequest) throws PersistentLayerException;
    void create(UserRegistrationRequestBean registrationRequest) throws DuplicateEmail, PersistentLayerException;
    Optional<User> getUser(UserBean userBean) throws PersistentLayerException;
    Optional<Doctor> getDoctor(DoctorBean doctorBean) throws PersistentLayerException;
    List<Doctor> getByField(String field) throws PersistentLayerException;
}
