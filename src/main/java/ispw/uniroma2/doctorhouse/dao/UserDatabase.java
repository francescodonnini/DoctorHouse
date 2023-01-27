package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.*;
import ispw.uniroma2.doctorhouse.model.Doctor;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class UserDatabase implements UserDao {
    private final Connection connection;
    private final OfficeDao officeDao;

    public UserDatabase(Connection connection, OfficeDao officeDao) {
        this.connection = connection;
        this.officeDao = officeDao;
    }

    @Override
    public Optional<Doctor> getFamilyDoctor(DoctorBean doctor) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL searchDoctor(?);")) {
            statement.setString(1, doctor.getEmail());
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    LocalDate birthDate = resultSet.getDate(1).toLocalDate();
                    String fiscalCode = resultSet.getString(2);
                    String firstName = resultSet.getString(3);
                    Gender gender = Gender.from(resultSet.getInt(4)).orElse(Gender.NOT_KNOWN);
                    String lastName = resultSet.getString(5);
                    String field = resultSet.getString(6);
                    Person person = new Person(birthDate, fiscalCode, firstName, lastName, gender);
                    List<Office> offices = officeDao.getOffices(doctor);
                    return Optional.of(new Doctor(doctor.getEmail(), person, null, field, offices));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Optional<User> get(LoginRequestBean loginRequest) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL login(?, ?);")) {
            statement.setString(1, loginRequest.getEmail());
            statement.setString(2, loginRequest.getPassword());
            statement.execute();
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    LocalDate birthDate = resultSet.getDate(1).toLocalDate();
                    String fiscalCode = resultSet.getString(2);
                    String firstName = resultSet.getString(3);
                    String lastName = resultSet.getString(6);
                    Gender gender = Gender.from(resultSet.getInt(5)).orElse(Gender.NOT_KNOWN);
                    DoctorBeanImpl familyDoctorBean = new DoctorBeanImpl();
                    familyDoctorBean.setEmail(resultSet.getString(7));
                    Optional<Doctor> familyDoctor = getFamilyDoctor(familyDoctorBean);
                    Person person = new Person(birthDate, fiscalCode, firstName, lastName, gender);
                    String field = resultSet.getString(8);
                    if (field == null) {
                        return Optional.of(new User(loginRequest.getEmail(), person, familyDoctor.orElse(null)));
                    } else {
                        DoctorBeanImpl doctorBean = new DoctorBeanImpl();
                        doctorBean.setEmail(loginRequest.getEmail());
                        List<Office> offices = officeDao.getOffices(doctorBean);
                        return Optional.of(new Doctor(loginRequest.getEmail(), person, familyDoctor.orElse(null), field, offices));
                    }
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public void create(UserRegistrationRequestBean request) throws DuplicateEmail, PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL register_as_patient(?, ?, ?, ?, ?, ?, ?, ?);")) {
            statement.setDate(1, Date.valueOf(request.getBirthDate()));
            statement.setString(2, request.getFirstName());
            statement.setString(3, request.getLastName());
            statement.setInt(4, request.getGender().isoCode);
            statement.setString(5, request.getEmail());
            statement.setString(6, request.getPassword());
            statement.setString(7, request.getFiscalCode());
            Optional<DoctorBeanImpl> familyDoctor = request.getFamilyDoctor();
            if (familyDoctor.isEmpty()) {
                statement.setNull(8, Types.VARCHAR);
            } else {
                statement.setString(8, familyDoctor.get().getEmail());
            }
            statement.execute();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateEmail();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Optional<Doctor> getDoctor(DoctorBean doctorBean) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getDoctor(?);")) {
            statement.setString(1, doctorBean.getEmail());
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    LocalDate birthDate = resultSet.getDate(1).toLocalDate();
                    String fiscalCode = resultSet.getString(2);
                    String firstName = resultSet.getString(3);
                    String lastName = resultSet.getString(4);
                    Gender gender = Gender.from(resultSet.getInt(5)).orElse(Gender.NOT_KNOWN);
                    DoctorBeanImpl familyDoctorBean = new DoctorBeanImpl();
                    familyDoctorBean.setEmail(resultSet.getString(6));
                    Optional<Doctor> familyDoctor = getFamilyDoctor(familyDoctorBean);
                    String field = resultSet.getString(7);
                    List<Office> offices = officeDao.getOffices(doctorBean);
                    Person person = new Person(birthDate, fiscalCode, firstName, lastName, gender);
                    return Optional.of(new Doctor(doctorBean.getEmail(), person, familyDoctor.orElse(null), field, offices));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Optional<User> getUser(UserBean userBean) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getUser(?);")) {
            statement.setString(1, userBean.getEmail());
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    LocalDate birthDate = resultSet.getDate(1).toLocalDate();
                    String fiscalCode = resultSet.getString(2);
                    String firstName = resultSet.getString(3);
                    String lastName = resultSet.getString(4);
                    Gender gender = Gender.from(resultSet.getInt(5)).orElse(Gender.NOT_KNOWN);
                    DoctorBeanImpl familyDoctorBean = new DoctorBeanImpl();
                    familyDoctorBean.setEmail(resultSet.getString(6));
                    Optional<Doctor> familyDoctor = getFamilyDoctor(familyDoctorBean);
                    Person person = new Person(birthDate, fiscalCode, firstName, lastName, gender);
                    return Optional.of(new User(userBean.getEmail(), person, familyDoctor.orElse(null)));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }
}
