package ispw.uniroma2.doctorhouse.dao.responses;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

import java.util.List;
import java.util.Optional;

public interface ResponseDao {


    void insertVisitResponse(ResponseBean responseBean, VisitPrescriptionBean visitPrescriptionBean) throws PersistentLayerException;

    void insertDrugResponse(ResponseBean responseBean, DrugPrescriptionBean drugPrescriptionBean) throws PersistentLayerException;

    Optional<List<ResponsePatientBean>> responseFinder() throws PersistentLayerException;

    void insertRejection(ResponseBean bean) throws PersistentLayerException;
}
