package com.tomtrotter.habitatsimulation.core.domain;

import com.tomtrotter.habitatsimulation.simulation.state.SimulatorState;
import com.tomtrotter.habitatsimulation.simulation.environment.Field;
import com.tomtrotter.habitatsimulation.simulation.environment.Location;

import java.util.List;

/**
* A class representing shared behaviours of prey.
* This interface defines the shared behaviors for prey animals, including grazing on plants.
*/

public interface Prey {

    /**
    * Logic for Prey finding and consuming plants.
    * The prey will search for plants in adjacent locations and graze on the first plant found.
    *
    * @param prey The animal grazing (the prey animal looking for food).
    * @return The location where the plant was found and consumed, or null if no plant was found.
    */
    default Location graze(Animal prey) {
        Field field = prey.getField();
        List<Location> adjacent = field.adjacentLocations(prey.getLocation());

        for (Location where: adjacent) {
            Object object = field.getOrganismAt(where);
            if (object instanceof Plant plant) {
                plant.setDead();
                prey.setFoodLevel(SimulatorState.getInstance().getPlantFoodValue());
                return where;
            }
        }
        return null;
    }

}
