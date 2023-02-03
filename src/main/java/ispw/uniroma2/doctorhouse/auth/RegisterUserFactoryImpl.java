package ispw.uniroma2.doctorhouse.auth;

import ispw.uniroma2.doctorhouse.dao.users.UserDao;

public class RegisterUserFactoryImpl implements RegisterUserFactory {
    private final UserDao userDao;

    public RegisterUserFactoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public RegisterUser create() {
        return new RegisterUser(userDao);
    }
}
