package ispw.uniroma2.doctorhouse.dao.office;

import ispw.uniroma2.doctorhouse.dao.shift.ShiftDao;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDao;

import java.sql.Connection;


public class OfficeDatabaseFactory implements OfficeDaoFactory {

    private final Connection connection;
    private final SpecialtyDao specialtyDao;
    private final ShiftDao shiftDao;

    public OfficeDatabaseFactory(Connection connection, SpecialtyDao specialtyDao, ShiftDao shiftDao) {
        this.connection = connection;
        this.specialtyDao = specialtyDao;
        this.shiftDao = shiftDao;
    }

    @Override
    public OfficeDao create() {
        return new OfficeDatabase(connection, specialtyDao, shiftDao);
    }
}
