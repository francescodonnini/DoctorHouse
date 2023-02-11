package ispw.uniroma2.doctorhouse.secondinterface.rearrange;

import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.OfficeBean;
import ispw.uniroma2.doctorhouse.beans.PendingAppointmentBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrange;
import ispw.uniroma2.doctorhouse.rearrange.What;
import ispw.uniroma2.doctorhouse.secondinterface.CommandLine;
import ispw.uniroma2.doctorhouse.secondinterface.State;
import ispw.uniroma2.doctorhouse.secondinterface.StateFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DoRearrangeState implements State {
    private static final String COMMANDS = "\\d (y | n)\nback\n";
    private final StateFactory factory;
    private final DoRearrange controller;
    private final UserBean loggedUser;
    private final List<PendingAppointmentBean> beans;

    public DoRearrangeState(StateFactory factory, DoRearrange controller, UserBean loggedUser) {
        this.factory = factory;
        this.controller = controller;
        this.loggedUser = loggedUser;
        beans = new ArrayList<>();
    }


    @Override
    public void onEnter(CommandLine context) {
        showPending(context);
    }

    @Override
    public void enter(CommandLine context, String line) throws PersistentLayerException {
        line = line.trim();
        String[] ts = line.split("\\s+");
        if (ts.length == 1 && ts[0].equals("back")) {
            context.setState(createHomePage());
        } else if (ts.length == 1 && ts[0].equals("help")) {
            context.setResponse(COMMANDS);
        } else if (ts.length == 2) {
            int index = Integer.parseInt(ts[0]);
            if (index >= 0 && index < beans.size()) {
                if (ts[1].equals("y")) {
                    submit(context, beans.get(index), What.CONFIRM);
                } else if (ts[1].equals("n")) {
                    submit(context, beans.get(index), What.CANCEL);
                } else {
                    context.setResponse("expected either y or n, got " + ts[1]);
                }
            }
        } else {
            invalidCommand(context, line);
        }
    }

    private void invalidCommand(CommandLine context, String line) {
        context.setResponse("invalid command " + line);
        showPending(context);
    }

    private State createHomePage() {
        if (loggedUser instanceof DoctorBean) {
            return factory.createDoctorHomePageState(loggedUser);
        } else {
            return factory.createUserHomePageState(loggedUser);
        }
    }

    private void submit(CommandLine context, PendingAppointmentBean bean, What what) {
        try {
            controller.submit(bean, what);
            context.setState(factory.createUserHomePageState(loggedUser));
        } catch (InvalidTimeSlot e) {
            //
        } catch (PersistentLayerException e) {
            context.setState(createHomePage());
        }
    }

    private void showPending(CommandLine context) {
        beans.clear();
        try {
            beans.addAll(controller.getPendingAppointments());
            if (beans.isEmpty()) {
                context.setResponse("You do not have any pending appointments\n");
                return;
            }
            StringBuilder s = new StringBuilder("please select a number between 1 and " + beans.size() + "\n");
            int k = 0;
            for (PendingAppointmentBean p : beans) {
                s.append(k).append(") ").append(stringify(p)).append("\n");
                k++;
            }
            context.setResponse(s.toString());
        } catch (PersistentLayerException e) {
            context.setState(createHomePage());
        }
    }

    private String stringify(PendingAppointmentBean bean) {
        return "patient=" +
                stringify(bean.getPatient()) +
                " doctor=" +
                stringify(bean.getDoctor()) +
                " office=" +
                stringify(bean.getOffice()) +
                " old-date=" +
                stringify(bean.getOldDateTime(), bean.getSpecialty().getDuration()) +
                " new-date=" +
                stringify(bean.getDateTime(), bean.getSpecialty().getDuration()) +
                " specialty=" +
                bean.getSpecialty().getName();
    }

    private String stringify(UserBean userBean) {
        return userBean.getEmail() +
                " " +
                userBean.getFirstName() +
                " " +
                userBean.getLastName();
    }

    private String stringify(DoctorBean doctorBean) {
        return doctorBean.getEmail() +
                " " +
                doctorBean.getLastName() +
                " " +
                doctorBean.getFirstName() +
                " " +
                doctorBean.getField();
    }

    private String stringify(OfficeBean officeBean) {
        return officeBean.getCountry() +
                " " +
                officeBean.getProvince() +
                " " +
                officeBean.getCity() +
                " " +
                officeBean.getAddress();
    }

    private String stringify(LocalDateTime dateTime, Duration duration) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) +
                " - " +
                duration;
    }
}
