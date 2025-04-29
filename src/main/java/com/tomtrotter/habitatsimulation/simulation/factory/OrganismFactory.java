package com.tomtrotter.habitatsimulation.simulation.factory;

import com.tomtrotter.habitatsimulation.core.domain.Plant;
import com.tomtrotter.habitatsimulation.core.domain.Animal;
import com.tomtrotter.habitatsimulation.simulation.environment.Field;
import com.tomtrotter.habitatsimulation.simulation.environment.Location;
import com.tomtrotter.habitatsimulation.simulation.entities.*;
import com.tomtrotter.habitatsimulation.simulation.state.SimulatorState;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.Map;
import java.util.HashMap;

/**
 * The OrganismFactory class is responsible for creating instances of animals and plants in the simulation.
 * It can create various types of predators, prey, and plants based on weighted probabilities.
 * The factory class uses weighted randomization to determine which type of predator or prey to create.
 */
public class OrganismFactory {

    private final Random rand;
    private final Field field;
    private final SimulatorState state;

    // Colors for different animals
    private final Color tigerColour = Color.ORANGE;
    private final Color leopardColour = Color.YELLOW;
    private final Color hareColour = Color.BROWN;
    private final Color deerColour = Color.SADDLEBROWN;
    private final Color wildBoarColour = Color.DARKGRAY;

    /**
     * Constructs an OrganismFactory with the given random number generator and field.
     *
     * @param rand The random number generator used for weighted randomization.
     * @param field The field where the created organisms will be placed.
     */
    public OrganismFactory(Random rand, Field field) {
        this.rand = rand;
        this.field = field;
        this.state = SimulatorState.getInstance();
    }

    /**
     * Creates a weighted random predator at the specified location.
     * The probability of each predator type is based on its weight in SimulatorState.
     *
     * @param location The location on the field where the predator will be placed.
     * @return A weighted randomly selected predator.
     */
    public Animal createRandomPredator(Location location) {
        Map<String, Double> weights = state.getPredatorWeights();
        double totalWeight = weights.values().stream().mapToDouble(Double::doubleValue).sum();

        if (totalWeight <= 0) {
            // Fallback to avoid division by zero
            return new Tiger(true, field, location, tigerColour, "");
        }

        double value = rand.nextDouble() * totalWeight;
        double cumulativeWeight = 0.0;

        for (Map.Entry<String, Double> entry : weights.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (value <= cumulativeWeight) {
                String predatorType = entry.getKey();
                return switch (predatorType) {
                    case "Tiger" -> new Tiger(true, field, location, tigerColour, "");
                    case "Leopard" -> new Leopard(true, field, location, leopardColour, "");
                    default -> throw new IllegalStateException("Unknown predator type: " + predatorType);
                };
            }
        }

        // Default fallback (should never reach here if weights are properly configured)
        return new Tiger(true, field, location, tigerColour, "");
    }

    /**
     * Creates a weighted random prey at the specified location.
     * The probability of each prey type is based on its weight in SimulatorState.
     *
     * @param location The location on the field where the prey will be placed.
     * @return A weighted randomly selected prey.
     */
    public Animal createRandomPrey(Location location) {
        Map<String, Double> weights = state.getPreyWeights();
        double totalWeight = weights.values().stream().mapToDouble(Double::doubleValue).sum();

        if (totalWeight <= 0) {
            // Fallback to avoid division by zero
            return new Hare(true, field, location, hareColour, "");
        }

        double value = rand.nextDouble() * totalWeight;
        double cumulativeWeight = 0.0;

        for (Map.Entry<String, Double> entry : weights.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (value <= cumulativeWeight) {
                String preyType = entry.getKey();
                return switch (preyType) {
                    case "Hare" -> new Hare(true, field, location, hareColour, "");
                    case "Deer" -> new Deer(true, field, location, deerColour, "");
                    case "WildBoar" -> new WildBoar(true, field, location, wildBoarColour, "");
                    default -> throw new IllegalStateException("Unknown prey type: " + preyType);
                };
            }
        }

        // Default fallback (should never reach here if weights are properly configured)
        return new Hare(true, field, location, hareColour, "");
    }

    /**
     * Creates a plant at the specified location.
     *
     * @param location The location on the field where the plant will be placed.
     * @return A new Plant instance.
     */
    public Plant createPlant(Location location) {
        return new Plant(field, location);
    }
}