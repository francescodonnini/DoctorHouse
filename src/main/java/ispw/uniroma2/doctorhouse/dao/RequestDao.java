package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.beans.PrescriptionRequestBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

public interface RequestDao {
    void addRequestOfPrescription(PrescriptionRequestBean requestBean) throws PersistentLayerException;

}
