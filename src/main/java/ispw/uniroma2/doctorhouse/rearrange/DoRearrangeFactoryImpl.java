package ispw.uniroma2.doctorhouse.rearrange;

import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;

public class DoRearrangeFactoryImpl implements DoRearrangeFactory {
    private final AppointmentDao appointmentDao;

    public DoRearrangeFactoryImpl(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    @Override
    public DoRearrange create() {
        return new DoRearrangeImpl(appointmentDao);
    }
}
