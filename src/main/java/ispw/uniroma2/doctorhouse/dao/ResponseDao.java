package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.view.exception.RequestNotFound;

import java.util.List;
import java.util.Optional;

public interface ResponseDao {
    Optional<List<DoctorRequestBean>> requestFinder() throws RequestNotFound;
}
