package com.tomtrotter.habitatsimulation.controller;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.tomtrotter.habitatsimulation.model.core.Animal;
import com.tomtrotter.habitatsimulation.model.core.Plant;
import com.tomtrotter.habitatsimulation.model.environment.Field;
import com.tomtrotter.habitatsimulation.model.environment.Location;
import com.tomtrotter.habitatsimulation.model.factory.OrganismFactory;
import com.tomtrotter.habitatsimulation.model.state.ViewState;
import com.tomtrotter.habitatsimulation.model.util.Randomizer;

/**
* A simple predator-prey simulator, based on a rectangular field
* containing predators and prey.
*/

public class Simulator {

    private static final double PREDATOR_CREATION_PROBABILITY = 0.03;
    private static final double PREY_CREATION_PROBABILITY = 0.09;

    private final List<Animal> animals;
    private final Field field;
    private final Random rand = Randomizer.getRandom();
    private int step;

    /**
    * Create a simulation field with the given size.
    * @param height Height of the field. Must be greater than zero.
    * @param width Width of the field. Must be greater than zero.
    */
    public Simulator(int height, int width) {
        
        animals = new ArrayList<>();
        field = new Field(height, width);

        reset();
    }

    /**
    * Run the simulation from its current state for a single step.
    * Iterate over the whole field, updating the state of each
    * animal.
    */
    public void simulateOneStep() {
        step++;
        ViewState.getInstance().setGeneration(step);
        List<Animal> newAnimals = new ArrayList<>();        

        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if(!animal.isAlive()) {
                it.remove();
            }
        }
        animals.addAll(newAnimals);
    }

    /**
    * Reset the simulation to a starting position.
    */
    public void reset() {
        step = 0;
        ViewState.getInstance().setGeneration(step);
        animals.clear();
        populate();
    }

    /**
    * Randomly populate the field with animals.
    */
    private void populate() {
        field.clear();
        OrganismFactory factory = new OrganismFactory(rand, field);

        for (int row = 0; row < field.getHeight(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                double roll = rand.nextDouble();

                if (roll <= PREDATOR_CREATION_PROBABILITY) {
                    Animal predator = factory.createRandomPredator(location);
                    animals.add(predator);
                } else if (roll <= PREY_CREATION_PROBABILITY) {
                    Animal prey = factory.createRandomPrey(location);
                    animals.add(prey);
                } else if (field.getOrganismAt(location) == null) {
                    Plant plant = factory.createPlant(location);
                    field.placeOrganism(plant, location);
                }
            }
        }
    }

    /**
    * Pause for a given time.
    * @param millisecond The time to pause for, in milliseconds.
    */
    public void delay(int millisecond) {
        try {
            Thread.sleep(millisecond);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }

    /**
    * Returns the field.
    * @return the field.
    */
    public Field getField() {
        return field;
    }

    /**
    * Returns generation number.
    * @return the generation number.
    */
    public int getStep() {
        return step;
    }

    /**
    * Returns a list of animals.
    * @return the list of animals.
    */
    public List<Animal> getAnimals() {
        return animals;
    }
}
