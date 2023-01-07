package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.auth.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.auth.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.model.Gender;
import ispw.uniroma2.doctorhouse.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

public class UserDatabase implements UserDao {
    private static UserDatabase instance;
    private final Connection connection;

    private UserDatabase(Connection connection) {
        this.connection = connection;
    }

    public static UserDatabase getInstance(Properties credentials) {
        if (instance == null) {
            try {
                Connection connection = DriverManager.getConnection(credentials.getProperty("url"), credentials.getProperty("user"), credentials.getProperty("password"));
                instance = new UserDatabase(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    @Override
    public Optional<User> get(LoginRequestBean loginRequest) throws UserNotFound {
        try {
            PreparedStatement statement = connection.prepareStatement("CALL login_as_patient(?, ?);");
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
                return Optional.of(new User(
                        birthDate,
                        fiscalCode,
                        firstName,
                        email,
                        gender.orElse(Gender.NOT_KNOWN),
                        lastName
                ));
            }
            throw new UserNotFound();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean create(UserRegistrationRequestBean request) throws DuplicateEmail {
        try {
            PreparedStatement statement = connection.prepareStatement("CALL register_as_patient(?, ?, ?, ?, ?, ?, ?);");
            statement.setDate(1, Date.valueOf(request.getBirthDate()));
            statement.setString(2, request.getFirstName());
            statement.setString(3, request.getLastName());
            statement.setInt(4, request.getGender().isoCode);
            statement.setString(5, request.getEmail());
            statement.setString(6, request.getPassword());
            statement.setString(7, request.getFiscalCode());
            return statement.execute();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateEmail();
        } catch (SQLException e) {
            return false;
        }
    }
}
