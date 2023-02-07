package ispw.uniroma2.doctorhouse.dao.requests;

import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.beans.PrescriptionRequestBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

import java.util.List;
import java.util.Optional;

public interface RequestDao {
    void addRequestOfPrescription(PrescriptionRequestBean requestBean) throws PersistentLayerException;
    Optional<List<DoctorRequestBean>> requestFinder() throws PersistentLayerException;

}
