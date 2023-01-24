package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.model.Specialty;

import java.util.List;

public interface SpecialtyDao {
    List<Specialty> getSpecialties(OfficeBean office);
}
