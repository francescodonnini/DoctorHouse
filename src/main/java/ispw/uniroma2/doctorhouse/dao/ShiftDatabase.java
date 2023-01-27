package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.ClockInterval;
import ispw.uniroma2.doctorhouse.model.Shift;
import ispw.uniroma2.doctorhouse.model.ShiftBuilder;
import ispw.uniroma2.doctorhouse.model.ShiftBuilderImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ShiftDatabase implements ShiftDao {
    private final Connection connection;

    public ShiftDatabase(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Shift> getShifts(OfficeBean office) throws PersistentLayerException {
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
            throw new PersistentLayerException(e);
        }
    }
}
