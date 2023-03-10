package ispw.uniroma2.doctorhouse.dao.office;

import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDao;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDao;
import ispw.uniroma2.doctorhouse.model.Location;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.OfficeBuilder;
import ispw.uniroma2.doctorhouse.model.OfficeBuilderImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class OfficeDatabase implements OfficeDao {
    private final Connection connection;
    private final SpecialtyDao specialtyDao;
    private final ShiftDao shiftDao;

    public OfficeDatabase(Connection connection, SpecialtyDao specialtyDao, ShiftDao shiftDao) {
        this.connection = connection;
        this.specialtyDao = specialtyDao;
        this.shiftDao = shiftDao;
    }

    @Override
    public Optional<Office> getOffice(int officeId, String doctorEmail) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getOffice(?, ?);")) {
            statement.setString(1, doctorEmail);
            statement.setInt(2, officeId);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    return fromResultSet(resultSet, doctorEmail);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public List<Office> getOffices(String email) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getOffices(?);")) {
            statement.setString(1, email);
            if (statement.execute()) {
                List<Office> offices = new ArrayList<>();
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    Optional<Office> office = fromResultSet(resultSet, email);
                    office.ifPresent(offices::add);
                }
                return offices;
            }
            return List.of();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    private Optional<Office> fromResultSet(ResultSet resultSet, String email) throws SQLException, PersistentLayerException {
        OfficeBuilder builder = new OfficeBuilderImpl();
        int id = resultSet.getInt(1);
        builder.setId(id);
        String country = resultSet.getString(2);
        String province = resultSet.getString(3);
        String city = resultSet.getString(4);
        String address = resultSet.getString(5);
        Location location = new Location(country, province, city, address);
        builder.setLocation(location);
        specialtyDao.getSpecialties(id, email).forEach(builder::addSpecialty);
        shiftDao.getShifts(id, email).forEach(builder::addShift);
        return Optional.ofNullable(builder.build());
    }
}
