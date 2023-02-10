package ispw.uniroma2.doctorhouse.dao.appointment;

import ispw.uniroma2.doctorhouse.beans.AppointmentBean;
import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.users.UserDao;
import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.appointment.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentDatabase implements AppointmentDao {
    private final Connection connection;
    private final UserDao userDao;
    private final OfficeDao officeDao;
    private final SpecialtyDao specialtyDao;

    public AppointmentDatabase(Connection connection, UserDao userDao, OfficeDao officeDao, SpecialtyDao specialtyDao) {
        this.connection = connection;
        this.userDao = userDao;
        this.officeDao = officeDao;
        this.specialtyDao = specialtyDao;
    }

    @Override
    public Optional<Appointment> create(AppointmentBean appointment) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL book(?, ?, ?, ?, ?);")) {
            statement.setString(1, appointment.getDoctor().getEmail());
            statement.setString(2, appointment.getPatient().getEmail());
            statement.setInt(3, appointment.getOffice().getId());
            statement.setString(4, appointment.getSpecialty().getName());
            statement.setObject(5, appointment.getDateTime());
            statement.execute();
            AppointmentBuilder builder = new AppointmentBuilderImpl();
            userDao.getUser(appointment.getPatient().getEmail()).ifPresent(builder::setPatient);
            userDao.getDoctor(appointment.getDoctor().getEmail()).ifPresent(builder::setDoctor);
            specialtyDao.getSpecialty(appointment.getSpecialty().getName(), appointment.getDoctor().getEmail()).ifPresent(builder::setSpecialty);
            OfficeBean office = appointment.getOffice();
            officeDao.getOffice(office.getId(), office.getDoctor().getEmail()).ifPresent(builder::setOffice);
            builder.setDate(appointment.getDateTime());
            return Optional.ofNullable(builder.build(ScheduledInfo.class));
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public List<Appointment> findByEmail(String participantEmail, Class<? extends AppointmentInfo> typeName) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL findAppointmentsByEmail(?, ?);")) {
            statement.setString(1, participantEmail);
            statement.setString(2, getStateName(typeName));
            if (statement.execute()) {
                return fromResultSet(statement.getResultSet(), typeName);
            }
            return List.of();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public List<Appointment> findByOffice(String doctorEmail, int officeId, Class<? extends AppointmentInfo> typeName) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL findAppointmentsByOffice(?, ?, ?);")) {
            statement.setInt(1, officeId);
            statement.setString(2, doctorEmail);
            statement.setString(3, getStateName(typeName));
            if (statement.execute()) {
                return fromResultSet(statement.getResultSet(), typeName);
            }
            return List.of();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    private List<Appointment> fromResultSet(ResultSet resultSet, Class<? extends AppointmentInfo> typeName) throws SQLException, PersistentLayerException {
        List<Appointment> appointments = new ArrayList<>();
        AppointmentBuilder builder = new AppointmentBuilderImpl();
        final int patientCol = 1;
        final int doctorCol = 2;
        final int dateCol = 3;
        final int specialtyCol = 4;
        final int officeCol = 5;
        final int oldDateCol = 6;
        final int initiatorCol = 7;
        while (resultSet.next()) {
            userDao.getUser(resultSet.getString(patientCol)).ifPresent(builder::setPatient);
            userDao.getDoctor(resultSet.getString(doctorCol)).ifPresent(builder::setDoctor);
            officeDao.getOffice(resultSet.getInt(officeCol), resultSet.getString(doctorCol)).ifPresent(builder::setOffice);
            specialtyDao.getSpecialty(resultSet.getString(specialtyCol), resultSet.getString(doctorCol)).ifPresent(builder::setSpecialty);
            builder.setDate(resultSet.getObject(dateCol, LocalDateTime.class));
            builder.setOldDate(resultSet.getObject(oldDateCol, LocalDateTime.class));
            userDao.getUser(resultSet.getString(initiatorCol)).ifPresent(builder::setInitiator);
            appointments.add(builder.build(typeName));
        }
        return appointments;
    }

    @Override
    public void save(Appointment appointment) throws PersistentLayerException, InvalidTimeSlot {
        AppointmentInfo info = appointment.getInfo();
        if (info instanceof CanceledInfo) {
            cancel(appointment, (CanceledInfo) info);
        } else if (info instanceof ScheduledInfo) {
            incoming(appointment, (ScheduledInfo)info);
        } else if (info instanceof PendingInfo) {
           pending(appointment, (PendingInfo)info);
        }
    }

    private void pending(Appointment appointment, PendingInfo info) throws InvalidTimeSlot, PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL pending(?, ?, ?, ?, ?);")) {
            statement.setString(1, appointment.getDoctor().getEmail());
            statement.setString(2, appointment.getPatient().getEmail());
            statement.setObject(3, info.getOldDate());
            statement.setObject(4, info.getNewDate());
            statement.setString(5, info.getInitiator().getEmail());
            statement.execute();
        } catch (SQLTransactionRollbackException e) {
            throw new InvalidTimeSlot((info).getNewDate());
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    private void incoming(Appointment appointment, ScheduledInfo info) throws InvalidTimeSlot, PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL incoming(?, ?, ?);")) {
            statement.setString(1, appointment.getDoctor().getEmail());
            statement.setString(2, appointment.getPatient().getEmail());
            statement.setObject(3, info.getDate());
            statement.execute();
        } catch (SQLTransactionRollbackException e){
            throw new InvalidTimeSlot(info.getDate());
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    private void cancel(Appointment appointment, CanceledInfo info) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL cancel(?, ?, ?, ?);")) {
            statement.setString(1, appointment.getDoctor().getEmail());
            statement.setString(2, appointment.getPatient().getEmail());
            statement.setObject(3, info.getDate());
            User initiator = info.getInitiator();
            statement.setString(4, initiator.getEmail());
            statement.execute();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }


    private String getStateName(Class<? extends AppointmentInfo> stateClass) {
        if (CanceledInfo.class.equals(stateClass)) {
            return "c";
        } else if (ScheduledInfo.class.equals(stateClass)) {
            return "s";
        } else if (PendingInfo.class.equals(stateClass)) {
            return "p";
        }
        throw new IllegalArgumentException();
    }
}
