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
import java.util.ArrayList;
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
    public Optional<User> get(LoginRequestBean loginRequest) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL login(?, ?);")) {
            statement.setString(1, loginRequest.getEmail());
            statement.setString(2, loginRequest.getPassword());
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    DoctorBean familyDoctorBean = new DoctorBean();
                    familyDoctorBean.setEmail(resultSet.getString(6));
                    Doctor familyDoctor = getDoctor(familyDoctorBean).orElse(null);
                    String field = resultSet.getString(7);
                    if (field == null) {
                        return Optional.of(new User(loginRequest.getEmail(), fromResultSet(resultSet), familyDoctor));
                    } else {
                        DoctorBean doctorBean = new DoctorBean();
                        doctorBean.setEmail(loginRequest.getEmail());
                        List<Office> offices = officeDao.getOffices(doctorBean);
                        return Optional.of(new Doctor(loginRequest.getEmail(), fromResultSet(resultSet), familyDoctor, field, offices));
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
        try (PreparedStatement statement = connection.prepareStatement("CALL registerAsPatient(?, ?, ?, ?, ?, ?, ?, ?);")) {
            statement.setDate(1, Date.valueOf(request.getBirthDate()));
            statement.setString(2, request.getFirstName());
            statement.setString(3, request.getLastName());
            statement.setInt(4, request.getGender().isoCode);
            statement.setString(5, request.getEmail());
            statement.setString(6, request.getPassword());
            statement.setString(7, request.getFiscalCode());
            Optional<DoctorBean> familyDoctor = request.getFamilyDoctor();
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
                    String field = resultSet.getString(6);
                    List<Office> offices = officeDao.getOffices(doctorBean);
                    return Optional.of(new Doctor(doctorBean.getEmail(), fromResultSet(resultSet), null, field, offices));
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
                    return Optional.of(new User(userBean.getEmail(), fromResultSet(resultSet), null));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public List<Doctor> getByField(String field) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getByField(?);")) {
            statement.setString(1, field);
            if (statement.execute()) {
                List<Doctor> doctors = new ArrayList<>();
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String email = resultSet.getString(6);
                    DoctorBean doctorBean = new DoctorBean();
                    doctorBean.setEmail(email);
                    List<Office> offices = officeDao.getOffices(doctorBean);
                    doctors.add(new Doctor(email, fromResultSet(resultSet), null, field, offices));
                }
                return doctors;
            }
            return List.of();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    private Person fromResultSet(ResultSet resultSet) throws SQLException {
        LocalDate birthDate = resultSet.getDate(1).toLocalDate();
        String fiscalCode = resultSet.getString(2);
        String firstName = resultSet.getString(3);
        String lastName = resultSet.getString(4);
        Gender gender = Gender.from(resultSet.getInt(5)).orElse(Gender.NOT_KNOWN);
        return new Person(birthDate, fiscalCode, firstName, lastName, gender);
    }
}
