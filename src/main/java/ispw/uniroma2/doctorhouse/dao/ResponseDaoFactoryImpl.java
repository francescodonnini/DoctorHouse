package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;

import java.io.IOException;
import java.util.Properties;

public class ResponseDaoFactoryImpl implements ResponseDaoFactory{
    @Override
    public ResponseDao create() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("user"));
            return ResponseDatabase.getInstance(properties);
        } catch (IOException e) {
            throw new IrrecoverableError(e);
        }
    }
}
