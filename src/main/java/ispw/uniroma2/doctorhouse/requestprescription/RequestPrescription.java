package ispw.uniroma2.doctorhouse.requestprescription;

import ispw.uniroma2.doctorhouse.beans.PrescriptionRequestBean;
import ispw.uniroma2.doctorhouse.dao.RequestDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

public class RequestPrescription {
    private final RequestDao dao;

    public RequestPrescription(RequestDao dao) {
        this.dao = dao;
    }

    public void sendPrescriptionRequest(PrescriptionRequestBean requestBean) throws PersistentLayerException {
        dao.addRequestOfPrescription(requestBean);
    }
}
