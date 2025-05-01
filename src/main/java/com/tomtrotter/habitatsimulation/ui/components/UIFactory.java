package com.tomtrotter.habitatsimulation.ui.components;

import com.tomtrotter.habitatsimulation.simulation.genetics.attributes.Attributes;
import com.tomtrotter.habitatsimulation.simulation.genetics.attributes.GeneticAttributeManager;
import com.tomtrotter.habitatsimulation.simulation.state.SimulatorState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/**
* A factory class that provides methods for creating various UI components used in the habitat simulation.
* This class centralizes the creation of common UI elements with consistent styling and behavior
* across the application.
*/
public class UIFactory {

    /**
    * Creates an HBox containing a Label and a Spinner for integer input.
    *
    * @param labelText The text to display on the label
    * @param min The minimum allowable value for the spinner
    * @param max The maximum allowable value for the spinner
    * @param initialValue The starting value for the spinner
    * @param onValueChange Consumer that will be invoked when the spinner value changes
    * @return An HBox containing the labeled spinner component
    */
    public static HBox createLabeledSpinner(String labelText, int min, int max, int initialValue, Consumer<Integer> onValueChange) {
        Label label = new Label(labelText);
        Spinner<Integer> spinner = new Spinner<>(min, max, initialValue);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((_,_, newVal) -> {
            onValueChange.accept(newVal);
        });
        HBox hBox = new HBox(10, label, spinner);
        hBox.setPadding(new Insets(5));
        return hBox;
    }

    /**
    * Creates a UI component for configuring species weight with a slider and text field.
    * This component updates the SimulatorState automatically when the weight value changes.
    *
    * @param speciesName The name of the species
    * @param initialValue The initial weight value for the species
    * @param isPredator Whether the species is a predator (true) or prey (false)
    * @return An HBox containing the species weight configuration UI components
    */
    public static HBox createSpeciesWeightOption(String speciesName, double initialValue, boolean isPredator) {
        SimulatorState state = SimulatorState.getInstance();

        Label nameLabel = new Label(speciesName + ":");
        nameLabel.setMinWidth(80);

        Slider slider = new Slider(0, 100, initialValue);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(25);
        slider.setMinorTickCount(5);
        slider.setPrefWidth(200);

        TextField valueField = new TextField(String.format("%.1f", initialValue));
        valueField.setPrefWidth(50);

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            valueField.setText(String.format("%.1f", newVal.doubleValue()));

            if (isPredator) {
                state.setPredatorWeight(speciesName, newVal.doubleValue());
            } else {
                state.setPreyWeight(speciesName, newVal.doubleValue());
            }
        });

        valueField.setOnAction(e -> {
            try {
                double value = Double.parseDouble(valueField.getText());
                slider.setValue(value);
            } catch (NumberFormatException ex) {
                valueField.setText(String.format("%.1f", slider.getValue()));
            }
        });

        HBox container = new HBox(10, nameLabel, slider, valueField);
        container.setAlignment(Pos.CENTER_LEFT);

        return container;
    }

    /**
    * Creates a labeled slider with a text field input that allows entering exact values.
    * The component includes a value label displaying the current percentage value.
    *
    * @param labelText The text to display on the label
    * @param min The minimum value for the slider
    * @param max The maximum value for the slider
    * @param initialValue The starting value for the slider
    * @param onValueChange Consumer that will be invoked when the slider or text field value changes
    * @return An HBox containing the slider with text input component
    */
    public static HBox createSliderWithInput(String labelText, double min, double max, double initialValue, Consumer<Double> onValueChange) {
        Label label = new Label(labelText);
        Slider slider = new Slider(min, max, initialValue);
        slider.setMajorTickUnit((max - min) / 4);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        TextField inputField = new TextField();
        inputField.setPrefWidth(60);
        Label valueLabel = new Label(String.format("%.2f%%", initialValue));

        slider.valueProperty().addListener((_, _, newVal) -> {
            double value = newVal.doubleValue();
            valueLabel.setText(String.format("%.2f%%", value));
            onValueChange.accept(value);
        });

        inputField.setOnAction(e -> {
            try {
                double input = Double.parseDouble(inputField.getText());
                if (input >= min && input <= max) {
                    slider.setValue(input);
                    onValueChange.accept(input);
                }
            } catch (NumberFormatException ignored) {}
            inputField.setText("");
        });
        VBox vBox = new VBox(label, valueLabel);
        HBox hBox = new HBox(10, vBox, slider, inputField);
        hBox.setPadding(new Insets(5));
        return hBox;
    }

    /**
    * Creates a checkbox option for enabling or disabling a genetic attribute.
    * The component automatically updates the GeneticAttributeManager when toggled.
    *
    * @param attributeName The display name for the attribute
    * @param attribute The enum value representing the attribute
    * @return An HBox containing the attribute checkbox
    */
    public static HBox createAttributeOption(String attributeName, Attributes attribute) {
        CheckBox checkBox = new CheckBox(attributeName);
        checkBox.setSelected(true);
        checkBox.setOnAction(e -> {
            GeneticAttributeManager.getInstance().setAttributeActive(attribute, checkBox.isSelected());
        });
        return new HBox(10, checkBox);
    }

    /**
    * Creates a component for selecting food level for a given species group.
    * The component consists of a label and a spinner for numerical input.
    *
    * @param name The label text for the species group (e.g., "Prey", "Predators")
    * @param min The minimum allowable food level value
    * @param max The maximum allowable food level value
    * @param initialValue The starting food level value
    * @param onValueChange Consumer that will be invoked when the food level changes
    * @return An HBox containing the food level selection components
    */
    public static HBox createFoodLevelOption(String name, int min, int max, int initialValue, Consumer<Integer> onValueChange) {
        Label foodLabel = new Label(name);
        Spinner<Integer> foodLevelSpinner = new Spinner<>(min, max, initialValue);
        foodLevelSpinner.valueProperty().addListener((_,_, newVal) -> {
            onValueChange.accept(newVal);
        });
        return new HBox(10, foodLabel, foodLevelSpinner);
    }

}