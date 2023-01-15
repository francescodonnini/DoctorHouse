package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;

import java.io.IOException;
import java.util.Properties;

public class UserDaoFactoryImpl implements UserDaoFactory {
    @Override
    public UserDao create() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("user"));
            return UserDatabase.getInstance(properties);
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }
}
