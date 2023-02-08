package ispw.uniroma2.doctorhouse.dao.appointment;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.users.UserDao;
import ispw.uniroma2.doctorhouse.model.TakenSlot;
import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.appointment.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            return Optional.ofNullable(builder.build(IncomingInfo.class));
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public List<Appointment> find(String participantEmail, Class<? extends AppointmentInfo> type) throws PersistentLayerException {
        if (PendingInfo.class.equals(type)) {
            return getPendingAppointments(participantEmail);
        } else {
            return getAppointments(participantEmail, type);
        }
    }

    private List<Appointment> getPendingAppointments(String participantEmail) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getPendingAppointments(?);")) {
            statement.setString(1, participantEmail);
            if (statement.execute()) {
                List<Appointment> appointments = new ArrayList<>();
                AppointmentBuilder builder = new AppointmentBuilderImpl();
                ResultSet resultSet = statement.getResultSet();
                final int doctorCol = 1;
                final int patientCol = 2;
                final int officeCol = 3;
                final int specialtyNameCol = 4;
                final int dateCol = 5;
                final int oldDateCol = 6;
                final int initiatorCol = 7;
                while (resultSet.next()) {
                    // 1: doctor
                    // 2: patient
                    // 3: office
                    // 4: specialty-name
                    // 5: date
                    // 6: old-date
                    // 7: initiator
                    userDao.getDoctor(resultSet.getString(doctorCol)).ifPresent(builder::setDoctor);
                    userDao.getUser(resultSet.getString(patientCol)).ifPresent(builder::setPatient);
                    officeDao.getOffice(resultSet.getInt(officeCol), resultSet.getString(doctorCol)).ifPresent(builder::setOffice);
                    specialtyDao.getSpecialty(resultSet.getString(specialtyNameCol), resultSet.getString(doctorCol)).ifPresent(builder::setSpecialty);
                    builder.setDate(resultSet.getObject(dateCol, LocalDateTime.class));
                    builder.setOldDate(resultSet.getObject(oldDateCol, LocalDateTime.class));
                    userDao.getUser(resultSet.getString(initiatorCol)).ifPresent(builder::setInitiator);
                    appointments.add(builder.build(PendingInfo.class));
                    builder.reset();
                }
                return appointments;
            }
            return List.of();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }
    private List<Appointment> getAppointments(String participantEmail, Class<? extends AppointmentInfo> type) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getAppointments(?, ?);")) {
            statement.setString(1, participantEmail);
            if (CanceledInfo.class.equals(type)) {
                statement.setString(2, "c");
            } else if (ConsumedInfo.class.equals(type)) {
                statement.setString(2, "co");
            } else if (IncomingInfo.class.equals(type)) {
                statement.setString(2, "s");
            }
            if (statement.execute()) {
                List<Appointment> appointments = new ArrayList<>();
                AppointmentBuilder builder = new AppointmentBuilderImpl();
                ResultSet resultSet = statement.getResultSet();
                final int doctorCol = 1;
                final int patientCol = 2;
                final int officeCol = 3;
                final int specialtyNameCol = 4;
                final int dateCol = 5;
                final int oldDateCol = 6;
                final int initiatorCol = 7;
                while (resultSet.next()) {
                    userDao.getDoctor(resultSet.getString(doctorCol)).ifPresent(builder::setDoctor);
                    userDao.getUser(resultSet.getString(patientCol)).ifPresent(builder::setPatient);
                    officeDao.getOffice(resultSet.getInt(officeCol), resultSet.getString(doctorCol)).ifPresent(builder::setOffice);
                    specialtyDao.getSpecialty(resultSet.getString(specialtyNameCol), resultSet.getString(doctorCol)).ifPresent(builder::setSpecialty);
                    builder.setDate(resultSet.getObject(dateCol, LocalDateTime.class));
                    builder.setOldDate(resultSet.getObject(oldDateCol, LocalDateTime.class));
                    userDao.getUser(resultSet.getString(initiatorCol)).ifPresent(builder::setInitiator);
                    appointments.add(builder.build(type));
                    builder.reset();
                }
                return appointments;
            }
            return List.of();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public List<TakenSlot> find(String doctorEmail) throws PersistentLayerException {
        List<TakenSlot> slots = new ArrayList<>();
        slots.addAll(find(doctorEmail, IncomingInfo.class).stream().map(a -> (TakenSlot) a.getInfo()).collect(Collectors.toList()));
        slots.addAll(find(doctorEmail, PendingInfo.class).stream().map(a -> (TakenSlot) a.getInfo()).collect(Collectors.toList()));
        return slots;
    }

    @Override
    public void save(Appointment appointment) throws PersistentLayerException, InvalidTimeSlot {
        AppointmentInfo info = appointment.getInfo();
        if (info instanceof CanceledInfo) {
            cancel(appointment, (CanceledInfo) info);
        } else if (info instanceof ConsumedInfo) {
            consume(appointment, (ConsumedInfo) info);
        } else if (info instanceof IncomingInfo) {
            incoming(appointment, (IncomingInfo)info);
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

    private void incoming(Appointment appointment, IncomingInfo info) throws InvalidTimeSlot, PersistentLayerException {
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
            Optional<User> initiator = info.getInitiator();
            if (initiator.isPresent()) {
                statement.setString(4, initiator.get().getEmail());
            } else {
                statement.setNull(4, Types.VARCHAR);
            }
            statement.execute();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    private void consume(Appointment appointment, ConsumedInfo info) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL consume(?, ?, ?);")) {
            statement.setString(1, appointment.getDoctor().getEmail());
            statement.setString(2, appointment.getPatient().getEmail());
            statement.setObject(3, info.getDate());
            statement.execute();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }
}
