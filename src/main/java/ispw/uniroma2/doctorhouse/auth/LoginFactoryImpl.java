package ispw.uniroma2.doctorhouse.auth;

import ispw.uniroma2.doctorhouse.dao.users.UserDao;

public class LoginFactoryImpl implements LoginFactory {
    private final UserDao userDao;

    public LoginFactoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Login create() {
        return new Login(userDao);
    }
}
