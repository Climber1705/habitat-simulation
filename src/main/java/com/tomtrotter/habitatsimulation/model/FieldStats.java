package com.tomtrotter.habitatsimulation.model;

import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Set;

/**
* Collects and provides statistical data on the state of a field.
* It dynamically tracks and maintains counts for different classes of animals and plants.
*/

public class FieldStats {

    private final HashMap<String, Counter> counters;
    private boolean countsValid;

    /**
    * Constructs a FieldStats object.
    * Initializes the counters for tracking animals and plants.
    */
    public FieldStats() {
        counters = new HashMap<>();
        countsValid = true;
    }

    /**
     * Retrieves a set of the animals and plants in the field.
     *
     * @return A set containing each species in the field.
     */
    public Set<String> getSpecies() {
        return counters.keySet();
    }

    /**
    * Retrieves the population details an animal or plant in the field.
    *
    * @param field: The field to analyze.
    * @return A string containing the descriptions of a species and their count.
    */
    public String getSpeciesDetails(Field field, String species) {
        if (!countsValid) {
            generateCounts(field);
        }
        StringBuilder buffer = new StringBuilder();
        Counter info = counters.get(species);
        buffer.append(" ");
        buffer.append(info.getName());
        buffer.append(": ");
        buffer.append(info.getCount());
        return buffer.toString();
    }

    /**
     * Retrieves a string for the animal or plants icon in the field.
     *
     * @param field: The field to analyze.
     * @return A string containing a species icon.
     */
    public String getSpeciesIcon(Field field, String species) {
        if (!countsValid) {
            generateCounts(field);
        }
        Counter speciesInfo = counters.get(species);
        return speciesInfo.getIcon();
    }

    /**
     * Retrieves a colour for the animal or plants icon in the field.
     *
     * @param field: The field to analyze.
     * @return The colour for the species.
     */
    public Color getSpeciesColour(Field field, String species) {
        if (!countsValid) {
            generateCounts(field);
        }
        Counter speciesInfo = counters.get(species);
        return speciesInfo.getColour();
    }

    /**
    * Retrieves the disease details an animal or plant in the field.
    *
    * @param field: The field to analyze.
    * @return A string containing the descriptions of a species and their disease count.
    */
    public String getSpeciesDisease(Field field, String species) {
        if (!countsValid) {
            generateCounts(field);
        }
        StringBuilder buffer = new StringBuilder();
        Counter info = counters.get(species);
        buffer.append(" ");
        buffer.append(info.getName());
        buffer.append(": ");
        buffer.append(info.getDisease());
        return buffer.toString();
    }

    /**
    * Resets all counters and invalidates the current set of statistics.
    * This ensures that new counts will be generated when needed.
    */
    public void reset() {
        countsValid = false;
        for (String key : counters.keySet()) {
            Counter count = counters.get(key);
            count.reset();
        }
    }

    /**
    * Increments the count for a specific animal species.
    * If the species does not yet have a counter, a new one is created.
    *
    * @param animalClass: The animal instance whose count is to be incremented.
    */
    public void incrementCountAnimal(Animal animalClass) {

        Counter count = counters.get(animalClass.getClass().getSimpleName());
        if (count == null) {
            // We do not have a counter for this species yet. Create one.
            count = new Counter(animalClass.getClass().getSimpleName(), animalClass.getIcon(), animalClass.getColour());
            counters.put(animalClass.getClass().getSimpleName(), count);
        }
        if(animalClass.isDiseased()) {
            count.incrementDisease();
        }
        count.increment();
    }

    /**
    * Increments the count for a specific plant species.
    * If the species does not yet have a counter, a new one is created.
    *
    * @param plantClass: The plant instance whose count is to be incremented.
    */
    public void incrementCountPlant(Plant plantClass) {
        Counter count = counters.get(plantClass.getClass().getSimpleName());
        if (count == null) {
            // We do not have a counter for this species yet. Create one.
            count = new Counter(plantClass.getClass().getSimpleName(), plantClass.getIcon(), plantClass.getColour());
            counters.put(plantClass.getClass().getSimpleName(), count);
        }
        count.increment();
    }

    /**
    * Marks the current count as complete, making the statistics valid.
    */
    public void countFinished() {
        countsValid = true;
    }

    /**
    * Determines if the simulation should continue running.
    * The simulation is considered viable if more than one species has a nonzero count.
    * The simulation stops all the animals from dying.
    *
    * @param field: The field to check for population viability.
    * @return true if there is more than one species alive, false otherwise.
    */
    public boolean isViable(Field field) {
        int nonZero = 0;
        if (!countsValid) {
            generateCounts(field);
        }
        for (String key : counters.keySet()) {
            if(key.equals("Plant")) {
                continue;
            }
            Counter info = counters.get(key);
            if (info.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero >= 1;
    }

    /**
    * Generates and updates the counts of animals and plants in the field.
    * This method scans the field and increments counters accordingly.
    *
    * @param field: The field for which the statistics are generated.
    */
    private void generateCounts(Field field) {
        reset();
        for (int row = 0; row < field.getHeight(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Animal animal = field.getAnimalAt(row, col);
                Plant plant = field.getPlantAt(row, col);
                if (animal != null) {
                    incrementCountAnimal(animal);
                }
                else if (plant != null) {
                    incrementCountPlant(plant);
                }
                
            }
        }
        countsValid = true;
    }

}
