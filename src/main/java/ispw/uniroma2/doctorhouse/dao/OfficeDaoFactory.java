package ispw.uniroma2.doctorhouse.dao;

public interface OfficeDaoFactory {
    void setSpecialtyDao(SpecialtyDao specialtyDao);
    void setShiftDao(ShiftDao shiftDao);
    OfficeDao create();
}
