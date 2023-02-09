package ispw.uniroma2.doctorhouse.requestprescription;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.NotValidRequest;
import ispw.uniroma2.doctorhouse.dao.requests.RequestDao;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Request;

import java.util.ArrayList;
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
        List<DoctorRequestBean> doctorRequestBeans = new ArrayList<>();
        Optional<List<Request>> requests = requestDao.requestFinder();
        requests.orElseThrow().forEach(f -> {
            DoctorRequestBean doctorRequestBean = new DoctorRequestBean();
            doctorRequestBean.setId(f.getId());
            doctorRequestBean.setPatient(f.getPatientEmail());
            doctorRequestBean.setMessage(f.getMessage());
            doctorRequestBeans.add(doctorRequestBean);
        });
        return Optional.of(doctorRequestBeans);
    }

    public void insertResponse(ResponseBean responseBean, PrescriptionBean prescriptionBean) throws PersistentLayerException, NotValidRequest {
        dao.insertResponse(responseBean, prescriptionBean);
    }


    public void insertRejection(ResponseBean responseBean) throws PersistentLayerException {
        dao.insertRejection(responseBean);
    }

}
