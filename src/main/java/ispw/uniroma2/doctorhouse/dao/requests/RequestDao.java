package ispw.uniroma2.doctorhouse.dao.requests;

import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestDao {
    void addRequestOfPrescription(String message) throws PersistentLayerException;
    Optional<List<Request>> requestFinder() throws PersistentLayerException;

}
