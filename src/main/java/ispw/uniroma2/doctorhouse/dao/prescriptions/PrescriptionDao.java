package ispw.uniroma2.doctorhouse.dao.prescriptions;

import ispw.uniroma2.doctorhouse.beans.DrugPrescriptionBean;
import ispw.uniroma2.doctorhouse.beans.VisitPrescriptionBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

public interface PrescriptionDao {
    int addDrugPrescription(DrugPrescriptionBean bean) throws PersistentLayerException;

    int addVisitPrescription(VisitPrescriptionBean bean) throws PersistentLayerException;
}
