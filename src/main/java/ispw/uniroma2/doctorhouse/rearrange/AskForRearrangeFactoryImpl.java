package ispw.uniroma2.doctorhouse.rearrange;

import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;
import ispw.uniroma2.doctorhouse.dao.slot.SlotDao;

public class AskForRearrangeFactoryImpl implements AskForRearrangeFactory {
    private final AppointmentDao appointmentDao;
    private final OfficeDao officeDao;
    private final SlotDao slotDao;

    public AskForRearrangeFactoryImpl(AppointmentDao appointmentDao, OfficeDao officeDao, SlotDao slotDao) {
        this.appointmentDao = appointmentDao;
        this.officeDao = officeDao;
        this.slotDao = slotDao;
    }

    @Override
    public AskForRearrange create() {
        return new AskForRearrangeImpl(appointmentDao, slotDao, officeDao);
    }
}
