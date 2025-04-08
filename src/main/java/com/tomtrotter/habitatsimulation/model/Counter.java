package com.tomtrotter.habitatsimulation.model;

import javafx.scene.paint.Color;

/**
* The Counter class tracks the number of participants of a specific type in the simulation.
* Each counter has a unique name, an identifying icon, and a count of occurrences.
* It provides methods to retrieve the name, count, and icon and increment or reset the count.
*/

public class Counter {
    
    private final String name, icon;
    private final Color colour;
    private int count, disease, immune;

    /**
    * Initializes a counter for a specific type of participant in the simulation.
    * The counter starts at zero.
    *
    * @param name: The name representing the type of participant (e.g., species name).
    * @param icon: A string representation (emoji or symbol) associated with this type.
    * @param colour: The colour representing this type.
    */
    public Counter(String name, String icon, Color colour) {
        this.name = name;
        this.icon = icon;
        this.colour = colour;
        count = 0;
        disease = 0;
        immune = 0;
    }

    /**
    * Retrieves the name associated with this counter.
    *
    * @return The name of the participant type.
    */
    public String getName() {
        return name;
    }

    /**
    * Retrieves the current count of participants of this type.
    *
    * @return The number of participants recorded in the counter.
    */
    public int getCount() {
        return count;
    }

    /**
    * Retrieves the icon associated with this counter.
    *
    * @return A string representing the participant type's icon.
    */
    public String getIcon() {
        return icon;
    }

    /**
    * Retrieves the colour associated with this counter.
    *
    * @return The colour representing the participant type.
    */
    public Color getColour() {
        return colour;
    }

    /**
    * Retrieves the current count of participants of this type that have a disease.
    *
    * @return The number of participants recorded in the counter that have a disease.
    */
    public int getDisease() {
        return disease;
    }

    /**
    * Retrieves the current count of participants of this type that are immune.
    *
    * @return The number of participants recorded in the counter that are immune.
    */
    public int getImmune() {
        return immune;
    }

    /**
    * Increments the count by one, representing the addition of a new participant of this type.
    */
    public void increment() {
        count++;
    }

    /**
    * Increments the count by one, representing the addition of a new diseased participant of this type.
    */
    public void incrementDisease() {
        disease++;
    }

    /**
    * Increments the count by one, representing the addition of a new immune participant of this type.
    */
    public void incrementImmune() {
        immune++;
    }

    /**
    * Resets the count to zero, clearing the current tally of participants.
    */
    public void reset() {
        count = 0;
        disease = 0;
        immune = 0;
    }

}
