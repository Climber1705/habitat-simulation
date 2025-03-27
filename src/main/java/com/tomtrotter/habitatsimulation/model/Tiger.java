package com.tomtrotter.habitatsimulation.model;

import java.util.List;

import javafx.scene.paint.Color;

/**
* A simple model of a Tiger. A Tiger can age, move, eat prey (such as Deer, Wild Boar, Rabbit),
* and die. It also implements the Predator interface, meaning it hunts for food in the simulation.
*/

public class Tiger extends Animal implements Predator {

    private static final String tigerIcon = "\uD83D\uDC2F";

    /**
    * Creates a Tiger. A tiger can be created as a newborn (age zero and not hungry)
    * or with a random age and food level based on whether it is from the initial generation.
    *
    * @param isGen1: Determines whether the tiger is from the initial generation. If true, the tiger
    *               will have a random age, hunger level, and gene.
    * @param field: The field currently occupied by the tiger.
    * @param location: The location of the tiger within the field.
    * @param colour: The color that the tiger is represented by.
    * @param gene: The tiger's genetic code.
    */
    public Tiger(boolean isGen1, Field field, Location location, Color colour, String gene) {
        super(field, location, colour, gene);
        //If initializing the simulation, set random ages and food levels for animals.
        if(isGen1) {
            setAge(rand.nextInt(genetics.getMaxAge()));
            setFoodLevel(rand.nextInt(PREY_FOOD_VALUE));
            setDisease(rand.nextBoolean());
        }
        else {
            setAge(0);
            setFoodLevel(PREY_FOOD_VALUE);
            setDisease(false);
        }
    }

    /**
     * Determines where the tiger should move to find food.
     * Delegates the findFood() method to call the hunt() method,
     * which allows the tiger to hunt prey like Deer, Wild Boar, or Hare
     * in the field based on the tiger's age.
     * <p>
     * If the tiger is young, it hunts smaller prey (Hare, Deer).
     * Older tigers may hunt larger prey (Deer, Wild Boar, Hare).
     *
     * @return The location where the tiger finds food.
     */
    @Override
    protected Location findFood() {
        List<Class<? extends Animal>> huntOrder = isYoung()
            ? List.of(Hare.class, Deer.class)
            : List.of(WildBoar.class, Deer.class, Hare.class);
            
        return hunt(this, huntOrder);
    }

    /**
    * Creates a new baby tiger with default attributes.
    * This method is called when a tiger successfully breeds.
    *
    * @param field: The field where the new tiger will be placed.
    * @param location: The location of the newborn tiger within the field.
    * @param colour: The colour of the newborn tiger in the simulation.
    * @param gene: The genetic material passed to the newborn tiger.
    * @return A new instance of a Tiger.
    */
    @Override
    protected Animal createBaby(Field field, Location location, Color colour, String gene) {
        return new Tiger(false, field, location, colour, gene);
    }

    /**
    * Returns the icon representing the Tiger.
    *
    * @return A string containing the emoji icon for the Tiger.
    */
    @Override
    public String getIcon() {
        return tigerIcon;
    }

}
