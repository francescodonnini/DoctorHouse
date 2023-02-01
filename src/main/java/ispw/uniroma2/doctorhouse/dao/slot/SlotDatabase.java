package ispw.uniroma2.doctorhouse.dao.slot;

import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.ClockInterval;
import ispw.uniroma2.doctorhouse.model.TakenSlot;
import ispw.uniroma2.doctorhouse.model.TakenSlotImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SlotDatabase implements SlotDao {
    private final Connection connection;
    private final AppointmentDao appointmentDao;

    public SlotDatabase(Connection connection, AppointmentDao appointmentDao) {
        this.connection = connection;
        this.appointmentDao = appointmentDao;
    }

    @Override
    public List<TakenSlot> getSlots(OfficeBean bean) throws PersistentLayerException {
        List<TakenSlot> slots = appointmentDao.find(bean);
        try (PreparedStatement statement = connection.prepareStatement("CALL findSlots(?, ?);")) {
            statement.setString(1, bean.getDoctor().getEmail());
            statement.setLong(2, bean.getId());
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    LocalDateTime dateTime = resultSet.getObject(1, LocalDateTime.class);
                    Duration duration = Duration.ofSeconds(resultSet.getInt(2));
                    slots.add(new TakenSlotImpl(dateTime, new ClockInterval(dateTime.toLocalTime(), dateTime.toLocalTime().plus(duration))));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return slots;
    }
}
