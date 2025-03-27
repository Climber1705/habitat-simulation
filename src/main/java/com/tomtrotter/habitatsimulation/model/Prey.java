package com.tomtrotter.habitatsimulation.model;

import java.util.List;

/**
* A class representing shared behaviours of prey.
* This interface defines the shared behaviors for prey animals, including grazing for plants.
*/

public interface Prey {

    /**
    * Logic for Prey finding and consuming plants.
    * The prey will search for plants in adjacent locations and graze on the first plant found.
    *
    * @param prey: The animal that is grazing (the prey animal looking for food).
    * @return The location where the plant was found and consumed, or null if no plant was found.
    */
    default Location graze(Animal prey) {
        Field field = prey.getField();
        List<Location> adjacent = field.adjacentLocations(prey.getLocation());

        for (Location where : adjacent) {
            Object object = field.getObjectAt(where);
            if (object instanceof Plant plant) {
                plant.setDead();
                prey.setFoodLevel(Animal.PLANT_FOOD_VALUE);
                return where;
            }
        }
        return null;
    }

}
