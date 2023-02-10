package ispw.uniroma2.doctorhouse.dao.prescriptions;

import ispw.uniroma2.doctorhouse.Main;

public class PrescriptionFileFactory implements PrescriptionDaoFactory{
    @Override
    public PrescriptionDao create() {
        String path = Main.APP_DIR_PATH +"/prescriptions.csv";
        return new PrescriptionFile(path);
    }
}
