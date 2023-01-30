package ispw.uniroma2.doctorhouse.requestprescription;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.ResponseDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

import java.util.List;
import java.util.Optional;

public class ResponseRequest {
    private final ResponseDao dao;


    public ResponseRequest(ResponseDao dao) {
        this.dao = dao;
    }

    public Optional<List<DoctorRequestBean>> getRequest() throws PersistentLayerException {
        return dao.requestFinder();
    }

    public void insertDrugPrescriptionResponse(ResponseBean responseBean, DrugPrescriptionBean drugPrescriptionBean) throws PersistentLayerException {
        dao.insertDrugResponse(responseBean, drugPrescriptionBean);
    }

    public void insertVisitPrescriptionResponse(ResponseBean responseBean, VisitPrescriptionBean visitPrescriptionBean) throws PersistentLayerException {
        dao.insertVisitResponse(responseBean, visitPrescriptionBean);
    }

}
