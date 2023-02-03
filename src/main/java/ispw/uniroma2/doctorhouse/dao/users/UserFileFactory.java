package ispw.uniroma2.doctorhouse.dao.users;

import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;

public class UserFileFactory implements UserDaoFactory {
    private final OfficeDao officeDao;

    public UserFileFactory(OfficeDao officeDao) {
        this.officeDao = officeDao;
    }

    public UserDao create() {
        return new UserFile(officeDao);
    }
}
