package ispw.uniroma2.doctorhouse.dao.appointment;

import ispw.uniroma2.doctorhouse.dao.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.UserDao;

public interface AppointmentDaoFactory {
    void setUserDao(UserDao userDao);
    void setOfficeDao(OfficeDao officeDao);
    void setSpecialtyDao(SpecialtyDao specialtyDao);
    AppointmentDao create();
}
