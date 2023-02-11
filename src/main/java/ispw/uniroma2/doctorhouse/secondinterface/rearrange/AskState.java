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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AskState implements State {
    private static final String DATE_PATTERN = "dd-MM-yyyy";
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
            context.setResponse("back\nrefresh\n%d dd-MM-yyyy [dd-MM-yyyy]\n");
        } else if (line.equals("back")) {
            goBackHome(context);
        } else if (line.equals("refresh")) {
            refresh(context);
        } else if (!beans.isEmpty()) {
            parseCommand(context, line);
        } else {
            context.setResponse(invalidCommand(line));
        }
    }

    private void parseCommand(CommandLine context, String line) {
        String[] tokens = line.split("\\s+");
        if (tokens.length < 2 || tokens.length > 3) {
            context.setResponse(invalidCommand(line));
            return;
        }
        if (!tokens[0].matches("\\d")) {
            context.setResponse("expected number, got " + line);
            return;
        }
        int index = Integer.parseInt(tokens[0]);
        if (index < 0 || index >= beans.size()) {
            context.setResponse("invalid index " + index);
            return;
        }
        try {
            LocalDate from = LocalDate.parse(tokens[1], DateTimeFormatter.ofPattern(DATE_PATTERN));
            LocalDate to = from.plusDays(7);
            if (tokens.length == 3) {
                to = LocalDate.parse(tokens[2], DateTimeFormatter.ofPattern(DATE_PATTERN));
            }
            context.setState(factory.createSelectDateState(controller, beans.get(index), from, to, loggedUser));
        } catch (DateTimeParseException ignored) {
            context.setResponse(invalidCommand(line));
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
            goBackHome(context);
        }
    }

    private String invalidCommand(String line) {
        return "invalid command " + line;
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
        return "patient=" +
                stringify(a.getPatient()) +
                " doctor=" +
                stringify(a.getDoctor()) +
                " date=" +
                a.getDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) +
                " specialty=" +
                a.getSpecialty().getName();
    }

    private String stringify(UserBean u) {
        return "email=" +
                u.getEmail() +
                " name=" +
                u.getFirstName() +
                " first-name=" +
                u.getFirstName();
    }

    private String stringify(DoctorBean d) {
        return "email=" +
                d.getEmail() +
                " name=" +
                d.getFirstName() +
                " first-name=" +
                d.getFirstName() +
                " field=" +
                d.getField();
    }
}
