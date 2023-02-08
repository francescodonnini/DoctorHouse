package ispw.uniroma2.doctorhouse.dao.office;

import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Office;

import java.util.List;
import java.util.Optional;

public interface OfficeDao {
    Optional<Office> getOffice(int officeId, String doctorEmail) throws PersistentLayerException;
    List<Office> getOffices(String email) throws PersistentLayerException;
}
