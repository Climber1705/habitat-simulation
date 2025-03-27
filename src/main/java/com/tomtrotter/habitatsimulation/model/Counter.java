package com.tomtrotter.habitatsimulation.model;

/**
* The Counter class tracks the number of participants of a specific type in the simulation.
* Each counter is associated with a unique name, an identifying icon, and a count of occurrences.
* It provides methods to retrieve the name, count, and icon, as well as to increment or reset the count.
*/

public class Counter {
    
    private final String name, icon;
    private int count;

    /**
    * Initializes a counter for a specific type of participant in the simulation.
    * The counter starts at zero.
    *
    * @param name: The name representing the type of participant (e.g., species name).
    * @param icon: A string representation (emoji or symbol) associated with this type.
    */
    public Counter(String name, String icon) {
        this.name = name;
        this.icon = icon;
        count = 0;
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
    * Increments the count by one, representing the addition of a new participant of this type.
    */
    public void increment() {
        count++;
    }

    /**
    * Resets the count to zero, clearing the current tally of participants.
    */
    public void reset() {
        count = 0;
    }

}
