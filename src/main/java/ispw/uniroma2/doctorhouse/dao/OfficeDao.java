package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.model.Office;

import java.util.List;

public interface OfficeDao {
    List<Office> getOffices(DoctorBean doctor);
}
