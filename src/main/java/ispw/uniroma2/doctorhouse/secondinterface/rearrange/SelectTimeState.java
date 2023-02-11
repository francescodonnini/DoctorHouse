package ispw.uniroma2.doctorhouse.secondinterface.rearrange;

import ispw.uniroma2.doctorhouse.beans.AppointmentBean;
import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.InvalidTimeSlot;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.DateTimeInterval;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrange;
import ispw.uniroma2.doctorhouse.secondinterface.CommandLine;
import ispw.uniroma2.doctorhouse.secondinterface.State;
import ispw.uniroma2.doctorhouse.secondinterface.StateFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class SelectTimeState implements State {
    private final StateFactory factory;
    private final AskForRearrange controller;
    private final LocalDate from;
    private final LocalDate to;
    private final UserBean loggedUser;
    private final AppointmentBean selected;
    private final List<DateTimeInterval> intervals;

    public SelectTimeState(StateFactory factory, AskForRearrange controller, LocalDate from, LocalDate to, UserBean loggedUser, AppointmentBean selected) {
        this.factory = factory;
        this.controller = controller;
        this.from = from;
        this.to = to;
        this.loggedUser = loggedUser;
        this.selected = selected;
        intervals = new ArrayList<>();
    }

    @Override
    public void onEnter(CommandLine context) {
        showTimeIntervals(context);
    }

    private void showTimeIntervals(CommandLine context) {
        try {
            intervals.clear();
            intervals.addAll(controller.getFreeSlots(selected, from, to));
            StringBuilder s = new StringBuilder();
            if (intervals.isEmpty()) {
                s.append("there are not free slots");
            } else {
                s.append("please choose one of:\n");
            }
            for (int i = 0; i < intervals.size(); i++) {
                s.append(i).append(") ").append(intervals.get(i)).append("\n");
            }
            context.setResponse(s.toString());
        } catch (PersistentLayerException e) {
            context.setResponse("error occurred while trying to fetch free time slots, try again later\n");
            goBackHome(context);
        }
    }

    @Override
    public void enter(CommandLine context, String line) throws PersistentLayerException {
        line = line.trim();
        switch (line) {
            case "help":
                context.setResponse("%d hh:mm\nback\nrefresh\n");
                break;
            case "back":
                goBackHome(context);
                break;
            case "refresh":
                showTimeIntervals(context);
                break;
            default:
                String[] tokens = line.split("\\s+");
                if (tokens.length == 2) {
                    int index = Integer.parseInt(tokens[0]);
                    if (index < 0 || index >= intervals.size()) {
                        context.setResponse("invalid index " + index);
                        return;
                    }
                    try {
                        LocalTime time = LocalTime.parse(tokens[1], DateTimeFormatter.ofPattern("HH:mm"));
                        trySubmit(context, intervals.get(index), time);
                        goBackHome(context);
                    } catch (DateTimeParseException ignored) {
                        context.setResponse("invalid date " + tokens[1]);
                    }
                } else {
                    context.setResponse("invalid command " + line);
                }
                break;
        }
    }

    private void trySubmit(CommandLine context, DateTimeInterval interval, LocalTime inputTime) {
        try {
            LocalTime start = interval.getInterval().getStart();
            LocalTime end = interval.getInterval().getEnd();
            if (inputTime.isBefore(start) || inputTime.plus(selected.getSpecialty().getDuration()).isAfter(end)) {
                context.setResponse("invalid time " + inputTime);
                return;
            }
            controller.ask(selected, LocalDateTime.of(interval.getDateTime().toLocalDate(), inputTime));
        } catch (InvalidTimeSlot e) {
            showTimeIntervals(context);
        } catch (PersistentLayerException e) {
            goBackHome(context);
        }
    }

    private void goBackHome(CommandLine context) {
        if (loggedUser instanceof DoctorBean) {
            context.setState(factory.createDoctorHomePageState(loggedUser));
        } else {
            context.setState(factory.createUserHomePageState(loggedUser));
        }
    }
}
