package com.tomtrotter.habitatsimulation.simulation.entities;

import com.tomtrotter.habitatsimulation.simulation.environment.Field;
import com.tomtrotter.habitatsimulation.simulation.environment.Location;
import com.tomtrotter.habitatsimulation.core.domain.Prey;
import com.tomtrotter.habitatsimulation.simulation.genetics.core.Genetics;
import com.tomtrotter.habitatsimulation.simulation.state.SimulatorState;
import com.tomtrotter.habitatsimulation.core.domain.Animal;
import javafx.scene.paint.Color;

/**
* A simple model of a Hare.
* A Hare ages, moves, eats plants, and dies.
* This class extends the Animal class and implements the Prey interface,
* meaning hares can be hunted by predators in the simulation.
* <p>
* Hares can be initialized as either a first-generation hare with random
* age and hunger levels or a newborn with default values.
*/

public class Hare extends Animal implements Prey {

    private static final String hareIcon = "🐰";

    /**
    * Creates a Hare. A hare can be created as a newborn (age zero
    * and with whole food level) or with a random age and hunger level
    * If it is part of the first generation of the simulation.
    *
    * @param isGen1 If true, the hare will have a random age, food level, and disease status.
    * @param field The field where the hare currently exists.
    * @param location The hare's location within the field.
    * @param colour The colour of the hare in the simulation.
    * @param genetics The hare's genetic material.
    */
    public Hare(boolean isGen1, Field field, Location location, Color colour, Genetics genetics) {
        super(field, location, colour, genetics);
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
    * Determines where the hare should move to find food.
    * Delegates the findFood() method to call graze(),
    * which allows the hare to graze on available plants in the field.
    *
    * @return The location where the hare finds food.
    */
    @Override
    protected Location findFood() {
        return graze(this);
    }

    /**
    * Creates a new baby hare with default attributes.
    * This method is called when a hare successfully breeds.
    *
    * @param field The field where the new hare will be placed.
    * @param location The location of the newborn hare within the field.
    * @param colour The colour of the newborn hare in the simulation.
    * @param genetics The genetic material passed to the newborn hare.
    * @return A new instance of a Hare.
    */
    @Override
    protected Animal createBaby(Field field, Location location, Color colour, Genetics genetics) {
        return new Hare(false, field, location, colour, genetics);
    }

    /**
    * Retrieves the Unicode icon representing a hare in the simulation.
    *
    * @return A string containing the hare emoji.
    */
    @Override
    public String getIcon() {
        return hareIcon;
    }

}
