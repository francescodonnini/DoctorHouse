package ispw.uniroma2.doctorhouse.dao.appointment;

import com.opencsv.CSVWriter;
import ispw.uniroma2.doctorhouse.Main;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.specialty.SpecialtyDao;
import ispw.uniroma2.doctorhouse.dao.users.UserDao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AppointmentFileFactory implements AppointmentDaoFactory {
    private final OfficeDao officeDao;
    private final SpecialtyDao specialtyDao;
    private final UserDao userDao;

    public AppointmentFileFactory(OfficeDao officeDao, SpecialtyDao specialtyDao, UserDao userDao) {
        this.officeDao = officeDao;
        this.specialtyDao = specialtyDao;
        this.userDao = userDao;
    }

    @Override
    public AppointmentDao create() throws PersistentLayerException {
        String filePath = Main.APP_DIR_PATH + "/appointments.csv";
        try {
            File file = new File(filePath);
            if (!file.exists()) {
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
