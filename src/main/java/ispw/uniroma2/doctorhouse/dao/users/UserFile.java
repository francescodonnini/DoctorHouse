package ispw.uniroma2.doctorhouse.dao.users;

import com.opencsv.*;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvValidationException;
import ispw.uniroma2.doctorhouse.auth.exceptions.DuplicateEmail;
import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.LoginRequestBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.beans.UserRegistrationRequestBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.model.*;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserFile implements UserDao {
    private static final int USER_BIRTHDATE_COL = 0;
    private static final int USER_FISCAL_CODE_COL = 1;
    private static final int USER_FIRST_NAME_COL = 2;
    private static final int USER_EMAIL_COL = 3;
    private static final int USER_GENDER_COL = 4;
    private static final int USER_LAST_NAME_COL = 5;
    private static final int USER_PASSWORD_HASH_COL = 6;
    private static final int USER_FAMILY_DOCTOR_COL = 7;
    private static final int DOCT_FIELD_COL = 8;
    private final String filePath;
    private final OfficeDao officeDao;
    public UserFile(OfficeDao officeDao) {
        this.filePath = Objects.requireNonNull(getClass().getResource("users.csv")).getPath();
        this.officeDao = officeDao;
    }

    @Override
    public Optional<User> get(LoginRequestBean loginRequest) throws PersistentLayerException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                String email = line[USER_EMAIL_COL];
                String password = line[USER_PASSWORD_HASH_COL];
                if (authenticate(email, loginRequest.getEmail(), password, loginRequest.getPassword())) {
                    DoctorBean doctorBean = new DoctorBean();
                    doctorBean.setEmail(line[USER_FAMILY_DOCTOR_COL]);
                    Doctor doctor = getDoctor(doctorBean).orElse(null);
                    String field = line[DOCT_FIELD_COL];
                    if (field == null) {
                        return Optional.of(new User(email, fromCSVLine(line), doctor));
                    } else {
                        doctorBean.setEmail(email);
                        List<Office> offices = officeDao.getOffices(doctorBean);
                        return Optional.of(new Doctor(email, fromCSVLine(line), doctor, field, offices));
                    }
                }
            }
            return Optional.empty();
        } catch (IOException | CsvValidationException | NoSuchAlgorithmException e) {
            throw new PersistentLayerException(e);
        }
    }

    private boolean authenticate(String email1, String email2, String passwordHash, String password) throws NoSuchAlgorithmException {
        return email1.equals(email2) && passwordHash.equals(getSHA512(password));
    }

    private String getSHA512(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] bytes = messageDigest.digest(password.getBytes());
        BigInteger hash = new BigInteger(1, bytes);
        StringBuilder s = new StringBuilder(hash.toString(16));
        while (s.length() < 32) {
            s.insert(0, s);
        }
        return s.toString();
    }

    @Override
    public void create(UserRegistrationRequestBean registrationRequest) throws DuplicateEmail, PersistentLayerException {
        try (ICSVWriter writer = getWriter()) {
            String familyDoctorEmail = "";
            Optional<DoctorBean> bean = registrationRequest.getFamilyDoctor();
            if (bean.isPresent()) {
                Optional<Doctor> doctor = getDoctor(bean.get());
                if (doctor.isPresent()) {
                    familyDoctorEmail = doctor.get().getEmail();
                }
            }
            writer.writeNext(new String[]{
                    registrationRequest.getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    registrationRequest.getFiscalCode(),
                    registrationRequest.getFirstName(),
                    registrationRequest.getEmail(),
                    String.valueOf(registrationRequest.getGender().isoCode),
                    registrationRequest.getLastName(),
                    getSHA512(registrationRequest.getPassword()),
                    familyDoctorEmail
            });
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new PersistentLayerException(e);
        }
    }

    private ICSVWriter getWriter() throws IOException {
        return new CSVWriterBuilder(new FileWriter(filePath))
                .build();
    }

    @Override
    public Optional<User> getUser(UserBean userBean) throws PersistentLayerException {
        List<User> user = getUserBy(List.of(Field.of(USER_EMAIL_COL, userBean.getEmail())));
        if (user.size() == 1) {
            return Optional.of(user.get(0));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Doctor> getDoctor(DoctorBean doctorBean) throws PersistentLayerException {
        List<Doctor> doctor = getDoctorBy(List.of(Field.of(USER_EMAIL_COL, doctorBean.getEmail())));
        if (doctor.size() == 1) {
            return Optional.of(doctor.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Doctor> getByField(String field) throws PersistentLayerException {
        return getDoctorBy(List.of(Field.of(DOCT_FIELD_COL, field)));
    }

    private CSVReader getReader() throws FileNotFoundException {
        return new CSVReaderBuilder(new FileReader(filePath))
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .build();
    }

    private List<Doctor> getDoctorBy(List<Field> fields) throws PersistentLayerException {
        try (CSVReader reader = getReader()) {
            List<Doctor> users = new ArrayList<>();
            String[] line;
            while ((line = reader.readNext()) != null) {
                String field = line[DOCT_FIELD_COL];
                if (field != null && check(line, fields)) {
                    String email = line[USER_EMAIL_COL];
                    DoctorBean doctorBean = new DoctorBean();
                    doctorBean.setEmail(email);
                    users.add(new Doctor(email, fromCSVLine(line), null, field, officeDao.getOffices(doctorBean)));
                }
            }
            return users;
        } catch (IOException | CsvValidationException e) {
            throw new PersistentLayerException(e);
        }
    }

    private List<User> getUserBy(List<Field> fields) throws PersistentLayerException {
        try (CSVReader reader = getReader()){
            List<User> users = new ArrayList<>();
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (check(line, fields)) {
                    String email = line[USER_EMAIL_COL];
                    DoctorBean doctorBean = new DoctorBean();
                    doctorBean.setEmail(line[USER_FAMILY_DOCTOR_COL]);
                    Optional<Doctor> doctor = getDoctor(doctorBean);
                    users.add(new User(email, fromCSVLine(line), doctor.orElse(null)));
                }
            }
            return users;
        } catch (IOException | CsvValidationException e) {
            throw new PersistentLayerException(e);
        }
    }

    private Person fromCSVLine(String[] line) {
        LocalDate birthDate = LocalDate.parse(line[USER_BIRTHDATE_COL]);
        String fiscalCode = line[USER_FISCAL_CODE_COL];
        String firstName = line[USER_FIRST_NAME_COL];
        Gender gender = Gender.from(Integer.parseInt(line[USER_GENDER_COL])).orElse(Gender.NOT_KNOWN);
        String lastName = line[USER_LAST_NAME_COL];
        return new Person(birthDate, fiscalCode, firstName, lastName, gender);
    }

    private boolean check(String[] line, List<Field> fields) {
        for (Field field : fields) {
            if (!line[field.getFieldIndex()].equals(field.getFieldValue())) {
                return false;
            }
        }
        return true;
    }

    private static class Field {
        private final int fieldIndex;
        private final String fieldValue;

        public static Field of(int fieldIndex, String fieldValue) {
            return new Field(fieldIndex, fieldValue);
        }

        private Field(int fieldIndex, String fieldValue) {
            this.fieldIndex = fieldIndex;
            this.fieldValue = fieldValue;
        }

        public int getFieldIndex() {
            return fieldIndex;
        }

        public String getFieldValue() {
            return fieldValue;
        }
    }
}
