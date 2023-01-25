package ispw.uniroma2.doctorhouse.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static Connection connection;

    private ConnectionFactory() {
    }

    public static Connection getConnection() {
        if(connection == null) {
            try(InputStream input = new FileInputStream(ConnectionFactory.class.getResource("user").getPath())) {
                Properties properties = new Properties();
                properties.load(input);
                connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
