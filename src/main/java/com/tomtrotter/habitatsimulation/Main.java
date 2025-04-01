package com.tomtrotter.habitatsimulation;

import com.tomtrotter.habitatsimulation.view.SimulatorView;

/**
* The Main class serves as the entry point for the Habitat Simulation application.
* It initializes and starts the simulation by creating an instance of SimulatorView
* and invoking its run method.
*/
public class Main {

    /**
    * The main method is the entry point of the application.
    * It creates an instance of SimulatorView and starts the simulation.
    *
    * @param args: Command-line arguments passed to the application.
    */
    public static void main(String[] args) {
        SimulatorView app = new SimulatorView();
        app.run(args);
    }

}
