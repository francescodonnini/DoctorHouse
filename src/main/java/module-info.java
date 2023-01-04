module ispw.uniroma2.doctorhouse {
    requires javafx.controls;
    requires javafx.fxml;


    opens ispw.uniroma2.doctorhouse to javafx.fxml;
    exports ispw.uniroma2.doctorhouse;
}