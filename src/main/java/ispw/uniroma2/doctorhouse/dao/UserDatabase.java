package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.auth.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.auth.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.auth.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.auth.exceptions.UserNotFound;
import ispw.uniroma2.doctorhouse.model.*;
import ispw.uniroma2.doctorhouse.model.doctor.Doctor;
import ispw.uniroma2.doctorhouse.model.doctor.OfficeImpl;
import ispw.uniroma2.doctorhouse.model.doctor.Specialty;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

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
                throw new IrrecoverableError(e);
            }
        }
        return instance;
    }

    private Optional<Doctor> getFamilyDoctor(String email) {
        Map<Location, OfficeBuilder> officeBuilderMap = new HashMap<>();
        Person p = null;
        String field = null;
        try(PreparedStatement statement = connection.prepareStatement("CALL search_doctor(?)")) {
            statement.setString(1, email);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while(rs.next()) {
                LocalDate birthDate = rs.getDate(1).toLocalDate();
                String fiscalCode = rs.getString(2);
                String firstName = rs.getString(3);
                Optional<Gender> gender = Gender.from(rs.getInt(5));
                String lastName = rs.getString(6);
                field = rs.getString(8);
                p = new Person(birthDate, fiscalCode, firstName, lastName, gender.orElse(Gender.NOT_KNOWN));
                Location location = new Location(rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12));
                OfficeBuilder builder = officeBuilderMap.get(location);
                if(builder == null) {
                    builder = new Builder();
                    builder.setLocation(location);
                    officeBuilderMap.put(location, builder);
                }
                builder.addInterval(DayOfWeek.valueOf(rs.getString(13)), new ClockInterval(LocalTime.parse(rs.getString(14)), LocalTime.parse(rs.getString(15))));
                builder.addSpecialties(new Specialty(rs.getString(16), Duration.ofMinutes(Long.parseLong(rs.getString(17)))));
            }
            List<OfficeImpl> offices = new ArrayList<>();
            officeBuilderMap.values().forEach(b -> offices.add(b.build()));
            return Optional.of(new Doctor(email, p, null, field, offices));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> get(LoginRequestBean loginRequest) throws UserNotFound {
        Map<Location, OfficeBuilder> builderMap = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement("CALL login(?, ?);")){
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
                Person p = new Person(birthDate, fiscalCode, firstName, lastName, gender.orElse(Gender.NOT_KNOWN));
                if(field == null){
                    return Optional.of(new User(email, p, getFamilyDoctor(rs.getString(7)).orElse(null)));
                } else {
                    while(rs.next()) {
                        Location location = new Location(rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12));
                        OfficeBuilder builder = builderMap.get(location);
                        if(builder == null) {
                            builder = new Builder();
                            builder.setLocation(location);
                            builderMap.put(location, builder);
                        }
                        builder.addInterval(DayOfWeek.valueOf(rs.getString(13)), new ClockInterval(LocalTime.parse(rs.getString(14)), LocalTime.parse(rs.getString(15))));
                        builder.addSpecialties(new Specialty(rs.getString(16), Duration.ofMinutes(Long.parseLong(rs.getString(17)))));
                    }
                    List<OfficeImpl> offices = new ArrayList<>();
                    builderMap.values().forEach(b -> offices.add(b.build()));
                    return Optional.of(new Doctor(email, p, null, field, offices));
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
            throw new IrrecoverableError(e);
        }
    }
}
