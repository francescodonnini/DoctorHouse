package ispw.uniroma2.doctorhouse.dao.specialty;

import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.beans.SpecialtyBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Specialty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SpecialtyDatabase implements SpecialtyDao {
    private final Connection connection;

    public SpecialtyDatabase(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<Specialty> getSpecialties(OfficeBean office) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getSpecialties(?, ?);")) {
            statement.setString(1, office.getDoctor().getEmail());
            statement.setInt(2, office.getId());
            if (statement.execute()) {
                List<Specialty> specialties = new ArrayList<>();
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    Duration duration = Duration.ofMinutes(resultSet.getLong(2));
                    specialties.add(new Specialty(name, duration));
                }
                return specialties;
            }
            return List.of();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Optional<Specialty> getSpecialty(String specialtyName, DoctorBean doctorBean) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getSpecialty(?, ?);")) {
            statement.setString(1, specialtyName);
            statement.setString(2, doctorBean.getEmail());
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    Duration duration = Duration.ofMinutes(resultSet.getLong(1));
                    return Optional.of(new Specialty(specialtyName, duration));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Optional<Specialty> getSpecialty(SpecialtyBean specialtyBean) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getSpecialty(?, ?);")) {
            statement.setString(1, specialtyBean.getName());
            statement.setString(2, specialtyBean.getDoctor().getEmail());
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    Duration duration = Duration.ofMinutes(resultSet.getLong(1));
                    return Optional.of(new Specialty(specialtyBean.getName(), duration));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }
}
