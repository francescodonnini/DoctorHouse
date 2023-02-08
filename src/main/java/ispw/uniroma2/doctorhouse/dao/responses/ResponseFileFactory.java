package ispw.uniroma2.doctorhouse.dao.responses;

import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDao;

public class ResponseFileFactory implements ResponseDaoFactory{
    private final PrescriptionDao prescriptionDao;

    public ResponseFileFactory(PrescriptionDao prescriptionDao) {
        this.prescriptionDao = prescriptionDao;
    }

    @Override
    public ResponseDao create() {
        return new ResponseFile(prescriptionDao);
    }
}
