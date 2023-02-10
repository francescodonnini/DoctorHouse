package ispw.uniroma2.doctorhouse.requestprescription;

import ispw.uniroma2.doctorhouse.beans.ResponsePatientBean;
import ispw.uniroma2.doctorhouse.dao.requests.RequestDao;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestPrescription {
    private final RequestDao dao;
    private final ResponseDao responseDao;

    public RequestPrescription(RequestDao dao, ResponseDao responseDao) {
        this.dao = dao;
        this.responseDao = responseDao;
    }

    public void sendPrescriptionRequest(String message) throws PersistentLayerException {
        dao.addRequestOfPrescription(message);
    }

    public Optional<List<ResponsePatientBean>> getResponse() throws PersistentLayerException {
        List<ResponsePatientBean> responsePatientBeans = new ArrayList<>();
        Optional<List<Response>> responses = responseDao.responseFinder();
        responses.orElseThrow().forEach(f -> {
            ResponsePatientBean responsePatientBean = new ResponsePatientBean();
            responsePatientBean.setMessage(f.getMessage());
            if(f.getPrescription() != null) {
                responsePatientBean.setKind(f.getPrescription().getKind());
                responsePatientBean.setName(f.getPrescription().getName());
                responsePatientBean.setQuantity(f.getPrescription().getQuantity());
            } else {
                responsePatientBean.setKind("");
                responsePatientBean.setName("");
                responsePatientBean.setQuantity(0);
            }
            responsePatientBeans.add(responsePatientBean);
        });
        return Optional.of(responsePatientBeans);
    }
}
