package com.tomtrotter.habitatsimulation.model;

import java.util.List;

import javafx.scene.paint.Color;

/**
* A simple model of a WildBoar. A wild boar ages, moves, eats plants, and can hunt prey.
* It can either graze or hunt based on a predefined probability.
*/

public class WildBoar extends Animal implements Predator, Prey {

    private double HUNT_PROBABILITY = 0.2;
    private static final String wildBoarIcon = "🐗";

    /**
    * Creates a WildBoar. A wild boar can be created as a newborn (age zero and not hungry)
    * or with a random age and food level based on whether it is from the initial generation.
    *
    * @param isGen1: If true, the wild boar will have random age and hunger level.
    * @param field: The field is currently occupied by wild boars.
    * @param location: The location within the field where the wild boar is placed.
    * @param colour: The colour that the wild boar is represented by.
    * @param gene: The genetic code for the wild boar.
    */
    public WildBoar(boolean isGen1, Field field, Location location, Color colour, String gene) {
        super(field, location, colour, gene);
        // If initializing the simulation, set random ages and animal food levels.
        if(isGen1) {
            setAge(rand.nextInt(genetics.getMaxAge()));
            setFoodLevel(rand.nextInt(PLANT_FOOD_VALUE));
            setDisease(rand.nextBoolean());
        }
        else {
            setAge(0);
            setFoodLevel(PLANT_FOOD_VALUE);
            setDisease(false);
        }
    }

    /**
    * Delegates the findFood() call to either the hunt() or graze() method. The wild boar decides whether to hunt
    * or graze based on a random probability. If it hunts, it attempts to find and eat a Hare. If it doesn't hunt, it grazes.
    *
    * @return The location of the food if found, or null if no food is found.
    */
    @Override
    protected Location findFood() {
        if (rand.nextDouble() < HUNT_PROBABILITY) {
            Location huntingLocation = hunt(this, List.of(Hare.class));
            if (huntingLocation != null) {
                return huntingLocation;
            }
        }
        return graze(this);
    }

    /**
    * Creates a new baby WildBoar.
    *
    * @param field: The field where the baby wild boar will be created.
    * @param location: The field where the baby wild boar will be placed.
    * @param colour: The colour of the baby wild boar.
    * @param gene: The genetic code for the baby wild boar.
    * @return A new WildBoar object representing the baby wild boar.
    */
    @Override
    protected Animal createBaby(Field field, Location location, Color colour, String gene) {
        return new WildBoar(false, field, location, colour, gene);
    }

    /**
    * Sets the hunting probability for the wild boar, determining whether it will hunt or graze.
    *
    * @param probability: The probability that the wild boar will hunt—a value between 0 and 1.
    */
    protected void setHuntProbability(double probability) {
        HUNT_PROBABILITY = probability;
    }

    /**
    * Returns the icon representing the WildBoar.
    *
    * @return A string containing the emoji icon for the WildBoar.
    */
    @Override
    public String getIcon() {
        return wildBoarIcon;
    }
}
