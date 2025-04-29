package com.tomtrotter.habitatsimulation.ui.screens;

import com.tomtrotter.habitatsimulation.simulation.simulation.Simulator;
import com.tomtrotter.habitatsimulation.core.domain.Animal;
import com.tomtrotter.habitatsimulation.core.domain.Organism;
import com.tomtrotter.habitatsimulation.core.domain.Plant;
import com.tomtrotter.habitatsimulation.simulation.environment.FieldStats;
import com.tomtrotter.habitatsimulation.ui.state.ViewState;
import com.tomtrotter.habitatsimulation.ui.canvas.FieldCanvas;
import com.tomtrotter.habitatsimulation.ui.base.BaseView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
* SimulatorView manages the graphical user interface (GUI) for the habitat simulation.
* It includes components for displaying the simulation field, statistics, and user controls.
*/

public class SimulatorView extends BaseView {

    private Label generationLabel;
    private VBox populationData, diseaseData;
    private Slider speedSlider;
    private Spinner<Integer> simulationStepsSpinner;
    private FieldCanvas fieldCanvas;

    private final int SIMULATION_WIDTH = 500;
    private final int SIMULATION_HEIGHT = 501;
    private final int GRID_WIDTH = 100;
    private final int GRID_HEIGHT = 100;

    private volatile boolean isSimulating = false;
    private volatile boolean cancelSimulation = false;
    private volatile boolean paused = false;

    private Simulator simulator;
    private FieldStats stats;
    private final ViewState viewState = ViewState.getInstance();

    private int generation = 0;

    /**
    * Creates the main simulation scene with GUI layout and initializes simulation components.
    * @return The full Scene object for rendering in the JavaFX window.
    */
    @Override
    public Scene createScene() {
        simulator = new Simulator(GRID_HEIGHT, GRID_WIDTH);
        stats = new FieldStats();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        root.setTop(createTopPanel());
        root.setLeft(createLeftPanel());
        root.setCenter(createCenterPanel());
        root.setRight(createRightPanel());

        return new Scene(root, viewState.getWindowWidth(), viewState.getWindowHeight());
    }

    /**
    * Builds the right-side panel of the UI for simulation controls (speed, steps, disease settings).
    * @return VBox with sliders and spinners for user interaction.
    */
    private VBox createRightPanel() {
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-padding: 10;");

        Label controlsLabel = new Label("Simulation Controls");
        Label speedLabel = new Label("Simulation Speed");

        speedSlider = new Slider(100, 1000, 500);
        HBox simulationSpeedHBox = new HBox(new Label("Slow"), speedSlider, new Label("Fast"));

        Label initPopulationLabel = new Label("Number of generations");
        simulationStepsSpinner = new Spinner<>(1, 1000, 30);

        rightPanel.getChildren().addAll(
                controlsLabel, speedLabel, simulationSpeedHBox, initPopulationLabel, simulationStepsSpinner
        );
        return rightPanel;
    }


    /**
    * Builds the left-side panel displaying population and disease statistics for all species.
    * @return VBox with dynamically updating stats views.
    */
    private VBox createLeftPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-padding: 10;");

        Label statsLabel = new Label("Simulation Stats");
        generationLabel = new Label(viewState.getGenPrefix());

        populationData = new VBox();
        diseaseData = new VBox();

        for (String species : stats.getSpecies()) {
            String icon = stats.getSpeciesIcon(simulator.getField(), species);
            Color colour = stats.getSpeciesColour(simulator.getField(), species);

            populationData.getChildren().add(new TextFlow(createIcon(icon, colour), new Text(stats.getSpeciesDetails(simulator.getField(), species))));
            diseaseData.getChildren().add(new TextFlow(createIcon(icon, colour), new Text(stats.getSpeciesDisease(simulator.getField(), species))));
        }

