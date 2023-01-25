module ispw.uniroma2.doctorhouse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports ispw.uniroma2.doctorhouse;
    exports ispw.uniroma2.doctorhouse.dao;
    exports ispw.uniroma2.doctorhouse.navigation;
    exports ispw.uniroma2.doctorhouse.navigation.login;
    exports ispw.uniroma2.doctorhouse.navigation.patient;
    exports ispw.uniroma2.doctorhouse.auth;
    exports ispw.uniroma2.doctorhouse.auth.beans;
    exports ispw.uniroma2.doctorhouse.auth.exceptions;
    exports ispw.uniroma2.doctorhouse.model;
    exports ispw.uniroma2.doctorhouse.view;
    opens ispw.uniroma2.doctorhouse.view to javafx.fxml;
    exports ispw.uniroma2.doctorhouse.beans;
}