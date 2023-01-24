package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;

import java.io.IOException;
import java.util.Properties;

public class SpecialtyDatabaseFactory implements SpecialtyDaoFactory {
    @Override
    public SpecialtyDao create() {
        try {
            Properties credentials = new Properties();
            credentials.load(getClass().getResourceAsStream("user"));
            return SpecialtyDatabase.getInstance(credentials);
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }
}
