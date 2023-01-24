package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;

import java.io.IOException;
import java.util.Properties;

public class ShiftDatabaseFactory implements ShiftDaoFactory {
    @Override
    public ShiftDao create() {
        try {
            Properties credentials = new Properties();
            credentials.load(getClass().getResourceAsStream("user"));
            return ShiftDatabase.getInstance(credentials);
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }
}
