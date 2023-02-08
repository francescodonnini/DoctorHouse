package ispw.uniroma2.doctorhouse.requestprescription;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.NotValidRequest;
import ispw.uniroma2.doctorhouse.dao.requests.RequestDao;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

import java.util.List;
import java.util.Optional;

public class ResponseRequest {
    private final ResponseDao dao;

    private final RequestDao requestDao;


    public ResponseRequest(ResponseDao dao, RequestDao requestDao) {
        this.dao = dao;
        this.requestDao = requestDao;
    }

    public Optional<List<DoctorRequestBean>> getRequest() throws PersistentLayerException {
        return requestDao.requestFinder();
    }

    public void insertDrugPrescriptionResponse(ResponseBean responseBean, DrugPrescriptionBean drugPrescriptionBean) throws PersistentLayerException, NotValidRequest {
        dao.insertDrugResponse(responseBean, drugPrescriptionBean);
    }

    public void insertVisitPrescriptionResponse(ResponseBean responseBean, VisitPrescriptionBean visitPrescriptionBean) throws PersistentLayerException {
        dao.insertVisitResponse(responseBean, visitPrescriptionBean);
    }

    public void insertRejection(ResponseBean responseBean) throws PersistentLayerException {
        dao.insertRejection(responseBean);
    }

}