        panel.getChildren().addAll(
                statsLabel, generationLabel, new Separator(),
                new Label("Population"), populationData, new Separator(),
                new Label("Disease"), diseaseData
        );
        return panel;
    }

    /**
    * Creates a colored text icon for species representation in the stats panel.
    * @param iconText Text symbol for the species.
    * @param colour   Display color for the species.
    * @return A styled Text object.
    */
    private Text createIcon(String iconText, Color colour) {
        Text icon = new Text(iconText);
        icon.setFill(colour);
        return icon;
    }

    /**
    * Builds the center panel where the simulation field grid is rendered.
    * @return A StackPane containing the simulation canvas.
    */
    private StackPane createCenterPanel() {
        StackPane simulationView = new StackPane();
        simulationView.setPrefSize(SIMULATION_WIDTH, SIMULATION_HEIGHT);

        fieldCanvas = new FieldCanvas(
                SIMULATION_WIDTH - 10, SIMULATION_HEIGHT - 10,
                GRID_WIDTH, GRID_HEIGHT
        );
        fieldCanvas.setWidth(SIMULATION_WIDTH);
        fieldCanvas.setHeight(SIMULATION_HEIGHT);

        simulationView.getChildren().add(fieldCanvas);
        simulationView.setMaxSize(SIMULATION_WIDTH, SIMULATION_HEIGHT);
        simulationView.setMinSize(SIMULATION_WIDTH, SIMULATION_HEIGHT);
        return simulationView;
    }

    /**
    * Builds the top panel with buttons for controlling simulation flow (Start, Pause, Reset, Settings).
    * @return An HBox with control buttons.
    */
    private HBox createTopPanel() {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));

        Button startButton = new Button("Start");
        startButton.setOnAction(_ -> {
            simulate(simulationStepsSpinner.getValue());
        });

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(_ -> paused = true);

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(_ -> reset());

        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(_ -> {
            viewState.switchTo(SettingsView.class);
        });

        panel.getChildren().addAll(startButton, pauseButton, resetButton, settingsButton);
        return panel;
    }

    /**
    * Displays a notification alert when the simulation completes all steps.
    */
    private void simulationEndAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulation Complete");
        alert.setHeaderText("The simulation has finished running.");
        alert.setContentText("Click OK to proceed.");
        alert.showAndWait();
    }

    /**
    * Updates population and disease data shown in the left panel based on current simulation state.
    */
    private void updatePopulation() {
        Platform.runLater(() -> {
            populationData.getChildren().clear();
            diseaseData.getChildren().clear();
            for (String species : stats.getSpecies()) {
                String icon = stats.getSpeciesIcon(simulator.getField(), species);
                Color colour = stats.getSpeciesColour(simulator.getField(), species);
                populationData.getChildren().add(new TextFlow(createIcon(icon, colour), new Text(stats.getSpeciesDetails(simulator.getField(), species))));
                diseaseData.getChildren().add(new TextFlow(createIcon(icon, colour), new Text(stats.getSpeciesDisease(simulator.getField(), species))));
            }
        });
    }

    /**
    * Redraws the simulation field with updated organism data and refreshes statistics.
    */
    public void updateCanvas() {
        generationLabel.setText(viewState.getGenPrefix() + generation);
        updatePopulation();
        stats.reset();

        for (int row = 0; row < simulator.getField().getHeight(); row++) {
            for (int col = 0; col < simulator.getField().getWidth(); col++) {
                Organism organism = simulator.getField().getOrganismAt(row, col);
                if (organism instanceof Animal animal && animal.isAlive()) {
                    stats.incrementCountAnimal(animal);
                    fieldCanvas.drawMark(col, row, animal.getColour());
                }
                else if (organism instanceof Plant plant && plant.isAlive()){
                    stats.incrementCountPlant(plant);
                    fieldCanvas.drawMark(col, row, plant.getColour());
                }
                else {
                    fieldCanvas.drawMark(col, row, Color.WHITE);
                }
            }
        }
        stats.countFinished();
    }

    /**
    * Resets the simulation state and reinitialized the canvas and statistics.
    */
    private void reset() {
        cancelSimulation = true;
        simulator.reset();
        simulator = new Simulator(GRID_HEIGHT, GRID_WIDTH);
        stats.reset();
        generation = 0;
        generationLabel.setText(viewState.getGenPrefix() + generation);
        updatePopulation();
        updateCanvas();
    }

    /**
    * Runs the simulation in a background thread for a set number of generations.
    * Allows pausing, canceling, and adjusting simulation speed dynamically.
    * @param numSteps The number of steps (generations) to simulate.
    */
    public void simulate(int numSteps) {
        cancelSimulation = false;
        paused = false;
        if (isSimulating){
            return;
        }

        isSimulating = true;
        new Thread(() -> {
            for (int step = 0; step < numSteps && !cancelSimulation; step++) {
                if (paused) {
                    step--;
                    continue;
                }
                generation++;
                simulator.simulateOneStep();
                Platform.runLater(() -> {
                    updateCanvas();
                });
                try {
                    Thread.sleep(speedSlider.maxProperty().longValue() - (long) speedSlider.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isSimulating = false;
            if (!cancelSimulation){
                Platform.runLater(this::simulationEndAlert);
            }
        }).start();
    }

}
