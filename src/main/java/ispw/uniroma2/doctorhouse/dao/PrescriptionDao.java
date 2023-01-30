package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.beans.DrugPrescriptionBean;
import ispw.uniroma2.doctorhouse.beans.VisitPrescriptionBean;

public interface PrescriptionDao {
    int addDrugPrescription(DrugPrescriptionBean bean);

    int addVisitPrescription(VisitPrescriptionBean bean);
}
