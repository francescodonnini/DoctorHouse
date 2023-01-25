package ispw.uniroma2.doctorhouse.requestprescription;

import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.dao.ResponseDao;
import ispw.uniroma2.doctorhouse.view.exception.RequestNotFound;

import java.util.List;
import java.util.Optional;

public class ResponseRequest {
    private final ResponseDao dao;

    public ResponseRequest(ResponseDao dao) {
        this.dao = dao;
    }

    public Optional<List<DoctorRequestBean>> getRequest() throws RequestNotFound {
        return dao.requestFinder();
    }
}