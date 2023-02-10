package ispw.uniroma2.doctorhouse.dao.responses;

import ispw.uniroma2.doctorhouse.Main;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDao;

public class ResponseFileFactory implements ResponseDaoFactory{
    private final PrescriptionDao prescriptionDao;


    public ResponseFileFactory(PrescriptionDao prescriptionDao) {
        this.prescriptionDao = prescriptionDao;
    }


    @Override
    public ResponseDao create() {
        String filePath = Main.APP_DIR_PATH + "/response.csv";
        return new ResponseFile(prescriptionDao, filePath);
    }
}
