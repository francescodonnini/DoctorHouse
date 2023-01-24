package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.IrrecoverableError;
import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.model.*;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

public class ShiftDatabase implements ShiftDao {
    private static ShiftDatabase instance;
    private final Connection connection;

    public static ShiftDatabase getInstance(Properties credentials) {
        if (instance == null) {
            try {
                Connection connection = DriverManager.getConnection(credentials.getProperty("url"), credentials.getProperty("user"), credentials.getProperty("password"));
                instance = new ShiftDatabase(connection);
            } catch (SQLException e) {
                throw new IrrecoverableError(e);
            }
        }
        return instance;
    }

    private ShiftDatabase(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Shift> getShifts(OfficeBean office) {
        try (PreparedStatement statement = connection.prepareStatement("CALL getShifts(?, ?);")) {
            statement.setString(1, office.getDoctor().getEmail());
            statement.setInt(2, office.getId());
            if (statement.execute()) {
                List<Shift> shifts = new ArrayList<>();
                ResultSet resultSet = statement.getResultSet();
                Map<DayOfWeek, ShiftBuilder> builders = new EnumMap<>(DayOfWeek.class);
                while (resultSet.next()) {
                    DayOfWeek day = DayOfWeek.of(resultSet.getInt(1));
                    ShiftBuilder builder = builders.computeIfAbsent(day, f -> {
                        ShiftBuilder b = new ShiftBuilderImpl();
                        b.setDay(day);
                        return b;
                    });
                    LocalTime start = resultSet.getTime(2).toLocalTime();
                    LocalTime end = resultSet.getTime(3).toLocalTime();
                    builder.addInterval(new ClockInterval(start, end));
                }
                builders.values().forEach(b -> shifts.add(b.build()));
                return shifts;
            }
            return List.of();
        } catch (SQLException e) {
            throw new IrrecoverableError(e);
        }
    }
}