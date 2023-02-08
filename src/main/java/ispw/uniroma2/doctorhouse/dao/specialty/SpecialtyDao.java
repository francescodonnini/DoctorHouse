package ispw.uniroma2.doctorhouse.dao.specialty;

import ispw.uniroma2.doctorhouse.beans.SpecialtyBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Specialty;

import java.util.List;
import java.util.Optional;

public interface SpecialtyDao {
    List<Specialty> getSpecialties(int officeId, String doctorEmail) throws PersistentLayerException;
    Optional<Specialty> getSpecialty(String specialtyName, String doctorEmail) throws PersistentLayerException;
    Optional<Specialty> getSpecialty(SpecialtyBean specialtyBean) throws PersistentLayerException;
}
