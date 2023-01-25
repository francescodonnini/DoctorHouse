package ispw.uniroma2.doctorhouse.dao;
import java.sql.Connection;


public class OfficeDatabaseFactory implements OfficeDaoFactory {

    private Connection connection;
    private SpecialtyDao specialtyDao;
    private ShiftDao shiftDao;

    @Override
    public void setSpecialtyDao(SpecialtyDao specialtyDao) {
        this.specialtyDao = specialtyDao;
    }

    @Override
    public void setShiftDao(ShiftDao shiftDao) {
        this.shiftDao = shiftDao;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    @Override
    public OfficeDao create() {
        return new OfficeDatabase(connection, specialtyDao, shiftDao);
    }
}
