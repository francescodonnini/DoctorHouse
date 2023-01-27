package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> get(LoginRequestBean loginRequest) throws PersistentLayerException;
    void create(UserRegistrationRequestBean registrationRequest) throws DuplicateEmail, PersistentLayerException;
    Optional<User> getUser(UserBean userBean) throws PersistentLayerException;
    Optional<Doctor> getDoctor(DoctorBean doctorBean) throws PersistentLayerException;
    Optional<Doctor> getFamilyDoctor(DoctorBean doctorBean) throws PersistentLayerException;
}
