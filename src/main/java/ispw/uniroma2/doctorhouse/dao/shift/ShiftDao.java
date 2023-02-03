package ispw.uniroma2.doctorhouse.dao.shift;

import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Shift;

import java.util.List;

public interface ShiftDao {
    List<Shift> getShifts(OfficeBean office) throws PersistentLayerException;
}
