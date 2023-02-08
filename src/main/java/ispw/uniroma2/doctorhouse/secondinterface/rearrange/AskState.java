package ispw.uniroma2.doctorhouse.secondinterface.rearrange;

import ispw.uniroma2.doctorhouse.beans.AppointmentBean;
import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrange;
import ispw.uniroma2.doctorhouse.secondinterface.CommandLine;
import ispw.uniroma2.doctorhouse.secondinterface.State;
import ispw.uniroma2.doctorhouse.secondinterface.StateFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AskState implements State {
    private final StateFactory factory;
    private final AskForRearrange controller;
    private final UserBean loggedUser;
    private final List<AppointmentBean> beans;

    public AskState(StateFactory factory, AskForRearrange controller, UserBean loggedUser) {
        this.factory = factory;
        this.controller = controller;
        this.loggedUser = loggedUser;
        beans = new ArrayList<>();
    }

    @Override
    public void onEnter(CommandLine context) {
        refresh(context);
    }

    @Override
    public void enter(CommandLine context, String line) throws PersistentLayerException {
        line = line.trim();
        if (line.equals("help")) {
            context.setResponse("refresh\n%d start-date [end-date]\n");
        } else if (line.equals("refresh")) {
            refresh(context);
        } else if (!beans.isEmpty()) {
            String[] tokens = line.split("\\s+");
            String fromStr;
            String toStr;
            switch (tokens.length) {
                case 2:
                    fromStr = tokens[1];
                    toStr = null;
                    break;
                case 3:
                    fromStr = tokens[1];
                    toStr = tokens[2];
                    break;
                default:
                    context.setResponse("invalid command " + line);
                    return;
            }
            int index = Integer.parseInt(tokens[0]);
            if (index < 0 || index >= beans.size()) {
                context.setResponse("invalid index " + index);
                return;
            }
            LocalDate from = LocalDate.parse(fromStr);
            LocalDate to = toStr == null ? from.plusDays(7) : LocalDate.parse(toStr);
            context.setState(factory.createSelectDateState(beans.get(index), from, to, loggedUser));
        } else {
            context.setResponse("invalid command " + line);
        }
    }

    public void refresh(CommandLine context) {
        try {
            beans.clear();
            beans.addAll(controller.getIncomingAppointments());
            StringBuilder s = new StringBuilder();
            if (beans.isEmpty()) {
                s.append("You do not have any incoming appointments");
            } else {
                s = new StringBuilder("please insert a number between 1 and " + beans.size()).append("\n");
                for (int i = 0; i < beans.size(); ++i) {
                    s.append(i + 1).append(") ").append(stringify(beans.get(i))).append("\n");
                }
            }
            context.setResponse(s.toString());
        } catch (PersistentLayerException e) {
            context.setResponse("smth went wrong");
            goBackHome(context);
        }
    }

    private void goBackHome(CommandLine context) {
        if (isDoctor()) {
            context.setState(factory.createDoctorHomePageState(loggedUser));
        } else {
            context.setState(factory.createUserHomePageState(loggedUser));
        }
    }

    private boolean isDoctor() {
        return loggedUser instanceof DoctorBean;
    }

    private String stringify(AppointmentBean a) {
        return new StringBuilder()
                .append("patient=")
                .append(stringify(a.getPatient()))
                .append(" doctor=")
                .append(stringify(a.getDoctor()))
                .append(" date=")
                .append(a.getDateTime().format(DateTimeFormatter.ISO_DATE_TIME))
                .append(" specialty=")
                .append(a.getSpecialty().getName())
                .toString();
    }

    private String stringify(UserBean u) {
        return new StringBuilder()
                .append("email=")
                .append(u.getEmail())
                .append(" name=")
                .append(u.getFirstName())
                .append(" first-name=")
                .append(u.getFirstName())
                .toString();
    }

    private String stringify(DoctorBean d) {
        return new StringBuilder()
                .append("email=")
                .append(d.getEmail())
                .append(" name=")
                .append(d.getFirstName())
                .append(" first-name=")
                .append(d.getFirstName())
                .append(" field=")
                .append(d.getField())
                .toString();
    }
}
