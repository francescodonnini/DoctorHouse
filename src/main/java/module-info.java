module ispw.uniroma2.doctorhouse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ispw.uniroma2.doctorhouse to javafx.fxml;
    opens ispw.uniroma2.doctorhouse.auth.login to javafx.fxml;
    opens ispw.uniroma2.doctorhouse.auth.registration to javafx.fxml;
    exports ispw.uniroma2.doctorhouse;
    exports ispw.uniroma2.doctorhouse.patienthomepage;
    opens ispw.uniroma2.doctorhouse.patienthomepage to javafx.fxml;
    exports ispw.uniroma2.doctorhouse.requestprescription;
    opens ispw.uniroma2.doctorhouse.requestprescription to javafx.fxml;
    exports ispw.uniroma2.doctorhouse.notification;
    opens ispw.uniroma2.doctorhouse.notification to javafx.fxml;
}