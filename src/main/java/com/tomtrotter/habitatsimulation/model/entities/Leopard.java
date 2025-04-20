package com.tomtrotter.habitatsimulation.model.entities;

import java.util.List;

import com.tomtrotter.habitatsimulation.model.core.Animal;
import com.tomtrotter.habitatsimulation.model.core.Predator;
import com.tomtrotter.habitatsimulation.model.environment.Field;
import com.tomtrotter.habitatsimulation.model.environment.Location;
import com.tomtrotter.habitatsimulation.model.state.SimulatorState;
import javafx.scene.paint.Color;

/**
* A simple model of a Leopard.
* A Leopard ages, moves, hunts prey (such as Deer, Wild Boar, Rabbit), and dies.
* The Leopard is a predator in the simulation and interacts with various prey animals.
*/

public class Leopard extends Animal implements Predator {

    private static final String leopardIcon = "🐆";

    /**
    * Creates a Leopard. A leopard can be created as a newborn (age zero
    * and with whole food level) or with a random age and hunger level
    * If it is part of the first generation of the simulation.
    *
    * @param isGen1 If true, the leopard will have a random age, food level, and disease status.
    * @param field The field where the leopard currently exists.
    * @param location The leopard's location within the field.
    * @param colour The colour of the leopard in the simulation.
    * @param gene The leopard's genetic material.
    */
    public Leopard(boolean isGen1, Field field, Location location, Color colour, String gene) {
        super(field, location, colour, gene);
        // If initializing the simulation, set random ages and animal food levels.
        if(isGen1) {
            setAge(rand.nextInt(genetics.getMaxAge()));
            setFoodLevel(rand.nextInt(SimulatorState.getInstance().getPreyFoodValue()));
            disease.setInfected(rand.nextBoolean());
        }
        else {
            setAge(0);
            setFoodLevel(SimulatorState.getInstance().getPreyFoodValue());
            disease.setInfected(false);
        }
    }

    /**
    * Determines where the leopard should move to find food.
    * Delegates the findFood() method to call the hunt() method,
    * which allows the leopard to hunt prey like Deer, Wild Boar, or Hare
    * in the field based on the leopard's age.
    * <p>
    * The leopard hunts smaller prey (Hare, Deer) if it is young.
    * Older leopards may hunt larger prey (Deer, Wild Boar, Hare).
    *
    * @return The location where the leopard finds food.
    */
    @Override
    protected Location findFood() {
        List<Class<? extends Animal>> huntOrder = isYoung()
            ? List.of(Hare.class, Deer.class)
            : List.of(Deer.class, Hare.class, WildBoar.class);
            
        return hunt(this, huntOrder);
    }

    /**
    * Creates a new baby leopard with default attributes.
    * This method is called when a leopard successfully breeds.
    *
    * @param field The field where the new leopard will be placed.
    * @param location The location of the newborn leopard within the field.
    * @param colour The colour of the newborn leopard in the simulation.
    * @param gene The genetic material passed to the newborn leopard.
    * @return A new instance of a Leopard.
    */
    @Override
    protected Animal createBaby(Field field, Location location, Color colour, String gene) {
        return new Leopard(false, field, location, colour, gene);
    }

    /**
    * Retrieves the Unicode icon representing a leopard in the simulation.
    *
    * @return A string containing the leopard emoji.
    */
    @Override
    public String getIcon() {
        return leopardIcon;
    }

}
