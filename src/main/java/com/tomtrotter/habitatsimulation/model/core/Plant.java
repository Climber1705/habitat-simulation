package com.tomtrotter.habitatsimulation.model.core;

import com.tomtrotter.habitatsimulation.model.environment.Field;
import com.tomtrotter.habitatsimulation.model.environment.Location;
import javafx.scene.paint.Color;

/**
* A class representing characteristics of plants.
* This class models the behavior of plants in a habitat simulation, including their location, colour, and whether they have been consumed.
*/

public class Plant extends Organism {

    private static final String plantIcon = "🌱";

    /**
    * Constructor for objects of class Plant.
    * Initializes a plant with a field and a location.
    * The plant is placed in the given location within the specified field.
    *
    * @param field The field currently occupied by the plant.
    * @param location The location within the field where the plant is placed.
    */
    public Plant(Field field, Location location) {
        super(field, location, Color.GREEN);
    }

    /**
    * Returns the icon used to represent the plant.
    *
    * @return The icon string for the plant.
    */
    public String getIcon() {
        return plantIcon;
    }

}
