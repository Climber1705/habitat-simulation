package com.tomtrotter.habitatsimulation.view;

import com.tomtrotter.habitatsimulation.model.state.ViewState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * View class responsible for displaying the settings interface for the simulation.
 * It provides various sections for configuring the environment, species, and simulation controls.
 */
public class SettingsView extends BaseView {

    private final ViewState viewState = ViewState.getInstance();

    /**
    * Creates the scene for the settings view, consisting of various configuration sections
    * and buttons for interacting with the settings.
    *
    * @return The scene for the settings view.
    */
    @Override
    public Scene createScene() {
        VBox leftColumn = new VBox(15, createEnvironmentBox(), createAdvancedSettingsBox());
        VBox rightColumn = new VBox(15, createSpeciesBox(), createSimulationControlBox());
        HBox mainGrid = new HBox(20, leftColumn, rightColumn);
        mainGrid.setPadding(new Insets(20));

        HBox bottomButtons = createBottomButtons();

        VBox root = new VBox(20, mainGrid, bottomButtons);
        root.setPadding(new Insets(20));

        return new Scene(root, viewState.getWindowWidth(), viewState.getWindowHeight());
    }

    /**
    * Creates the environment configuration section, including fields for temperature range,
    * humidity, and terrain type.
    *
    * @return A VBox containing the environment configuration settings.
    */
    private VBox createEnvironmentBox() {
        VBox box = createSectionBox("🌲 Environment Parameters");

        HBox tempRange = new HBox(5, new Label("Temperature Range (°C):"),
                new TextField("20"), new TextField("30"));
        HBox humidity = new HBox(5, new Label("Humidity (%):"), new Slider(0, 100, 50));
        ComboBox<String> terrainCombo = new ComboBox<>();
        terrainCombo.getItems().addAll("Forest", "Desert", "Mountain", "Grassland");
        terrainCombo.setValue("Forest");
        HBox terrain = new HBox(5, new Label("Terrain Type:"), terrainCombo);

        box.getChildren().addAll(tempRange, humidity, terrain);
        return box;
    }

    /**
    * Creates the species configuration section, including fields for predator and prey populations,
    * reproduction rate, and species traits.
    *
    * @return A VBox containing the species configuration settings.
    */
    private VBox createSpeciesBox() {
        VBox box = createSectionBox("🦊 Species Configuration");

        HBox initialPop = new HBox(5, new Label("Predators:"), new TextField("10"),
                new Label("Prey:"), new TextField("50"));
        HBox reproductionRate = new HBox(5, new Label("Reproduction Rate:"), new Slider(0, 1, 0.5));

        VBox traits = new VBox(5,
                new Label("Species Traits:"),
                new CheckBox("Pack Behavior"),
                new CheckBox("Hibernation"),
                new CheckBox("Nocturnal"),
                new CheckBox("Migration")
        );

        box.getChildren().addAll(initialPop, reproductionRate, traits);
        return box;
    }

    /**
    * Creates the simulation control section, including fields for simulation speed, timescale,
    * and data collection options.
    *
    * @return A VBox containing the simulation control settings.
    */
    private VBox createSimulationControlBox() {
        VBox box = createSectionBox("🎛 Simulation Controls");

        ComboBox<String> speedCombo = new ComboBox<>();
        speedCombo.getItems().addAll("0.5x", "1x", "2x");
        speedCombo.setValue("0.5x");

        HBox simSpeed = new HBox(5, new Label("Simulation Speed:"), speedCombo);
        HBox timeScale = new HBox(5, new Label("Days per Second:"), new TextField("1"),
                new Label("Total Days:"), new TextField("365"));

        VBox dataCollection = new VBox(5,
                new Label("Data Collection:"),
                new CheckBox("Population Statistics") {{ setSelected(true); }},
                new CheckBox("Environmental Data") {{ setSelected(true); }},
                new CheckBox("Behavior Patterns")
        );

        box.getChildren().addAll(simSpeed, timeScale, dataCollection);
        return box;
    }

    /**
    * Creates the advanced settings section, including fields for random events, mutation rate,
    * resource distribution, and weather patterns.
    *
    * @return A VBox containing the advanced settings options.
    */
    private VBox createAdvancedSettingsBox() {
        VBox box = createSectionBox("🔧 Advanced Settings");

        ComboBox<String> randomEventCombo = new ComboBox<>();
        randomEventCombo.getItems().addAll("Disabled", "Enabled");
        randomEventCombo.setValue("Disabled");

        ComboBox<String> resourceCombo = new ComboBox<>();
        resourceCombo.getItems().addAll("Even", "Clustered", "Random");
        resourceCombo.setValue("Even");

        ComboBox<String> weatherCombo = new ComboBox<>();
        weatherCombo.getItems().addAll("Static", "Dynamic");
        weatherCombo.setValue("Static");

        HBox randomEvents = new HBox(5, new Label("Random Events:"), randomEventCombo);
        HBox mutationRate = new HBox(5, new Label("Mutation Rate:"), new Slider(0, 1, 0.1));
        HBox resourceDist = new HBox(5, new Label("Resource Distribution:"), resourceCombo);
        HBox weather = new HBox(5, new Label("Weather Patterns:"), weatherCombo);

        box.getChildren().addAll(randomEvents, mutationRate, resourceDist, weather);
        return box;
    }

    /**
    * Creates the bottom button section, including buttons for navigating back, resetting settings,
    * canceling changes, and saving settings.
    *
    * @return An HBox containing the bottom action buttons.
    */
    private HBox createBottomButtons() {
        Button backBtn = new Button("⬅ Back");
        Button resetBtn = new Button("↺ Reset to Defaults");
        Button cancelBtn = new Button("Cancel");
        Button saveBtn = new Button("Save Settings");

        saveBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");

        backBtn.setOnAction(_ -> viewState.switchTo(SimulatorView.class));

        HBox buttonBox = new HBox(10, backBtn, resetBtn, cancelBtn, saveBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        return buttonBox;
    }

    /**
    * Creates a generic section box with a given title.
    * The section box includes a border and padding for layout.
    *
    * @param title The title for the section box.
    * @return A VBox containing the section title and layout.
    */
    private VBox createSectionBox(String title) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #ccc; -fx-border-width: 1;");
        box.getChildren().add(new Label(title));
        return box;
    }

}
