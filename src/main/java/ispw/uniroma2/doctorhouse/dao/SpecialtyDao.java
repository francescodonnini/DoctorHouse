package ispw.uniroma2.doctorhouse.dao;

import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.beans.SpecialtyBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Specialty;

import java.util.List;
import java.util.Optional;

public interface SpecialtyDao {
    List<Specialty> getSpecialties(OfficeBean office) throws PersistentLayerException;
    Optional<Specialty> getSpecialty(String specialtyName, DoctorBean doctorBean) throws PersistentLayerException;
    Optional<Specialty> getSpecialty(SpecialtyBean specialtyBean) throws PersistentLayerException;
}
