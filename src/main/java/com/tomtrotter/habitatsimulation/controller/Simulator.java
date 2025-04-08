package com.tomtrotter.habitatsimulation.controller;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.tomtrotter.habitatsimulation.model.*;
import javafx.scene.paint.Color;

/**
* A simple predator-prey simulator, based on a rectangular field
* containing predators and prey.
*/

public class Simulator {

    private static final double PREDATOR_CREATION_PROBABILITY = 0.03;
    private static final double PREY_CREATION_PROBABILITY = 0.09;
    private static final int NUMBER_OF_PREDATOR_TYPES = 2;
    private static final int NUMBER_OF_PREY_TYPES = 3;
    private static final Color tigerColour = Color.ORANGE;
    private static final Color leopardColour = Color.YELLOW;
    private static final Color hareColour = Color.PINK;
    private static final Color deerColour = Color.BROWN;
    private static final Color wildBoarColour = Color.BLACK;

    private final List<Animal> animals;
    private final Field field;
    private final Random rand = Randomizer.getRandom();
    private int step;

    /**
    * Create a simulation field with the given size.
    * @param height: Height of the field. Must be greater than zero.
    * @param width: Width of the field. Must be greater than zero.
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
    public void simulateOneStep(int diseaseDuration, double moralityRate) {
        ++step;
        List<Animal> newAnimals = new ArrayList<>();        

        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if(!animal.isAlive()) {
                it.remove();
            }
        }
        animals.addAll(newAnimals);
        animals.forEach(animal -> {
            animal.disease.setDuration(diseaseDuration);
            animal.disease.setMortalityRate(moralityRate);
        });
    }

    /**
    * Reset the simulation to a starting position.
    */
    public void reset() {
        step = 0;
        animals.clear();
        populate();
    }

    /**
    * Randomly populate the field with animals.
    */
    private void populate() {
        field.clear();
        for(int row = 0; row < field.getHeight(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= PREDATOR_CREATION_PROBABILITY) {
                    switch (rand.nextInt(NUMBER_OF_PREDATOR_TYPES)) {
                        // Adds a tiger
                        case 0: {
                                    Location location = new Location(row, col);
                                    Tiger tiger = new Tiger(true, field, location, tigerColour,  "");
                                    animals.add(tiger);
                                    break;
                        }
                        // Adds a leopard
                        case 1: {
                                    Location location = new Location(row, col);
                                    Leopard leopard = new Leopard(true, field, location, leopardColour,"");
                                    animals.add(leopard);
                                    break;
                        }                        
                    }
                }

                else if(rand.nextDouble() <= PREY_CREATION_PROBABILITY) {
                    switch (rand.nextInt(NUMBER_OF_PREY_TYPES)) {
                        // Adds a Hare
                        case 0: {
                                    Location location = new Location(row, col);
                                    Hare hare = new Hare(true, field, location, hareColour,"");
                                    animals.add(hare);
                                    break;
                        }
                        // Adds a deer
                        case 1: {
                                    Location location = new Location(row, col);
                                    Deer deer = new Deer(true, field, location, deerColour,"");
                                    animals.add(deer);
                                    break;
                        }
                        // Adds a wild boar
                        case 2: {
                                    Location location = new Location(row, col);
                                    WildBoar wildBoar = new WildBoar(true, field, location, wildBoarColour,"");
                                    animals.add(wildBoar);
                                    break;
                        }
                    }
                }
                
                // Adds a plant
                else {
                    Location location = new Location(row, col);
                    if (field.getObjectAt(location) == null) {
                        Plant newPlant = new Plant(field, location);
                        field.placePlant(newPlant, location);
                    }
                }
            }
        }
    }

    /**
    * Pause for a given time.
    * @param millisecond: The time to pause for, in milliseconds.
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
