package com.tomtrotter.habitatsimulation.ui.components;

import com.tomtrotter.habitatsimulation.simulation.state.SimulatorState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class UIFactory {

    /**
     * Creates an HBox containing a Label and a Spinner.
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
     * Creates a UI component for configuring species weight with a slider.
     *
     * @param speciesName The name of the species.
     * @param initialValue The initial weight value.
     * @param isPredator Whether the species is a predator or prey.
     * @return An HBox containing the species weight configuration UI.
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

        // Bind slider value to text field
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            valueField.setText(String.format("%.1f", newVal.doubleValue()));

            // Update the weight in SimulatorState
            if (isPredator) {
                state.setPredatorWeight(speciesName, newVal.doubleValue());
            } else {
                state.setPreyWeight(speciesName, newVal.doubleValue());
            }
        });

        // Allow manual text input to update slider
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
     * Creates a labeled slider with a text field input to allow exact values.
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

        // Slider -> Label and callback
        slider.valueProperty().addListener((_, _, newVal) -> {
            double value = newVal.doubleValue();
            valueLabel.setText(String.format("%.2f%%", value));
            onValueChange.accept(value);
        });

        // TextField -> Slider and callback
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

    public static HBox createAttributeOption(String attributeName) {
        CheckBox checkBox = new CheckBox(attributeName);
        checkBox.setSelected(true);
        return new HBox(10, checkBox);
    }

    /**
     * Creates a row for food level selection for a given species group.
     *
     * @param name The label for the species group (e.g., "Prey", "Predators").
     * @return An HBox containing the label and spinner for food level selection.
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

