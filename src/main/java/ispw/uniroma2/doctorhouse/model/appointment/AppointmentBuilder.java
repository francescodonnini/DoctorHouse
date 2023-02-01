package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.Doctor;
import ispw.uniroma2.doctorhouse.model.Office;
import ispw.uniroma2.doctorhouse.model.Specialty;
import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

/**
 * Defines an API to obtain objects which implement Appointment interface
 */
public interface AppointmentBuilder {
    /**
     * Set the patient who will participate/has participated in the appointment
     */
    void setPatient(User user);

    /**
     * Set the doctor who will participate/has participated in the appointment
     */
    void setDoctor(Doctor doctor);

    /**
     * Set the kind of visit the doctor will perform/has performed to the patient
     */
    void setSpecialty(Specialty specialty);

    /**
     * Set the date the doctor is going to perform/has performed to the patient
     */
    void setDate(LocalDateTime date);

    /**
     * Set the date either the doctor or the patient wish to rearrange an incoming appointment
     */
    void setOldDate(LocalDateTime oldDate);

    /**
     * Set the office the appointment is going to take/has taken place
     */
    void setOffice(Office office);

    /**
     * Set the participant who:
     * - has requested the appointment to be rearranged
     * - has canceled the incoming appointment
     */
    void setInitiator(User user);

    /**
     * Build an object that implements the Appointment interface depending on the @param type given
     * @param type Class name of a concrete implementation of Appointment
     * @return an object that implements the Appointment interface
     * @throws IllegalStateException if not enough information has been given to the builder by using the appropriate setters
     */
    Appointment build(Class<? extends AppointmentInfo> type);

    /**
     * Set all the information used by the builder to build appointment objects to null. This method make an instance of AppointmentBuilder
     * reusable to build more appointment objects
     */
    void reset();
}
