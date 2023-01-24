package ispw.uniroma2.doctorhouse.dao;

public interface UserDaoFactory {
    void setOfficeDao(OfficeDao officeDao);
    UserDao create();
}
