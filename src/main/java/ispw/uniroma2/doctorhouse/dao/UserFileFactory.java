package ispw.uniroma2.doctorhouse.dao;

public class UserFileFactory implements UserDaoFactory {
    private OfficeDao officeDao;

    @Override
    public void setOfficeDao(OfficeDao officeDao) {
        this.officeDao = officeDao;
    }

    @Override
    public UserDao create() {
        return new UserFile(officeDao);
    }
}
