module com.tomtrotter.habitatsimulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.tomtrotter.habitatsimulation to javafx.fxml;
    exports com.tomtrotter.habitatsimulation;
    exports com.tomtrotter.habitatsimulation.view;
    opens com.tomtrotter.habitatsimulation.view to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.controller;
    opens com.tomtrotter.habitatsimulation.controller to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.model.entities;
    opens com.tomtrotter.habitatsimulation.model.entities to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.model.core;
    opens com.tomtrotter.habitatsimulation.model.core to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.model.environment;
    opens com.tomtrotter.habitatsimulation.model.environment to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.model.factory;
    opens com.tomtrotter.habitatsimulation.model.factory to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.model.util;
    opens com.tomtrotter.habitatsimulation.model.util to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.model.state;
    opens com.tomtrotter.habitatsimulation.model.state to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.model.genetics;
    opens com.tomtrotter.habitatsimulation.model.genetics to javafx.fxml;
}