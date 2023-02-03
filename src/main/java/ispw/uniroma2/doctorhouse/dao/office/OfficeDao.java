package ispw.uniroma2.doctorhouse.dao.office;

import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Office;

import java.util.List;
import java.util.Optional;

public interface OfficeDao {
    Optional<Office> getOffice(OfficeBean office) throws PersistentLayerException;
    List<Office> getOffices(DoctorBean doctor) throws PersistentLayerException;
}
