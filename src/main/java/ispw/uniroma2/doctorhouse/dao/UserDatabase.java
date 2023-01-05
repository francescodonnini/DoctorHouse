package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.auth.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.auth.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.model.Gender;
import ispw.uniroma2.doctorhouse.model.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

public class UserDatabase implements UserDao {
    private final Properties credentials;

    public UserDatabase(Properties credentials) {
        this.credentials = credentials;
    }

    @Override
    public Optional<User> get(LoginRequestBean loginRequest) {
        try (Connection connection = DriverManager.getConnection(credentials.getProperty("url"), credentials)) {
            PreparedStatement statement = connection.prepareStatement("SELECT birthDate, fiscalCode, firstName, email, gender, lastName FROM users WHERE email = ? AND password_hash = PASSWORD(?);");
            statement.setString(1, loginRequest.getEmail().getEmail());
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
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean create(UserRegistrationRequestBean request) {
        try (Connection connection = DriverManager.getConnection(credentials.getProperty("url"), credentials)) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (birthDate, fiscalCode, firstName, email, gender, lastName, password_hash) VALUES(?, ?, ?, ?, ?, ?, PASSWORD(?));");
            statement.setDate(1, Date.valueOf(request.getBirthDate()));
            statement.setString(2, request.getFiscalCode());
            statement.setString(3, request.getFirstName());
            statement.setString(4, request.getEmail().getEmail());
            statement.setInt(5, request.getGender().isoCode);
            statement.setString(6, request.getLastName());
            statement.setString(7, request.getPassword());
            return statement.execute();
        } catch (SQLException e) {
            return false;
        }
    }
}
