package com.tomtrotter.habitatsimulation.model.core;

import com.tomtrotter.habitatsimulation.model.environment.Field;
import com.tomtrotter.habitatsimulation.model.environment.Location;
import javafx.scene.paint.Color;

/**
* The Organism class represents an organism in the habitat simulation.
* It includes properties like the organism's location within a field, its color,
* and whether it is alive or dead. The class also provides methods to update the organism's
* location, check if it is alive, and remove it from the field if it dies.
* <p>
* The organism is associated with a specific field and can be placed or removed from
* various locations within that field during the simulation.
*/

public class Organism {

    private final Field field;
    private Location location;
    private final Color colour;
    private boolean isAlive;

    /**
    * Constructor to initialize an organism with a field, location, and colour.
    *
    * @param field The field where the organism will reside.
    * @param location The starting location of the organism.
    * @param colour The color representing the organism.
    */
    public Organism(Field field, Location location, Color colour) {
        isAlive = true;
        this.field = field;
        this.colour = colour;
        setLocation(location);
    }

    /**
    * Checks whether the organism is still alive.
    *
    * @return true if the organism is alive, false otherwise.
    */
    public boolean isAlive() {
        return isAlive;
    }

    /**
    * Marks the organism as dead, removing it from the field.
    * If the organism has a valid location, it is also removed from the simulation.
    */
    protected void setDead() {
        isAlive = false;
        if (location != null) {
            field.removeOrganism(location);
        }
    }

    /**
    * Retrieves the organism's current location.
    *
    * @return The location of the organism.
    */
    public Location getLocation() {
        return location;
    }

    /**
    * Updates the organism's location to a new position within the field.
    * If the organism had a previous location, it is removed from that location first.
    *
    * @param newLocation The new location for the organism.
    */
    protected void setLocation(Location newLocation) {
        if(location != null) {
            field.removeOrganism(location);
        }
        location = newLocation;
        field.placeOrganism(this, newLocation);
    }

    /**
    * Retrieves the field in which the organism resides.
    *
    * @return The field containing the organism.
    */
    protected Field getField() {
        return field;
    }

    /**
    * Retrieves the colour of the organism.
    *
    * @return The colour of the organism.
    */
    public Color getColour() {
        return colour;
    }

}
