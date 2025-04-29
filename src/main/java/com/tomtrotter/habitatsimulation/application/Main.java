package com.tomtrotter.habitatsimulation.application;

import com.tomtrotter.habitatsimulation.ui.state.ViewState;
import com.tomtrotter.habitatsimulation.ui.screens.SettingsView;
import com.tomtrotter.habitatsimulation.ui.screens.SimulatorView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
* The Main class is a JavaFX application that provides a graphical user interface (GUI)
* to visualize and interact with a habitat simulation. It displays a grid representing a field
* with animals and plants, and allows users to start, reset, and control the simulation. The view
* also shows simulation statistics, including the current generation and population details.
*/

public class Main extends Application {

    /**
    * Initializes the JavaFX application and sets up the main window layout and components.
    * Creates the simulation view, control panels, and buttons, and then displays the window.
    *
    * @param stage The GUI window.
    */
    @Override
    public void start(Stage stage) {

        ViewState viewState = ViewState.getInstance();
        viewState.setStage(stage);

        viewState.registerView(SimulatorView.class, new SimulatorView());
        viewState.registerView(SettingsView.class, new SettingsView());

        viewState.switchTo(SimulatorView.class);

        stage.setTitle("Habitat Simulator");
        stage.setResizable(false);

        viewState.getView(SimulatorView.class).updateCanvas();

        stage.show();
    }

    /**
    * This method serves as the entry point for launching a JavaFX application.
    * It takes an array of command-line arguments and passes them to the
    * JavaFX launch method, which initializes and starts the application.
    *
    * @param args An array of command-line arguments passed to the application.
    */
    public static void main(String[] args) {
        launch(args);
    }

}
