package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.model.*;
import ispw.uniroma2.doctorhouse.model.Doctor;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class UserDatabase implements UserDao {
    private final Connection connection;
    private final OfficeDao officeDao;

    public UserDatabase(Connection connection, OfficeDao officeDao) {
        this.connection = connection;
        this.officeDao = officeDao;
    }


    private Optional<Doctor> getFamilyDoctor(DoctorBean doctor) {
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
            throw new IrrecoverableError(e);
        }
    }

    @Override
    public Optional<User> get(LoginRequestBean loginRequest) throws UserNotFound {
        try (PreparedStatement statement = connection.prepareStatement("CALL login(?, ?);")) {
            statement.setString(1, loginRequest.getEmail());
            statement.setString(2, loginRequest.getPassword());
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                LocalDate birthDate = rs.getDate(1).toLocalDate();
                String fiscalCode = rs.getString(2);
                String firstName = rs.getString(3);
                String email = rs.getString(4);
                Optional<Gender> gender = Gender.from(rs.getInt(5));
                String lastName = rs.getString(6);
                String field = rs.getString(8);
                Person person = new Person(birthDate, fiscalCode, firstName, lastName, gender.orElse(Gender.NOT_KNOWN));
                String familyDoctorEmail = rs.getString(7);
                DoctorBeanImpl familyDoctorBean = new DoctorBeanImpl();
                familyDoctorBean.setEmail(familyDoctorEmail);
                Doctor familyDoctor = getFamilyDoctor(familyDoctorBean).orElse(null);
                if (field == null) {
                    return Optional.of(new User(email, person, familyDoctor));
                } else {
                    DoctorBeanImpl doctor = new DoctorBeanImpl();
                    List<Office> offices = officeDao.getOffices(doctor);
                    return Optional.of(new Doctor(email, person, familyDoctor, field, offices));
                }
            }
            throw new UserNotFound();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public void create(UserRegistrationRequestBean request) throws DuplicateEmail {
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
            throw new IrrecoverableError(e);
        }
    }
}
