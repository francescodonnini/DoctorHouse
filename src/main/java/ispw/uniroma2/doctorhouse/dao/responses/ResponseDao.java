package ispw.uniroma2.doctorhouse.dao.responses;

import ispw.uniroma2.doctorhouse.beans.*;
import ispw.uniroma2.doctorhouse.dao.exceptions.NotValidRequest;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Response;

import java.util.List;
import java.util.Optional;

public interface ResponseDao {

    void insertResponse(ResponseBean responseBean, PrescriptionBean prescriptionBean) throws PersistentLayerException, NotValidRequest;

    Optional<List<Response>> responseFinder() throws PersistentLayerException;

    void insertRejection(ResponseBean bean) throws PersistentLayerException;
}
