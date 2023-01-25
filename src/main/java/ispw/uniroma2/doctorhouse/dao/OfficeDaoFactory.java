package ispw.uniroma2.doctorhouse.dao;

import java.sql.Connection;

public interface OfficeDaoFactory {
    void setSpecialtyDao(SpecialtyDao specialtyDao);
    void setShiftDao(ShiftDao shiftDao);
    void setConnection(Connection connection);
    OfficeDao create();
}
