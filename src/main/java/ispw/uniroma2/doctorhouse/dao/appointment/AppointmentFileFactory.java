package ispw.uniroma2.doctorhouse.dao.appointment;

import com.opencsv.CSVWriter;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.users.UserDao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class AppointmentFileFactory implements AppointmentDaoFactory {
    private final String filePath;
    private final OfficeDao officeDao;
    private final SpecialtyDao specialtyDao;
    private final UserDao userDao;

    public AppointmentFileFactory(OfficeDao officeDao, SpecialtyDao specialtyDao, UserDao userDao) {
        this.filePath = "appointments.csv";
        this.officeDao = officeDao;
        this.specialtyDao = specialtyDao;
        this.userDao = userDao;
    }

    @Override
    public AppointmentDao create() throws PersistentLayerException {
        File file = new File(Objects.requireNonNull(getClass().getResource("appointments.csv")).getPath());
        try {
            if (file.length() == 0) {
                initCsvFile(file);
            }
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
        return new AppointmentFile(filePath, officeDao, specialtyDao, userDao);
    }

    private void initCsvFile(File file) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        writer.writeNext(new String[]{"patient", "doctor", "date", "specialty-name", "specialty-doctor", "office", "state", "old-date", "initiator"});
        writer.close();
    }
}
