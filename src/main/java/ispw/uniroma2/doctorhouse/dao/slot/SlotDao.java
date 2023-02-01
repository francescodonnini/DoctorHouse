package ispw.uniroma2.doctorhouse.dao.slot;

import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.TakenSlot;

import java.util.List;

public interface SlotDao {
    List<TakenSlot> getSlots(OfficeBean bean) throws PersistentLayerException;
}
