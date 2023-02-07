package ispw.uniroma2.doctorhouse.requestprescription;

import ispw.uniroma2.doctorhouse.beans.PrescriptionRequestBean;
import ispw.uniroma2.doctorhouse.beans.ResponsePatientBean;
import ispw.uniroma2.doctorhouse.dao.requests.RequestDao;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

import java.util.List;
import java.util.Optional;

public class RequestPrescription {
    private final RequestDao dao;
    private final ResponseDao responseDao;

    public RequestPrescription(RequestDao dao, ResponseDao responseDao) {
        this.dao = dao;
        this.responseDao = responseDao;
    }

    public void sendPrescriptionRequest(PrescriptionRequestBean requestBean) throws PersistentLayerException {
        dao.addRequestOfPrescription(requestBean);
    }

    public Optional<List<ResponsePatientBean>> getResponse() throws PersistentLayerException {
        return responseDao.responseFinder();
    }
}
