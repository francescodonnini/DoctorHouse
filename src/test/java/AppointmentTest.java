import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.ConnectionFactory;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentFile;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDaoFactory;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDao;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDaoFactory;
import ispw.uniroma2.doctorhouse.dao.shift.ShiftDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDaoFactory;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDatabaseFactory;
import ispw.uniroma2.doctorhouse.dao.users.UserDao;
import ispw.uniroma2.doctorhouse.dao.users.UserDaoFactory;
import ispw.uniroma2.doctorhouse.dao.users.UserDatabaseFactoryImpl;
import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.Specialty;
import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.appointment.Appointment;
import ispw.uniroma2.doctorhouse.model.appointment.ScheduledInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class AppointmentTest {
    AppointmentFile file;
    Appointment appointment;
    Connection connection;
    private static final String TEST_FILE_PATH = "appointments-test.csv";
    private static final String DOCTOR_EMAIL = "theadora.quinta@email.it";
    private static final String PATIENT_EMAIL = "wandie.auroora@email.it";
    private static final int OFFICE_ID = 637;
    private static final String SPECIALTY_NAME = "visita#0";

    // Creates an appointment @TEST_FILE_PATH so that we can test the method AppointmentFile#findByEmail
    @BeforeEach
    void setup() throws PersistentLayerException {
        connection = ConnectionFactory.getConnection();
        SpecialtyDaoFactory specialtyDaoFactory = new SpecialtyDatabaseFactory(connection);
        SpecialtyDao specialtyDao = specialtyDaoFactory.create();
        ShiftDaoFactory shiftDaoFactory = new ShiftDatabaseFactory(connection);
        ShiftDao shiftDao = shiftDaoFactory.create();
        OfficeDaoFactory officeDaoFactory = new OfficeDatabaseFactory(connection, specialtyDao, shiftDao);
        OfficeDao officeDao = officeDaoFactory.create();
        UserDaoFactory userDaoFactory = new UserDatabaseFactoryImpl(officeDao, connection);
        UserDao userDao = userDaoFactory.create();
        file = new AppointmentFile(TEST_FILE_PATH, officeDao, specialtyDao, userDao);
        AppointmentBean bean = new AppointmentBean();
        Optional<Doctor> optionalDoctor = userDao.getDoctor(DOCTOR_EMAIL);
        if (optionalDoctor.isPresent()) {
            DoctorBean doctorBean = new DoctorBean(optionalDoctor.get());
            bean.setDoctor(doctorBean);
        }
        Optional<User> optionalUser = userDao.getUser(PATIENT_EMAIL);
        if (optionalUser.isPresent()) {
            UserBean userBean = new UserBean(optionalUser.get());
            bean.setPatient(userBean);
        }
        Optional<Office> optionalOffice = officeDao.getOffice(OFFICE_ID, DOCTOR_EMAIL);
        if (optionalOffice.isPresent()) {
            Office office = optionalOffice.get();
            OfficeBean officeBean = new OfficeBean();
            officeBean.setId(office.getId());
            officeBean.setCountry(office.getLocation().getCountry());
            officeBean.setProvince(office.getLocation().getProvince());
            officeBean.setCity(office.getLocation().getCity());
            officeBean.setAddress(office.getLocation().getAddress());
            bean.setOffice(officeBean);
        }
        Optional<Specialty> optionalSpecialty = specialtyDao.getSpecialty(SPECIALTY_NAME, DOCTOR_EMAIL);
        if (optionalSpecialty.isPresent()) {
            Specialty specialty = optionalSpecialty.get();
            SpecialtyBean specialtyBean = new SpecialtyBean();
            specialtyBean.setName(specialty.getName());
            specialtyBean.setDuration(specialty.getDuration());
            specialtyBean.setDoctor(bean.getDoctor());
            bean.setSpecialty(specialtyBean);
        }
        bean.setDateTime(LocalDateTime.of(2023, 3, 20, 9, 0));
        Optional<Appointment> optional = file.create(bean);
        appointment = optional.get();
    }

    @Test
    void testFindByEmail() throws PersistentLayerException {
        List<Appointment> result = file.findByEmail(DOCTOR_EMAIL, ScheduledInfo.class);
        Assertions.assertEquals(1, result.size());
        Appointment target = result.get(0);
        Assertions.assertEquals(appointment.getPatient().getEmail(), target.getPatient().getEmail());
        Assertions.assertEquals(appointment.getDoctor().getEmail(), target.getDoctor().getEmail());
        Assertions.assertEquals(appointment.getInfo().getDate(), target.getInfo().getDate());
        Assertions.assertEquals(appointment.getSpecialty().getName(), target.getSpecialty().getName());
        Assertions.assertEquals(appointment.getSpecialty().getDuration(), target.getSpecialty().getDuration());
    }

    @AfterEach
    void close() throws SQLException {
       connection.close();
    }
}
