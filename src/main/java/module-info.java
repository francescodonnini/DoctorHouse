module ispw.uniroma2.doctorhouse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.opencsv;

    exports ispw.uniroma2.doctorhouse;
    exports ispw.uniroma2.doctorhouse.dao;
    exports ispw.uniroma2.doctorhouse.navigation;
    exports ispw.uniroma2.doctorhouse.navigation.login;
    exports ispw.uniroma2.doctorhouse.auth.exceptions;
    exports ispw.uniroma2.doctorhouse.model;
    exports ispw.uniroma2.doctorhouse.rearrange;
    exports ispw.uniroma2.doctorhouse.view;
    opens ispw.uniroma2.doctorhouse.view to javafx.fxml;
    exports ispw.uniroma2.doctorhouse.beans;
    exports ispw.uniroma2.doctorhouse.model.appointment;
    exports ispw.uniroma2.doctorhouse.dao.exceptions;
    exports ispw.uniroma2.doctorhouse.dao.appointment;
    exports ispw.uniroma2.doctorhouse.dao.slot;
    exports ispw.uniroma2.doctorhouse.dao.office;
    exports ispw.uniroma2.doctorhouse.dao.users;
    exports ispw.uniroma2.doctorhouse.dao.specialty;
    exports ispw.uniroma2.doctorhouse.dao.shift;
    exports ispw.uniroma2.doctorhouse.auth;
    exports ispw.uniroma2.doctorhouse.secondinterface;
    opens ispw.uniroma2.doctorhouse.secondinterface;
}