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
import javafx.util.StringConverter;

/**
* The SimulatorView class is a JavaFX application that provides a graphical user interface (GUI)
* to visualize and interact with a habitat simulation. It displays a grid representing a field
* with animals and plants, and allows users to start, reset, and control the simulation. The view
* also shows simulation statistics, including the current generation and population details.
*/

public class SimulatorView extends Application {

    public static final int WINDOW_WIDTH = 960;
    public static final int WINDOW_HEIGHT = 600;
    public static final int SIMULATION_HEIGHT = 501;
    public static final int SIMULATION_WIDTH = 500;
    public static final int GRID_WIDTH = 100;
    public static final int GRID_HEIGHT = 100;
    private static final Color EMPTY_COLOUR = Color.WHITE;

    public static int PADDING = 10;

    private final String GENERATION_PREFIX = "Generation: ";

    private Label generationLabel;
    private VBox populationData, diseaseData, immuneData;
    private Slider speedSlider;

    private Spinner<Integer> simulationStepsSpinner, diseaseStepsSpinner;
    private Spinner<Double> moralityRateSpinner;
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

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(PADDING));

        root.setTop(createTopPanel());
        root.setLeft(createLeftPanel());
        root.setCenter(createCenterPanel());
        root.setRight(createRightPanel());

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
        Label speedLabel = new Label("Simulation Speed");

        speedSlider = new Slider(100, 1000, 500);
        HBox simulationSpeedHBox = new HBox(new Label("Slow"), speedSlider, new Label("Fast"));

        Label initPopulationLabel = new Label("Number of generations");
        simulationStepsSpinner = new Spinner<>(1, 1000, 30);

        Label diseaseControls = new Label("Disease Controls");
        Label diseaseDurationLabel = new Label("Disease duration (No. of Generations)");

        diseaseStepsSpinner = new Spinner<>(1, 50, 10);

        Label moralityRateLabel = new Label("Mortality Rate");
        moralityRateSpinner = new Spinner<>();
        moralityRateSpinner.setValueFactory(getMoralityRateValueFactory());

        rightPanel.getChildren().addAll(
                controlsLabel, speedLabel, simulationSpeedHBox,
                initPopulationLabel, simulationStepsSpinner, new Separator(),
                diseaseControls, diseaseDurationLabel, diseaseStepsSpinner,
                moralityRateLabel, moralityRateSpinner
        );
        return rightPanel;
    }

    /**
    * Provides a custom StringConverter for the morality rate spinner.
    *
    * @return SpinnerValueFactory with percentage conversion for morality rate.
    */
    private SpinnerValueFactory<Double> getMoralityRateValueFactory() {
        SpinnerValueFactory<Double> factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, 0.01);
        factory.setConverter(new StringConverter<>() {
            @Override
            public String toString(Double value) {
                return value == null ? "" : String.format("%.0f%%", value * 100);
            }

            @Override
            public Double fromString(String string) {
                try {
                    return Double.parseDouble(string.replace("%", "").trim()) / 100.0;
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        });
        return factory;
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

        populationData = new VBox();
        diseaseData = new VBox();
        immuneData = new VBox();

        for (String species : stats.getSpecies()) {

            String icon = stats.getSpeciesIcon(simulator.getField(), species);
            Color colour = stats.getSpeciesColour(simulator.getField(), species);

            populationData.getChildren().add(new TextFlow(createIcon(icon, colour), new Text(stats.getSpeciesDetails(simulator.getField(), species))));
            diseaseData.getChildren().add(new TextFlow(createIcon(icon, colour), new Text(stats.getSpeciesDisease(simulator.getField(), species))));
            immuneData.getChildren().add(new TextFlow(createIcon(icon, colour), new Text(stats.getSpeciesImmune(simulator.getField(), species))));
        }

        panel.getChildren().addAll(
                statsLabel, generationLabel, new Separator(),
                new Label("Population"), populationData, new Separator(),
                new Label("Disease"), diseaseData, new Separator(),
                new Label("Immunity"), immuneData
        );
        return panel;
    }

    /**
    * Creates an icon text element with a specific color.
    *
    * @param iconText: The text of the icon.
    * @param colour: The colour to apply.
    * @return A Text object representing the icon.
    */
    private Text createIcon(String iconText, Color colour) {
        Text icon = new Text(iconText);
        icon.setFill(colour);
        return icon;
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
        startButton.setOnAction(_ -> simulate(simulationStepsSpinner.getValue()));
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(_ -> reset());
        panel.getChildren().addAll(startButton, resetButton);
        return panel;
    }

    /**
    * Shows an alert dialog when the simulation ends.
    */
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
            populationData.getChildren().clear();
            diseaseData.getChildren().clear();
            immuneData.getChildren().clear();
            for (String species : stats.getSpecies()) {
                String icon = stats.getSpeciesIcon(simulator.getField(), species);
                Color colour = stats.getSpeciesColour(simulator.getField(), species);
                populationData.getChildren().add(new TextFlow(createIcon(icon, colour), new Text(stats.getSpeciesDetails(simulator.getField(), species))));
                diseaseData.getChildren().add(new TextFlow(createIcon(icon, colour), new Text(stats.getSpeciesDisease(simulator.getField(), species))));
                immuneData.getChildren().add(new TextFlow(createIcon(icon, colour), new Text(stats.getSpeciesImmune(simulator.getField(), species))));
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
        final int duration = diseaseStepsSpinner.getValue();
        final double moralityRate = moralityRateSpinner.getValue();

        new Thread(() -> {
            for (int gen = 0; gen < numStep; gen++) {
                if(!isViable(simulator.getField())) {
                    Platform.runLater(this::simulationEndAlert);
                    break;
                }
                simulator.simulateOneStep(duration, moralityRate);
                Platform.runLater(() -> updateCanvas(simulator.getStep(), simulator.getField()));
                simulator.delay((int) Math.round(speedSlider.getMax() - speedSlider.getValue()));
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
