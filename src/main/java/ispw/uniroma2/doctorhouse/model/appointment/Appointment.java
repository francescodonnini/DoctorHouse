package ispw.uniroma2.doctorhouse.model.appointment;

import ispw.uniroma2.doctorhouse.model.User;

import java.time.LocalDateTime;

/**
 * Interface for appointments. <a href="#{@link Appointment#cancel(User)}">cancel</a>, <a href="#{@link Appointment#confirm(User)}">confirm</a>, <a href="#{@link Appointment#reschedule(User, LocalDateTime)}">reschedule</a> behaves
 * differently depending on the state the appointment is in.
 */
public interface Appointment {
    /**
     * @param initiator credentials who accepts the request by the other participant to rearrange the appointment to a new date and time
     */
    void confirm(User initiator);

    /**
     * @param initiator credentials who does not accept the request by the other participant to rearrange the appointment to a new date and time
     */
    void cancel(User initiator);

    /**
     * @param initiator credentials who requests to the other participant to move the appointment to newDate
     * @param newDate   the date initiator wishes to rearrange the appointment
     */
    void reschedule(User initiator, LocalDateTime newDate);

    /**
     * Returns an <a href="#{@link AppointmentInfo}">AppointmentInfo</a> object that contains all the information about an appointment such as the patient,
     * the doctor (participants of an appointment), the office, the visit that the doctor is going to perform to the patient and the date (and time)
     * the appointment will happen. There could be additional information depending on the state of the Appointment is in.
     *
     * @return a concrete implementation of AppointmentInfo class depending on the state the Appointment is in
     */
    AppointmentInfo getInfo();

    AppointmentMemento createMemento();

    void restore(AppointmentMemento memento);
}
