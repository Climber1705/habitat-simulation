module com.tomtrotter.habitatsimulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    exports com.tomtrotter.habitatsimulation.simulation.entities;
    opens com.tomtrotter.habitatsimulation.simulation.entities to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.simulation.environment;
    opens com.tomtrotter.habitatsimulation.simulation.environment to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.simulation.factory;
    opens com.tomtrotter.habitatsimulation.simulation.factory to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.util;
    opens com.tomtrotter.habitatsimulation.util to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.simulation.state;
    opens com.tomtrotter.habitatsimulation.simulation.state to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.ui.base;
    opens com.tomtrotter.habitatsimulation.ui.base to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.ui.screens;
    opens com.tomtrotter.habitatsimulation.ui.screens to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.ui.canvas;
    opens com.tomtrotter.habitatsimulation.ui.canvas to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.application;
    opens com.tomtrotter.habitatsimulation.application to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.simulation.simulation;
    opens com.tomtrotter.habitatsimulation.simulation.simulation to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.core.domain;
    opens com.tomtrotter.habitatsimulation.core.domain to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.ui.state;
    opens com.tomtrotter.habitatsimulation.ui.state to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.simulation.genetics.attributes;
    opens com.tomtrotter.habitatsimulation.simulation.genetics.attributes to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.simulation.genetics.mutation;
    opens com.tomtrotter.habitatsimulation.simulation.genetics.mutation to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.simulation.genetics.builder;
    opens com.tomtrotter.habitatsimulation.simulation.genetics.builder to javafx.fxml;
    exports com.tomtrotter.habitatsimulation.simulation.genetics.core;
    opens com.tomtrotter.habitatsimulation.simulation.genetics.core to javafx.fxml;
}