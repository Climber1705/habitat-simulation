package com.tomtrotter.habitatsimulation.model;

import java.util.List;

/**
* A class representing shared behaviours of predators.
* This interface defines the shared behaviors for predator animals, including hunting prey.
*/

public interface Predator {

    /**
    * Look for prey adjacent to the current location.
    * Only the first live prey is eaten.
    * The predator will search for prey in a specific order based on the provided list of prey types.
    *
    * @param predator: The predator that is hunting.
    * @param preyTypes: The list of prey types that the predator will consider hunting, in prioritized order.
    * @return The location where prey was found, or null if no prey was found.
    */
    default Location hunt(Animal predator, List<Class<? extends Animal>> preyTypes) {
        Field field = predator.getField();
        List<Location> adjacent = field.adjacentLocations(predator.getLocation());
        
        for(Class<? extends Animal> preyType: preyTypes) {
            for (Location where: adjacent) {
                Object obj = field.getObjectAt(where);
                if (!(obj instanceof Animal prey)) {
                    continue;
                }

                if (preyType.isInstance(prey) && prey.isAlive()) {
                    prey.setDead();
                    predator.setFoodLevel(Animal.PREY_FOOD_VALUE);
                    return where;
                }
            }
        }
        return null;
    }

}
