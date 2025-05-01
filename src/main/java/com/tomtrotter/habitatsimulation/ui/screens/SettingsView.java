package com.tomtrotter.habitatsimulation.ui.screens;

import com.tomtrotter.habitatsimulation.simulation.entities.*;
import com.tomtrotter.habitatsimulation.simulation.genetics.attributes.Attributes;
import com.tomtrotter.habitatsimulation.simulation.state.SimulatorState;
import com.tomtrotter.habitatsimulation.ui.state.ViewState;
import com.tomtrotter.habitatsimulation.ui.base.BaseView;
import com.tomtrotter.habitatsimulation.ui.components.SectionBuilder;
import com.tomtrotter.habitatsimulation.ui.components.UIFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Responsible for rendering the settings UI in the habitat simulation application.
 * This view allows users to configure disease parameters, species spawn weights, food values,
 * and genetics settings. Also provides navigation and reset functionality.
 */
public class SettingsView extends BaseView {

    private final ViewState viewState = ViewState.getInstance();
    private final SimulatorState simulatorState = SimulatorState.getInstance();

    /**
     * Constructs and returns the complete Scene for the settings view.
     * The scene includes sections for disease parameters, species settings,
     * genetics configuration, and control buttons.
     *
     * @return the fully composed Scene to be displayed
     */
    @Override
    public Scene createScene() {
        VBox rightColumn = new VBox(15, createDiseaseBox(), createGeneticsSettingsBox());
        VBox leftColumn = new VBox(15, createSpeciesBox());

        HBox mainGrid = new HBox(20, leftColumn, rightColumn);
        mainGrid.setPadding(new Insets(20));

        HBox bottomButtons = createBottomButtons();

        VBox root = new VBox(mainGrid, bottomButtons);
        root.setPadding(new Insets(20));

        return new Scene(root, viewState.getWindowWidth(), viewState.getWindowHeight());
    }

    /**
     * Creates the section of the UI for disease-related simulation parameters,
     * including duration and mortality rate.
     *
     * @return VBox containing disease controls
     */
    private VBox createDiseaseBox() {
        HBox durationControl = UIFactory.createLabeledSpinner(
                "Disease Duration (in days):", 1, 60, 14,
                simulatorState::setDuration
        );

        HBox mortalityControl = UIFactory.createSliderWithInput(
                "Mortality Rate (%):", 0, 100, 3.5,
                val -> simulatorState.setMortalityRate(val / 100.0)
        );

        return new SectionBuilder()
                .setTitle("🦠 Disease Parameters")
                .add(durationControl)
                .add(mortalityControl)
                .build();
    }

    /**
     * Creates the species configuration section of the UI.
     * Allows users to set spawn weights and food values for prey and predator species.
     *
     * @return VBox containing species configuration controls
     */
    private VBox createSpeciesBox() {
        VBox speciesList = new VBox(10);
        speciesList.getChildren().add(new Label("Configure Species Spawn Weights:"));

        // Prey species
        speciesList.getChildren().addAll(new Label("Prey:"), new Separator());
        speciesList.getChildren().addAll(
                UIFactory.createSpeciesWeightOption("Deer", simulatorState.getPreyWeight("Deer"), false),
                UIFactory.createSpeciesWeightOption("Hare", simulatorState.getPreyWeight("Hare"), false),
                UIFactory.createSpeciesWeightOption("WildBoar", simulatorState.getPreyWeight("WildBoar"), false)
        );

        // Predator species
        speciesList.getChildren().addAll(new Label("Predators:"), new Separator());
        speciesList.getChildren().addAll(
                UIFactory.createSpeciesWeightOption("Leopard", simulatorState.getPredatorWeight("Leopard"), true),
                UIFactory.createSpeciesWeightOption("Tiger", simulatorState.getPredatorWeight("Tiger"), true)
        );

        // Food values
        speciesList.getChildren().addAll(new Label("Set food level:"), new Separator());
        speciesList.getChildren().addAll(
                UIFactory.createFoodLevelOption("Prey", 1, 20, simulatorState.getPlantFoodValue(), simulatorState::setPlantFoodValue),
                UIFactory.createFoodLevelOption("Predators", 1, 20, simulatorState.getPreyFoodValue(), simulatorState::setPreyFoodValue)
        );

        return new SectionBuilder()
                .setTitle("🦊 Species Configuration")
                .add(speciesList)
                .build();
    }

    /**
     * Creates the UI section for genetics configuration, including mutation rate
     * and various selectable genetic attributes.
     *
     * @return VBox containing genetics configuration controls
     */
    private VBox createGeneticsSettingsBox() {
        HBox mutationControl = UIFactory.createSliderWithInput(
                "Mutation Rate (%):", 0, 100,
                simulatorState.getMutationProbability() * 100,
                val -> simulatorState.setMutationProbability(val / 100.0)
        );

        VBox attributesVBox = new VBox(
                UIFactory.createAttributeOption("Breeding Age", Attributes.BREEDING_AGE),
                UIFactory.createAttributeOption("Max Age", Attributes.MAX_AGE),
                UIFactory.createAttributeOption("Breeding Probability", Attributes.BREEDING_PROBABILITY),
                UIFactory.createAttributeOption("Max Litter Size", Attributes.MAX_LITTER_SIZE),
                UIFactory.createAttributeOption("Disease Probability", Attributes.DISEASE_PROBABILITY),
                UIFactory.createAttributeOption("Metabolism", Attributes.METABOLISM)
        );

        return new SectionBuilder()
                .setTitle("🧪 Genetics Settings")
                .addAll(
                        mutationControl,
                        new Separator(),
                        new Label("Select attributes: "),
                        attributesVBox
                )
                .build();
    }

    /**
     * Creates navigation and reset buttons displayed at the bottom of the settings view.
     * Includes functionality to switch views or reset values to default.
     *
     * @return HBox containing bottom control buttons
     */
    private HBox createBottomButtons() {
        Button backBtn = new Button("⬅ Back");
        Button resetBtn = new Button("↺ Reset to Defaults");

        backBtn.setOnAction(_ -> viewState.switchTo(SimulatorView.class));

        resetBtn.setOnAction(e -> {
            simulatorState.setPredatorWeight("Tiger", 50.0);
            simulatorState.setPredatorWeight("Leopard", 50.0);
            simulatorState.setPreyWeight("Deer", 34.0);
            simulatorState.setPreyWeight("Hare", 33.0);
            simulatorState.setPreyWeight("WildBoar", 33.0);
        });

        HBox buttonBox = new HBox(10, backBtn, resetBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        return buttonBox;
    }

}
