package ispw.uniroma2.doctorhouse.dao.appointment;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.UserDao;
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
            userDao.getUser(appointment.getPatient()).ifPresent(builder::setPatient);
            userDao.getDoctor(appointment.getDoctor()).ifPresent(builder::setDoctor);
            specialtyDao.getSpecialty(appointment.getSpecialty().getName(), appointment.getDoctor()).ifPresent(builder::setSpecialty);
            officeDao.getOffice(appointment.getOffice()).ifPresent(builder::setOffice);
            builder.setDate(appointment.getDateTime());
            return Optional.ofNullable(builder.build(IncomingInfo.class));
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public List<Appointment> find(UserBean participant, Class<? extends AppointmentInfo> type) throws PersistentLayerException {
        if (PendingInfo.class.equals(type)) {
            return getPendingAppointments(participant);
        } else {
            return getAppointments(participant, type);
        }

    }

    private List<Appointment> getPendingAppointments(UserBean participant) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getPendingAppointments(?);")) {
            statement.setString(1, participant.getEmail());
            if (statement.execute()) {
                List<Appointment> appointments = new ArrayList<>();
                AppointmentBuilder builder = new AppointmentBuilderImpl();
                DoctorBean doctorBean = new DoctorBean();
                OfficeBean officeBean = new OfficeBean();
                SpecialtyBean specialtyBean = new SpecialtyBean();
                UserBean userBean = new UserBean();
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String doctorEmail = resultSet.getString(1);
                    doctorBean.setEmail(doctorEmail);
                    userDao.getDoctor(doctorBean).ifPresent(builder::setDoctor);

                    String patientEmail = resultSet.getString(2);
                    userBean.setEmail(patientEmail);
                    userDao.getUser(userBean).ifPresent(builder::setPatient);

                    officeBean.setId(resultSet.getInt(3));
                    officeBean.setDoctor(doctorBean);
                    officeDao.getOffice(officeBean).ifPresent(builder::setOffice);

                    specialtyBean.setName(resultSet.getString(4));
                    specialtyBean.setDoctor(doctorBean);
                    specialtyDao.getSpecialty(specialtyBean).ifPresent(builder::setSpecialty);

                    LocalDateTime date = resultSet.getObject(5, LocalDateTime.class);
                    builder.setDate(date);

                    LocalDateTime oldDate = resultSet.getObject(6, LocalDateTime.class);
                    builder.setOldDate(oldDate);

                    String initiatorEmail = resultSet.getString(7);
                    userBean.setEmail(initiatorEmail);
                    userDao.getUser(userBean).ifPresent(builder::setInitiator);

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
    private List<Appointment> getAppointments(UserBean participant, Class<? extends AppointmentInfo> type) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL getAppointments(?, ?);")) {
            statement.setString(1, participant.getEmail());
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
                DoctorBean doctorBean = new DoctorBean();
                OfficeBean officeBean = new OfficeBean();
                SpecialtyBean specialtyBean = new SpecialtyBean();
                UserBean userBean = new UserBean();
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String doctorEmail = resultSet.getString(1);
                    doctorBean.setEmail(doctorEmail);
                    userDao.getDoctor(doctorBean).ifPresent(builder::setDoctor);

                    String patientEmail = resultSet.getString(2);
                    userBean.setEmail(patientEmail);
                    userDao.getUser(userBean).ifPresent(builder::setPatient);

                    officeBean.setId(resultSet.getInt(3));
                    officeBean.setDoctor(doctorBean);
                    officeDao.getOffice(officeBean).ifPresent(builder::setOffice);

                    specialtyBean.setName(resultSet.getString(4));
                    specialtyBean.setDoctor(doctorBean);
                    specialtyDao.getSpecialty(specialtyBean).ifPresent(builder::setSpecialty);

                    LocalDateTime date = resultSet.getObject(5, LocalDateTime.class);
                    builder.setDate(date);

                    LocalDateTime newDate = resultSet.getObject(6, LocalDateTime.class);
                    builder.setOldDate(newDate);

                    String initiatorEmail = resultSet.getString(7);
                    userBean.setEmail(initiatorEmail);
                    userDao.getUser(userBean).ifPresent(builder::setInitiator);

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
    public List<TakenSlot> find(OfficeBean office) throws PersistentLayerException {
        UserBean participant = new UserBean();
        participant.setEmail(office.getDoctor().getEmail());
        List<TakenSlot> slots = new ArrayList<>();
        slots.addAll(find(participant, IncomingInfo.class).stream().map(a -> (TakenSlot) a.getInfo()).collect(Collectors.toList()));
        slots.addAll(find(participant, PendingInfo.class).stream().map(a -> (TakenSlot) a.getInfo()).collect(Collectors.toList()));
        return slots;
    }

    @Override
    public void save(Appointment appointment) throws PersistentLayerException, InvalidTimeSlot {
        AppointmentInfo info = appointment.getInfo();
        if (info instanceof CanceledInfo) {
            cancel((CanceledInfo) info);
        } else if (info instanceof ConsumedInfo) {
            consume((ConsumedInfo) info);
        } else if (info instanceof IncomingInfo) {
            incoming((IncomingInfo)info);
        } else if (info instanceof PendingInfo) {
           pending((PendingInfo)info);
        }
    }

    private void pending(PendingInfo info) throws InvalidTimeSlot, PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL pending(?, ?, ?, ?, ?);")) {
            statement.setString(1, info.getDoctor().getEmail());
            statement.setString(2, info.getPatient().getEmail());
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

    private void incoming(IncomingInfo info) throws InvalidTimeSlot, PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL incoming(?, ?, ?);")) {
            statement.setString(1, info.getDoctor().getEmail());
            statement.setString(2, info.getPatient().getEmail());
            statement.setObject(3, info.getDate());
            statement.execute();
        } catch (SQLTransactionRollbackException e){
            throw new InvalidTimeSlot(info.getDate());
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }

    private void cancel(CanceledInfo info) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL cancel(?, ?, ?, ?);")) {
            statement.setString(1, info.getDoctor().getEmail());
            statement.setString(2, info.getPatient().getEmail());
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

    private void consume(ConsumedInfo info) throws PersistentLayerException {
        try (PreparedStatement statement = connection.prepareStatement("CALL consume(?, ?, ?);")) {
            statement.setString(1, info.getDoctor().getEmail());
            statement.setString(2, info.getPatient().getEmail());
            statement.setObject(3, info.getDate());
            statement.execute();
        } catch (SQLException e) {
            throw new PersistentLayerException(e);
        }
    }
}
