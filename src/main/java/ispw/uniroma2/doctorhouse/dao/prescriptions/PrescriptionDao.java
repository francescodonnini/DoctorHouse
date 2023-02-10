package ispw.uniroma2.doctorhouse.dao.prescriptions;

import ispw.uniroma2.doctorhouse.beans.PrescriptionBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Prescription;

public interface PrescriptionDao {
    int savePrescription(PrescriptionBean bean) throws PersistentLayerException;
    Prescription getPrescription(int prescriptionId) throws PersistentLayerException;
}
