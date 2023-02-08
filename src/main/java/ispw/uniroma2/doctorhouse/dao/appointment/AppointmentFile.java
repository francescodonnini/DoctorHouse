package ispw.uniroma2.doctorhouse.dao.appointment;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import ispw.uniroma2.doctorhouse.beans.AppointmentBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.users.UserDao;
import ispw.uniroma2.doctorhouse.model.TakenSlot;
import ispw.uniroma2.doctorhouse.model.User;
import ispw.uniroma2.doctorhouse.model.appointment.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AppointmentFile implements AppointmentDao {
    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DOCTOR_KEY = "doctor";
    private static final String PATIENT_KEY = "patient";
    private static final String DATE_KEY = "date";
    private static final String SPECIALTY_DOCTOR_KEY = "specialty-doctor";
    private static final String SPECIALTY_NAME_KEY = "specialty-name";
    private static final String OFFICE_KEY = "office";
    private static final String STATE_KEY = "state";
    private static final String OLD_DATE_KEY = "old-date";
    private static final String INITIATOR_KEY = "initiator";
    private static final Map<String, Integer> COLUMNS = new HashMap<>();
    private final String filePath;
    private final OfficeDao officeDao;
    private final SpecialtyDao specialtyDao;
    private final UserDao userDao;

    public AppointmentFile(String filePath, OfficeDao officeDao, SpecialtyDao specialtyDao, UserDao userDao) {
        this.filePath = filePath;
        this.officeDao = officeDao;
        this.specialtyDao = specialtyDao;
        this.userDao = userDao;
        initColumnMap();
    }

    private void initColumnMap() {
        COLUMNS.put(PATIENT_KEY, 0);
        COLUMNS.put(DOCTOR_KEY, 1);
        COLUMNS.put(DATE_KEY, 2);
        COLUMNS.put(SPECIALTY_NAME_KEY, 3);
        COLUMNS.put(SPECIALTY_DOCTOR_KEY, 4);
        COLUMNS.put(OFFICE_KEY, 5);
        COLUMNS.put(STATE_KEY, 6);
        COLUMNS.put(OLD_DATE_KEY, 7);
        COLUMNS.put(INITIATOR_KEY, 8);
    }

    private int columnOf(String colName) {
        return COLUMNS.get(colName);
    }

    @Override
    public Optional<Appointment> create(AppointmentBean appointment) throws PersistentLayerException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            writer.writeNext(createLine(appointment));
            AppointmentBuilder builder = new AppointmentBuilderImpl();
            userDao.getDoctor(appointment.getDoctor().getEmail()).ifPresent(builder::setDoctor);
            userDao.getUser(appointment.getPatient().getEmail()).ifPresent(builder::setPatient);
            builder.setDate(appointment.getDateTime());
            officeDao.getOffice(appointment.getOffice().getId(), appointment.getDoctor().getEmail()).ifPresent(builder::setOffice);
            specialtyDao.getSpecialty(appointment.getSpecialty().getName(), appointment.getSpecialty().getDoctor().getEmail()).ifPresent(builder::setSpecialty);
            return Optional.ofNullable(builder.build(IncomingInfo.class));
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
    }

    private String[] createLine(AppointmentBean bean) {
        String[] line = new String[9];
        writeColumn(line, PATIENT_KEY, bean.getPatient().getEmail());
        writeColumn(line, DOCTOR_KEY, bean.getDoctor().getEmail());
        writeColumn(line, DATE_KEY, bean.getDateTime().format(DateTimeFormatter.ISO_DATE_TIME));
        writeColumn(line, OFFICE_KEY, String.valueOf(bean.getOffice().getId()));
        writeColumn(line, SPECIALTY_NAME_KEY, bean.getSpecialty().getName());
        writeColumn(line, SPECIALTY_DOCTOR_KEY, bean.getSpecialty().getDoctor().getEmail());
        return line;
    }

    private void writeColumn(String[] line, String colName, String colVal) {
        line[columnOf(colName)] = colVal;
    }

    private CSVReader getReader() throws FileNotFoundException {
        return new CSVReaderBuilder(new FileReader(filePath))
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .build();
    }

    @Override
    public List<Appointment> find(String email, Class<? extends AppointmentInfo> type) throws PersistentLayerException {
        try (CSVReader reader = getReader()) {
            Predicate<String[]> filter;
            if (type.equals(PendingInfo.class)) {
                filter = createPendingFilter(email);
            } else {
                filter = createOthersFilter(email, getStateName(type));
            }
            List<Appointment> appointments = new ArrayList<>();
            AppointmentBuilder builder = new AppointmentBuilderImpl();
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                if (filter.test(line)) {
                    String patient = getColumn(line, PATIENT_KEY).orElse("");
                    userDao.getUser(patient).ifPresent(builder::setPatient);
                    String doctor = getColumn(line, DOCTOR_KEY).orElse("");
                    userDao.getDoctor(doctor).ifPresent(builder::setDoctor);
                    Optional<String> col = getColumn(line, DATE_KEY);
                    if (col.isPresent()) {
                        LocalDateTime date = LocalDateTime.parse(col.get(), DateTimeFormatter.ofPattern(DATETIME_PATTERN));
                        builder.setDate(date);
                    }
                    String office = getColumn(line, OFFICE_KEY).orElse("");
                    if (!office.isEmpty()) {
                        int officeId = Integer.parseInt(office);
                        officeDao.getOffice(officeId, doctor).ifPresent(builder::setOffice);
                    }
                    String specialtyName = getColumn(line, SPECIALTY_NAME_KEY).orElse("");
                    String specialtyDoctor = getColumn(line, SPECIALTY_DOCTOR_KEY).orElse("");
                    specialtyDao.getSpecialty(specialtyName, specialtyDoctor).ifPresent(builder::setSpecialty);
                    String oldDate = getColumn(line, OLD_DATE_KEY).orElse("");
                    if (!oldDate.isEmpty()) {
                        builder.setOldDate(LocalDateTime.parse(oldDate, DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
                    }
                    String initiator = getColumn(line, INITIATOR_KEY).orElse("");
                    userDao.getUser(initiator).ifPresent(builder::setInitiator);
                    appointments.add(builder.build(type));
                }
            }
            return appointments;
        } catch (IOException | CsvValidationException e) {
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

    private Optional<String> getColumn(String[] line, String colName) {
        int colIndex = columnOf(colName);
        if (colIndex >= 0 && colIndex < line.length) {
            return Optional.ofNullable(line[colIndex]);
        }
        return Optional.empty();
    }

    private Predicate<String[]> createOthersFilter(String participant, String stateName) {
        return line -> {
            String patient = getColumn(line, PATIENT_KEY).orElse("");
            String doctor = getColumn(line, DOCTOR_KEY).orElse("");
            String state = getColumn(line, STATE_KEY).orElse("");
            return (participant.equals(patient) || participant.equals(doctor)) && state.equals(stateName);
        };
    }

    private Predicate<String[]> createPendingFilter(String participant) {
        return line -> {
            String patient = getColumn(line, PATIENT_KEY).orElse("");
            String doctor = getColumn(line, DOCTOR_KEY).orElse("");
            String initiator = getColumn(line, INITIATOR_KEY).orElse("");
            String state = getColumn(line, STATE_KEY).orElse("");
            return (patient.equals(participant) || doctor.equals(participant)) && !initiator.equals(participant) && state.equals("p");
        };
    }

    private String getStateName(Class<? extends AppointmentInfo> type) {
        if (type.equals(IncomingInfo.class)) {
            return "s";
        } else if (type.equals(CanceledInfo.class)) {
            return "c";
        } else if (type.equals(ConsumedInfo.class)) {
            return "co";
        } else if (type.equals(PendingInfo.class)) {
            return "p";
        } else {
            throw new IllegalArgumentException("invalid type " + type);
        }
    }

    @Override
    public void save(Appointment appointment) throws PersistentLayerException, InvalidTimeSlot {
        if (appointment.getInfo() instanceof CanceledInfo) {
            cancel(appointment, (CanceledInfo) appointment.getInfo());
        } else if (appointment.getInfo() instanceof IncomingInfo) {
            incoming(appointment, (IncomingInfo) appointment.getInfo());
        } else if (appointment.getInfo() instanceof PendingInfo) {
            pending(appointment, (PendingInfo) appointment.getInfo());
        } else if (appointment.getInfo() instanceof ConsumedInfo) {
            consume(appointment, (ConsumedInfo) appointment.getInfo());
        }
    }

    private void cancel(Appointment appointment, CanceledInfo info) throws PersistentLayerException {
        try (CSVReader reader = new CSVReader(new BufferedReader(new FileReader(filePath)))) {
            List<String[]> lines = reader.readAll();
            Optional<Integer> optional = findLineByKey(
                    lines,
                    appointment.getPatient().getEmail(),
                    appointment.getDoctor().getEmail(),
                    info.getDate());
            if (optional.isEmpty()) {
                return;
            }
            String[] line = lines.remove((int) optional.get());
            writeColumn(line, STATE_KEY, "c");
            Optional<User> initiator = info.getInitiator();
            String initiatorEmail = "";
            if (initiator.isPresent()) {
                initiatorEmail = initiator.get().getEmail();
            }
            writeColumn(line, INITIATOR_KEY, initiatorEmail);
            writeColumn(line, OLD_DATE_KEY, "");
            lines.add(line);
            saveLines(lines);
        } catch (IOException | CsvException e) {
            throw new PersistentLayerException(e);
        }
    }

    private void consume(Appointment appointment, ConsumedInfo info) throws PersistentLayerException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String patient = appointment.getPatient().getEmail();
            String doctor = appointment.getDoctor().getEmail();
            LocalDateTime dateTime = info.getDate();
            List<String[]> lines = reader.readAll();
            Optional<Integer> optional = findLineByKey(lines, patient, doctor, dateTime);
            if (optional.isEmpty()) {
                return;
            }
            int index = optional.get();
            String[] line = lines.remove(index);
            writeColumn(line, STATE_KEY, "co");
            lines.add(line);
            saveLines(lines);
        } catch (IOException | CsvException e) {
            throw new PersistentLayerException(e);
        }
    }

    private void incoming(Appointment appointment, IncomingInfo info) throws PersistentLayerException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String doctorEmail = appointment.getDoctor().getEmail();
            String patientEmail = appointment.getPatient().getEmail();
            LocalDateTime dateTime = info.getDateTime();
            List<String[]> lines = reader.readAll();
            Optional<Integer> optional = findLineByKey(lines, patientEmail, doctorEmail, dateTime);
            if (optional.isEmpty()) {
                return;
            }
            int index = optional.get();
            String[] line = lines.remove(index);
            writeColumn(line, INITIATOR_KEY, "");
            writeColumn(line, OLD_DATE_KEY, "");
            writeColumn(line, STATE_KEY, "s");
            lines.add(line);
            saveLines(lines);
        } catch (IOException | CsvException e) {
            throw new PersistentLayerException(e);
        }
    }

    private void pending(Appointment appointment, PendingInfo info) throws PersistentLayerException, InvalidTimeSlot {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String patient = appointment.getPatient().getEmail();
            String doctor = appointment.getDoctor().getEmail();
            LocalDateTime dateTime = info.getOldDate();
            String initiator = info.getInitiator().getEmail();
            List<String[]> lines = reader.readAll();
            Optional<Integer> optional = findByDate(lines, info.getNewDate());
            if (optional.isPresent()) {
                throw new InvalidTimeSlot(info.getNewDate());
            }
            optional = findLineByKey(lines, patient, doctor, dateTime);
            if (optional.isEmpty()) {
                return;
            }
            int index = optional.get();
            String[] line = lines.remove(index);
            writeColumn(line, STATE_KEY, "p");
            writeColumn(line, DATE_KEY, info.getNewDate().format(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
            writeColumn(line, OLD_DATE_KEY, dateTime.format(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
            writeColumn(line, INITIATOR_KEY, initiator);
            lines.add(line);
            saveLines(lines);
        } catch (IOException | CsvException e) {
            throw new PersistentLayerException(e);
        }
    }

    private void saveLines(List<String[]> lines) throws PersistentLayerException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            for (String[] line : lines) {
                writer.writeNext(line);
            }
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
    }

    private Optional<Integer> findLineByKey(List<String[]> lines, String patient, String doctor, LocalDateTime dateTime) {
        for (int i = 1; i < lines.size(); i++) {
            String[] line = lines.get(i);
            String patient2 = getColumn(line, PATIENT_KEY).orElse("");
            String doctor2 = getColumn(line, DOCTOR_KEY).orElse("");
            String dateCol = getColumn(line, DATE_KEY).orElse("");
            if (dateCol.isEmpty()) {
                continue;
            }
            LocalDateTime dateTime2 = LocalDateTime.parse(dateCol, DateTimeFormatter.ofPattern(DATETIME_PATTERN));
            if (patient.equals(patient2) && doctor.equals(doctor2) && dateTime2.equals(dateTime)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> findByDate(List<String[]> lines, LocalDateTime dateTime) {
        String dateTimeStr = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        for (int i = 0; i < lines.size(); i++) {
            String[] line = lines.get(i);
            String dateTime2 = getColumn(line, DATE_KEY).orElse("");
            if (dateTimeStr.equals(dateTime2)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }
}
