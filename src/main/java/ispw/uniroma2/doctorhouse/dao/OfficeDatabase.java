package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.*;

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
    public Optional<Office> getOffice(OfficeBean office) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getOffice(?, ?);")) {
            statement.setString(1, office.getDoctor().getEmail());
            statement.setInt(2, office.getId());
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    return fromResultSet(resultSet, office.getDoctor());
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public List<Office> getOffices(DoctorBean doctor) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getOffices(?);")) {
            statement.setString(1, doctor.getEmail());
            if (statement.execute()) {
                List<Office> offices = new ArrayList<>();
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    Optional<Office> office = fromResultSet(resultSet, doctor);
                    office.ifPresent(offices::add);
                }
                return offices;
            }
            return List.of();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    private Optional<Office> fromResultSet(ResultSet resultSet, DoctorBean doctor) throws SQLException, PersistentLayerException {
        OfficeBuilder builder = new OfficeBuilderImpl();
        int id = resultSet.getInt(1);
        String country = resultSet.getString(2);
        String province = resultSet.getString(3);
        String city = resultSet.getString(4);
        String address = resultSet.getString(5);
        Location location = new Location(country, province, city, address);
        builder.setLocation(location);
        OfficeBean officeBean = new OfficeBean();
        officeBean.setId(id);
        officeBean.setDoctor(doctor);
        List<Specialty> specialties = specialtyDao.getSpecialties(officeBean);
        specialties.forEach(builder::addSpecialty);
        List<Shift> shifts = shiftDao.getShifts(officeBean);
        shifts.forEach(builder::addShift);
        return Optional.ofNullable(builder.build());
    }
}
