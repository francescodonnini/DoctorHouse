package ispw.uniroma2.doctorhouse.rearrange;

import ispw.uniroma2.doctorhouse.dao.office.OfficeDao;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;

public class AskForRearrangeFactoryImpl implements AskForRearrangeFactory {
    private final AppointmentDao appointmentDao;
    private final OfficeDao officeDao;

    public AskForRearrangeFactoryImpl(AppointmentDao appointmentDao, OfficeDao officeDao) {
        int b;
        this.appointmentDao = appointmentDao;
        this.officeDao = officeDao;
    }

    @Override
    public AskForRearrange create() {
        return new AskForRearrangeImpl(appointmentDao, officeDao);
    }
}
