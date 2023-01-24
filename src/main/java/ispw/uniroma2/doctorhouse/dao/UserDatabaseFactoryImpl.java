package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;

import java.io.IOException;
import java.util.Properties;

public class UserDatabaseFactoryImpl implements UserDaoFactory {
    private OfficeDao officeDao;

    @Override
    public void setOfficeDao(OfficeDao officeDao) {
        this.officeDao = officeDao;
    }

    @Override
    public UserDao create() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("user"));
            return UserDatabase.getInstance(properties, officeDao);
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }
}
