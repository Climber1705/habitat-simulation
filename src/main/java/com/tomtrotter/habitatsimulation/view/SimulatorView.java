package com.tomtrotter.habitatsimulation.view;

import com.tomtrotter.habitatsimulation.controller.Simulator;
import com.tomtrotter.habitatsimulation.model.Animal;
import com.tomtrotter.habitatsimulation.model.Field;
import com.tomtrotter.habitatsimulation.model.FieldStats;
import com.tomtrotter.habitatsimulation.model.Plant;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
* The SimulatorView class is a JavaFX application that provides a graphical user interface (GUI)
* to visualize and interact with a habitat simulation. It displays a grid representing a field
* with animals and plants, and allows users to start, reset, and control the simulation. The view
* also shows simulation statistics, including the current generation and population details.
*/

public class SimulatorView extends Application {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 500;
    public static final int SIMULATION_HEIGHT = 501;
    public static final int SIMULATION_WIDTH = 435;
    public static final int GRID_WIDTH = 100;
    public static final int GRID_HEIGHT = 100;
    private static final Color EMPTY_COLOUR = Color.WHITE;

    public static int PADDING = 10;

    private final String GENERATION_PREFIX = "Generation: ";

    private Label generationLabel;
    private VBox populationData, diseaseData;

    private Spinner<Integer> simulationStepsSpinner;
    private FieldCanvas fieldCanvas;
    private FieldStats stats;
    private Simulator simulator;

    /**
    * Initializes the JavaFX application and sets up the main window layout and components.
    * Creates the simulation view, control panels, and buttons, and then displays the window.
    *
    * @param stage: The GUI window.
    */
    @Override
    public void start(Stage stage) {
        simulator = new Simulator(GRID_HEIGHT, GRID_WIDTH);
        stats = new FieldStats();

        // Root layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(PADDING));

        // Left panel (Simulation Stats & Population)
        VBox leftPanel = createLeftPanel();

        // Center panel (Simulation View)
        StackPane centerPanel = createCenterPanel();

        // Top Panel (Buttons)
        HBox topPanel = createTopPanel();

        // Right Panel (Simulation Controls & Environment Factors)
        VBox rightPanel = createRightPanel();

        // Add elements to root layout
        root.setTop(topPanel);
        root.setLeft(leftPanel);
        root.setCenter(centerPanel);
        root.setRight(rightPanel);

        // Create scene and show stage
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Habitat Simulator");
        stage.setScene(scene);

        stage.setResizable(false);
        updateCanvas(simulator.getStep(), simulator.getField());
        stage.show();
    }

