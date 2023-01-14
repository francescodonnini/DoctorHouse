package ispw.uniroma2.doctorhouse.model.doctor;

public interface OfficeMgr {
    void addSpecialty(OfficeImpl office, Specialty specialty);
    void removeSpecialty(OfficeImpl office, Specialty specialty);
}
