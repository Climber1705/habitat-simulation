module com.tomtrotter.habitatsimulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.tomtrotter.habitatsimulation to javafx.fxml;
    exports com.tomtrotter.habitatsimulation;
    exports com.tomtrotter.habitatsimulation.model;
    opens com.tomtrotter.habitatsimulation.model to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.view;
    opens com.tomtrotter.habitatsimulation.view to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.controller;
    opens com.tomtrotter.habitatsimulation.controller to javafx.fxml;
}