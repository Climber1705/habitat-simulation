package com.tomtrotter.habitatsimulation.model;

import javafx.scene.paint.Color;

/**
* A class representing characteristics of plants.
* This class models the behavior of plants in a habitat simulation, including their location, colour, and whether they have been consumed.
*/

public class Plant {

    private Location location;
    private final Field field;
    private final Color colour = Color.GREEN;
    private static final String plantIcon = "🌱";

    /**
    * Constructor for objects of class Plant.
    * Initializes a plant with a field and a location.
    * The plant is placed in the given location within the specified field.
    *
    * @param field: The field currently occupied by the plant.
    * @param location: The location within the field where the plant is placed.
    */
    public Plant(Field field, Location location) {
        this.field = field;
        setLocation(location);
    }

    /**
    * Returns the location of the plant.
    *
    * @return The location of the plant.
    */
    public Location getLocation() {
        return location;
    }

    /**
    * Place the plant at a new location within the given field.
    * The plant has been moved from its current location to a new one, and the field has been updated accordingly.
    *
    * @param newLocation: The new location to place the plant.
    */
    protected void setLocation(Location newLocation) {
        if(location != null) {
            field.removePlant(location);
        }
        location = newLocation;
        field.placePlant(this, newLocation);
    }

    /**
    * Returns the colour of the plant.
    *
    * @return The colour of the plant.
    */
    public Color getColour() {
        return colour;
    }

    /**
    * Marks the plant as consumed.
    * The plant is flagged as consumed and removed from the field.
    */
    protected void setDead() {
        if (location != null) {
            field.removePlant(location);
        }
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
