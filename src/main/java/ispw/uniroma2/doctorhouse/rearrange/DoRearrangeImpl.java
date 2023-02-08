package ispw.uniroma2.doctorhouse.rearrange;

import ispw.uniroma2.doctorhouse.beans.PendingAppointmentAdapter;
import ispw.uniroma2.doctorhouse.beans.PendingAppointmentBean;
import ispw.uniroma2.doctorhouse.dao.appointment.AppointmentDao;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Session;
import ispw.uniroma2.doctorhouse.model.appointment.Appointment;
import ispw.uniroma2.doctorhouse.model.appointment.PendingInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoRearrangeImpl implements DoRearrange {
    private final AppointmentDao appointmentDao;
    private final Map<PendingAppointmentBean, Appointment> appointmentMap;

    public DoRearrangeImpl(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
        appointmentMap = new HashMap<>();
    }

    @Override
    public void submit(PendingAppointmentBean pendingAppointmentBean, What what) throws InvalidTimeSlot, PersistentLayerException {
        Appointment appointment = appointmentMap.get(pendingAppointmentBean);
        if (What.CONFIRM.equals(what)) {
            appointment.confirm(Session.getSession().getUser());
        } else {
            appointment.cancel(Session.getSession().getUser());
        }
        appointmentDao.save(appointment);
    }

    @Override
    public List<PendingAppointmentBean> getPendingAppointments() throws PersistentLayerException {
        String email = Session.getSession().getUser().getEmail();
        appointmentMap.clear();
        appointmentDao.find(email, PendingInfo.class).forEach(a -> appointmentMap.put(new PendingAppointmentAdapter(a), a));
        return new ArrayList<>(appointmentMap.keySet());
    }
}
