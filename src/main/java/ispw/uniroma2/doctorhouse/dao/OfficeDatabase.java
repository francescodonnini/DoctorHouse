package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.OfficeBeanImpl;
import ispw.uniroma2.doctorhouse.model.Location;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.OfficeBuilder;
import ispw.uniroma2.doctorhouse.model.OfficeBuilderImpl;
import ispw.uniroma2.doctorhouse.model.Shift;
import ispw.uniroma2.doctorhouse.model.Specialty;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OfficeDatabase implements OfficeDao {
    private static OfficeDatabase instance;
    private final Connection connection;
    private final SpecialtyDao specialtyDao;
    private final ShiftDao shiftDao;

    private OfficeDatabase(Connection connection, SpecialtyDao specialtyDao, ShiftDao shiftDao) {
        this.connection = connection;
        this.specialtyDao = specialtyDao;
        this.shiftDao = shiftDao;
    }

    public static OfficeDatabase getInstance(Properties credentials, SpecialtyDao specialtyDao, ShiftDao shiftDao) {
        if (instance == null) {
            try {
                Connection connection = DriverManager.getConnection(credentials.getProperty("url"), credentials.getProperty("user"), credentials.getProperty("password"));
                instance = new OfficeDatabase(connection, specialtyDao, shiftDao);
            } catch (SQLException e) {
                throw new IrrecoverableError(e);
            }
        }
        return instance;
    }

    @Override
    public List<Office> getOffices(DoctorBean doctor) {
        try (PreparedStatement statement = connection.prepareStatement("CALL getOffices(?);")) {
            statement.setString(1, doctor.getEmail());
            if (statement.execute()) {
                List<Office> offices = new ArrayList<>();
                OfficeBuilder builder = new OfficeBuilderImpl();
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String country = resultSet.getString(2);
                    String province = resultSet.getString(3);
                    String city = resultSet.getString(4);
                    String address = resultSet.getString(5);
                    Location location = new Location(country, province, city, address);
                    builder.setLocation(location);
                    OfficeBeanImpl officeBean = new OfficeBeanImpl();
                    officeBean.setId(id);
                    officeBean.setDoctor(doctor);
                    List<Specialty> specialties = specialtyDao.getSpecialties(officeBean);
                    specialties.forEach(builder::addSpecialty);
                    List<Shift> shifts = shiftDao.getShifts(officeBean);
                    shifts.forEach(builder::addShift);
                    offices.add(builder.build());
                    builder.reset();
                }
                return offices;
            }
            return List.of();
        } catch (SQLException e) {
            throw new IrrecoverableError(e);
        }
    }
}