    /**
    * Creates the right panel that contains simulation controls like number of generations to simulate.
    *
    * @return A VBox containing simulation control elements such as labels and spinners.
    */
    private VBox createRightPanel() {
        VBox rightPanel = new VBox(PADDING);
        rightPanel.setPadding(new Insets(PADDING));
        rightPanel.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-padding: 10;");

        Label controlsLabel = new Label("Simulation Controls");
        Label initPopulationLabel = new Label("Number of generations");

        // Spinner for selecting simulation steps (min: 1, max: 1000, initial: 30)
        simulationStepsSpinner = new Spinner<>();
        simulationStepsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 30));

        Label diseaseLabel = new Label("Disease Details");
        diseaseData = new VBox();
        for (String species : stats.getSpecies()) {
            if(species.equals("Plant")) {
                continue;
            }
            String speciesIcon = stats.getSpeciesIcon(simulator.getField(), species);
            String speciesInfo = stats.getSpeciesDisease(simulator.getField(), species);
            Color speciesColour = stats.getSpeciesColour(simulator.getField(), species);

            Text icon = new Text(speciesIcon);
            icon.setFill(speciesColour);
            Text info = new Text(speciesInfo);

            TextFlow infoText = new TextFlow(icon, info);
            diseaseData.getChildren().add(infoText);
        }
        rightPanel.getChildren().addAll(controlsLabel, initPopulationLabel, simulationStepsSpinner, new Separator(), diseaseLabel, diseaseData);
        return rightPanel;
    }

    /**
    * Creates the left panel that displays simulation stats such as the current generation and population details.
    *
    * @return A VBox containing labels for statistics and population data.
    */
    private VBox createLeftPanel() {
        VBox panel = new VBox(PADDING);
        panel.setPadding(new Insets(PADDING));
        panel.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-padding: 10;");

        Label statsLabel = new Label("Simulation Stats");
        generationLabel = new Label(GENERATION_PREFIX);

        Label populationLabel = new Label("Population Details");
        populationData = new VBox();
        for (String species : stats.getSpecies()) {
            String speciesIcon = stats.getSpeciesIcon(simulator.getField(), species);
            String speciesInfo = stats.getSpeciesDetails(simulator.getField(), species);
            Color speciesColour = stats.getSpeciesColour(simulator.getField(), species);

            Text icon = new Text(speciesIcon);
            icon.setFill(speciesColour);
            Text info = new Text(speciesInfo);

            TextFlow infoText = new TextFlow(icon, info);
            populationData.getChildren().add(infoText);
        }
        panel.getChildren().addAll(statsLabel, generationLabel, new Separator(), populationLabel, populationData);
        return panel;
    }

    /**
    * Creates the center panel which is used to display the grid representing the simulation field.
    *
    * @return A StackPane containing the fieldCanvas, where the simulation grid is drawn.
    */
    private StackPane createCenterPanel() {
        StackPane simulationView = new StackPane();
        simulationView.setPrefSize(SIMULATION_WIDTH, SIMULATION_HEIGHT);

        fieldCanvas = new FieldCanvas(SIMULATION_WIDTH - PADDING, SIMULATION_HEIGHT - PADDING, GRID_WIDTH, GRID_HEIGHT);
        fieldCanvas.setWidth(SIMULATION_WIDTH);
        fieldCanvas.setHeight(SIMULATION_HEIGHT);

        simulationView.getChildren().add(fieldCanvas);

        simulationView.setMaxSize(SIMULATION_WIDTH, SIMULATION_HEIGHT);
        simulationView.setMinSize(SIMULATION_WIDTH, SIMULATION_HEIGHT);
        return simulationView;
    }

    /**
    * Creates the top panel which contains buttons to start and reset the simulation.
    *
    * @return An HBox containing buttons for starting and resetting the simulation.
    */
    private HBox createTopPanel() {
        HBox panel = new HBox(PADDING);
        panel.setPadding(new Insets(PADDING));
        Button startButton = new Button("Start");
        startButton.setOnAction(_ -> simulate(simulationStepsSpinner.getValue())); // Start simulation with chosen steps
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(_ -> reset());
        panel.getChildren().addAll(startButton, resetButton);
        return panel;
    }

    private void simulationEndAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulation Complete");
        alert.setHeaderText("The simulation has finished running.");
        alert.setContentText("Click OK to proceed.");
        alert.showAndWait();
    }

    /**
    * Updates the population data labels in the left panel to reflect the current population in the simulation and
    * the labels in the right panel which reflect the disease population in the simulation.
    * This method is called on the JavaFX Application Thread to update the UI components safely.
    */
    private void updatePopulation() {
        Platform.runLater(() -> {
            populationData.getChildren().clear(); // Clear old labels
            for (String species : stats.getSpecies()) {
                String speciesIcon = stats.getSpeciesIcon(simulator.getField(), species);
                String speciesInfo = stats.getSpeciesDetails(simulator.getField(), species);
                Color speciesColour = stats.getSpeciesColour(simulator.getField(), species);

                Text icon = new Text(speciesIcon);
                icon.setFill(speciesColour);
                Text info = new Text(speciesInfo);

                TextFlow infoText = new TextFlow(icon, info);
                populationData.getChildren().add(infoText);
            }
            diseaseData.getChildren().clear(); // Clear old labels
            for (String species : stats.getSpecies()) {
                String speciesIcon = stats.getSpeciesIcon(simulator.getField(), species);
                String speciesInfo = stats.getSpeciesDisease(simulator.getField(), species);
                Color speciesColour = stats.getSpeciesColour(simulator.getField(), species);

                Text icon = new Text(speciesIcon);
                icon.setFill(speciesColour);
                Text info = new Text(speciesInfo);

                TextFlow infoText = new TextFlow(icon, info);
                diseaseData.getChildren().add(infoText);
            }
        });
    }

    /**
    * Updates the canvas to display the current simulation state, including the current generation,
    * population details, and the grid of animals and plants.
    *
    * @param generation: The current generation of the simulation.
    * @param field: The current state of the simulation field containing animals and plants.
    */
    public void updateCanvas(int generation, Field field) {
        generationLabel.setText(GENERATION_PREFIX + generation);
        updatePopulation();
        stats.reset();
        for (int row = 0; row < field.getHeight(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Animal animal = field.getAnimalAt(row, col);
                Plant plant = field.getPlantAt(row,col);
                if (animal != null && animal.isAlive()) {
                    stats.incrementCountAnimal(animal);
                    fieldCanvas.drawMark(col, row, animal.getColour());
                }
                else if (plant != null){
                    stats.incrementCountPlant(plant);
                    fieldCanvas.drawMark(col, row, plant.getColour());
                }
                else {
                    fieldCanvas.drawMark(col, row, EMPTY_COLOUR);
                }
            }
        }
        stats.countFinished();
    }

    /**
    * Checks whether the simulation is still viable by verifying if at least one species is alive.
    *
    * @param field: The current state of the simulation field.
    * @return true if the simulation is viable (more than one species is alive), false otherwise.
    */
    private boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
    * Simulates a specified number of generations. If the simulation becomes non-viable
    * before reaching the desired number of generations, it stops early.
    *
    * @param numStep: The number of generations to simulate.
    */
    public void simulate(int numStep) {
        new Thread(() -> {
            for (int gen = 0; gen < numStep; gen++) {
                if(!isViable(simulator.getField())) {
                    Platform.runLater(this::simulationEndAlert);
                    break;
                }
                simulator.simulateOneStep();
                Platform.runLater(() -> updateCanvas(simulator.getStep(), simulator.getField()));
                simulator.delay(500);
            }
        }).start();
    }

    /**
    * Resets the simulation to its initial state, clearing the grid and resetting the population and statistics.
    */
    public void reset() {
        simulator.reset();
        updateCanvas(simulator.getStep(), simulator.getField());
    }

    /**
    * This method serves as the entry point for launching a JavaFX application.
    * It takes an array of command-line arguments and passes them to the
    * JavaFX launch method, which initializes and starts the application.
    *
    * @param args: An array of command-line arguments passed to the application.
    */
    public void run(String[] args) {
        launch(args);
    }

}
