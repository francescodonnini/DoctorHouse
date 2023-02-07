package ispw.uniroma2.doctorhouse.dao.prescriptions;

public class PrescriptionFileFactory implements PrescriptionDaoFactory{
    @Override
    public PrescriptionDao create() {
        return new PrescriptionFile();
    }
}
