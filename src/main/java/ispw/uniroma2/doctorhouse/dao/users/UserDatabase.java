package ispw.uniroma2.doctorhouse.dao.users;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
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
    public Optional<User> get(LoginRequestBean loginRequest) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL login(?, ?);")) {
            statement.setString(1, loginRequest.getEmail());
            statement.setString(2, loginRequest.getPassword());
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    Doctor familyDoctor = getDoctor(resultSet.getString(6)).orElse(null);
                    String field = resultSet.getString(7);
                    if (field == null) {
                        return Optional.of(new User(loginRequest.getEmail(), fromResultSet(resultSet), familyDoctor));
                    } else {
                        List<Office> offices = officeDao.getOffices(loginRequest.getEmail());
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
    public Optional<Doctor> getDoctor(String email) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getDoctor(?);")) {
            statement.setString(1, email);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    String field = resultSet.getString(6);
                    List<Office> offices = officeDao.getOffices(email);
                    return Optional.of(new Doctor(email, fromResultSet(resultSet), null, field, offices));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Optional<User> getUser(String email) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getUser(?);")) {
            statement.setString(1, email);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    return Optional.of(new User(email, fromResultSet(resultSet), null));
                }
            }
            return Optional.empty();
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
