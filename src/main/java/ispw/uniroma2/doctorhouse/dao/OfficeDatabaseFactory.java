package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;

import java.io.IOException;
import java.util.Properties;

public class OfficeDatabaseFactory implements OfficeDaoFactory {
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
    public OfficeDao create() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("user"));
            return OfficeDatabase.getInstance(properties, specialtyDao, shiftDao);
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }
}
