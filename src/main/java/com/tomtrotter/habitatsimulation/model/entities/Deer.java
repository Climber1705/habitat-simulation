package com.tomtrotter.habitatsimulation.model.entities;

import com.tomtrotter.habitatsimulation.model.environment.Field;
import com.tomtrotter.habitatsimulation.model.environment.Location;
import com.tomtrotter.habitatsimulation.model.core.Prey;
import com.tomtrotter.habitatsimulation.model.state.SimulatorState;
import com.tomtrotter.habitatsimulation.model.core.Animal;
import javafx.scene.paint.Color;

/**
* A simple model of a Deer in the habitat simulation.
* The Deer ages, moves, grazes for food, and can die.
* It implements the `Prey` interface to reflect its behavior in the ecosystem.
*/

public class Deer extends Animal implements Prey {

    private static final String deerIcon = "🦌";

    /**
    * Creates a Deer instance. A Deer can either be a newborn (age 0, not hungry) or have a random age
    * and hunger level based on the first generation flag (`isGen1`).
    *
    * @param isGen1 If true, the deer will have a random age and hunger level. If false, it will start as a newborn.
    * @param field The field where the deer is located.
    * @param location The location within the field where the deer will be placed.
    * @param colour The colour representation of the deer in the simulation.
    * @param gene The genetic material assigned to the deer.
    */
    public Deer(boolean isGen1, Field field, Location location, Color colour, String gene) {
        super(field, location, colour, gene);
        // If initializing the simulation, set random ages and animal food levels.
        if(isGen1) {
            setAge(rand.nextInt(genetics.getMaxAge()));
            setFoodLevel(rand.nextInt(SimulatorState.getInstance().getPlantFoodValue()));
            disease.setInfected(rand.nextBoolean());
        }
        else {
            setAge(0);
            setFoodLevel(SimulatorState.getInstance().getPlantFoodValue());
            disease.setInfected(false);
        }
    }

    /**
    * Searches for food for the deer. In this case, it delegates the food search to the `graze()` method,
    * Deer are herbivores that graze on plants.
    *
    * @return The food's location, or null if no food is found.
    */
    @Override
    protected Location findFood() {
        return graze(this);
    }

    /**
    * Creates a new baby deer. The newborn deer inherits the field, location, colour, and genetic material
    * from its parents.
    *
    * @param field The field where the newborn deer will be placed.
    * @param location The location where the newborn deer will be placed.
    * @param colour The colour representation of the newborn deer in the simulation.
    * @param gene The genetic material passed on to the newborn deer.
    * @return A new instance of the `Deer` class, representing the newborn.
    */
    @Override
    protected Animal createBaby(Field field, Location location, Color colour, String gene) {
        return new Deer(false, field, location, colour, gene);
    }

    /**
    * Retrieves the icon that represents the deer in the simulation.
    * This method returns the emoji 🦌 to symbolize a deer.
    *
    * @return A string representing the deer's icon ("🦌").
    */
    @Override
    public String getIcon() {
        return deerIcon;
    }

}
