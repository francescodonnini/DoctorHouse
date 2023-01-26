package ispw.uniroma2.doctorhouse.dao;
import java.sql.Connection;


public class OfficeDatabaseFactory implements OfficeDaoFactory {

    private final Connection connection;
    private SpecialtyDao specialtyDao;
    private ShiftDao shiftDao;

    public OfficeDatabaseFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setSpecialtyDao(SpecialtyDao specialtyDao) {
        this.specialtyDao = specialtyDao;
    }

    @Override
    public void setShiftDao(ShiftDao shiftDao) {
        this.shiftDao = shiftDao;
    }


    @Override
    public OfficeDao create() {
        return new OfficeDatabase(connection, specialtyDao, shiftDao);
    }
}
