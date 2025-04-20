package com.tomtrotter.habitatsimulation.model.factory;

import com.tomtrotter.habitatsimulation.model.core.Plant;
import com.tomtrotter.habitatsimulation.model.core.Animal;
import com.tomtrotter.habitatsimulation.model.entities.*;
import com.tomtrotter.habitatsimulation.model.environment.Field;
import com.tomtrotter.habitatsimulation.model.environment.Location;
import javafx.scene.paint.Color;

import java.util.Random;

/**
* The OrganismFactory class is responsible for creating instances of animals and plants in the simulation.
* It can create various types of predators, prey, and plants.
* The factory class uses randomization to determine which type of predator or prey to create,
* allowing for more varied and dynamic simulations.
*/

public class OrganismFactory {

    private final Random rand;

    private final Field field;

    private final Color tigerColour = Color.ORANGE;
    private final Color leopardColour = Color.YELLOW;
    private final Color hareColour = Color.BROWN;
    private final Color deerColour = Color.SADDLEBROWN;
    private final Color wildBoarColour = Color.DARKGRAY;

    /**
     * Constructs an OrganismFactory with the given random number generator and field.
     *
     * @param rand The random number generator used for randomizing predator and prey selection.
     * @param field The field where the created organisms will be placed.
     */
    public OrganismFactory(Random rand, Field field) {
        this.rand = rand;
        this.field = field;
    }

    /**
     * Creates a random predator (Tiger or Leopard) at the specified location.
     *
     * @param location The location on the field where the predator will be placed.
     * @return A randomly selected predator (either a Tiger or a Leopard).
     */
    public Animal createRandomPredator(Location location) {
        final int NUMBER_OF_PREDATOR_TYPES = 2;

        return switch (rand.nextInt(NUMBER_OF_PREDATOR_TYPES)) {
            case 0 -> new Tiger(true, field, location, tigerColour, "");
            case 1 -> new Leopard(true, field, location, leopardColour, "");
            default -> throw new IllegalStateException("Unknown predator type");
        };
    }

    /**
     * Creates a random prey (Hare, Deer, or WildBoar) at the specified location.
     *
     * @param location The location on the field where the prey will be placed.
     * @return A randomly selected prey (either a Hare, Deer, or WildBoar).
     */
    public Animal createRandomPrey(Location location) {
        final int NUMBER_OF_PREY_TYPES = 3;

        return switch (rand.nextInt(NUMBER_OF_PREY_TYPES)) {
            case 0 -> new Hare(true, field, location, hareColour, "");
            case 1 -> new Deer(true, field, location, deerColour, "");
            case 2 -> new WildBoar(true, field, location, wildBoarColour, "");
            default -> throw new IllegalStateException("Unknown prey type");
        };
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
