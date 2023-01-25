package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.model.Specialty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class SpecialtyDatabase implements SpecialtyDao {
    private final Connection connection;

    public SpecialtyDatabase(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<Specialty> getSpecialties(OfficeBean office) {
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
            throw new IrrecoverableError(e);
        }
    }
}
