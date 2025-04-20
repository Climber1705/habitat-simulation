package com.tomtrotter.habitatsimulation.model.environment;

import com.tomtrotter.habitatsimulation.model.core.Organism;
import com.tomtrotter.habitatsimulation.model.core.Plant;
import com.tomtrotter.habitatsimulation.model.util.Randomizer;
import com.tomtrotter.habitatsimulation.model.core.Animal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
* The Field class represents a rectangular grid of positions that can hold organisms
* like animals and plants. It provides methods to place, remove, and retrieve organisms,
* as well as utilities for handling adjacent locations and identifying free spaces.
* The field is the main simulation environment where organisms interact with each other
* and the landscape.
* <p>
* This class includes methods to:
* - Place and remove organisms (Animals and Plants)
* - Get organisms at specific locations
* - Get neighboring locations (adjacent, free, etc.)
* - Shuffle and retrieve lists of animals or free spaces nearby
*/

public class Field {

    private static final Random rand = Randomizer.getRandom();
    private final int height;
    private final int width;
    private final Organism[][] field;

    /**
    * Constructs a field with the specified dimensions.
    * The field consists of animals and plants.
    *
    * @param height The number of rows in the field.
    * @param width The number of columns in the field.
    */
    public Field(int height, int width) {
        this.height = height;
        this.width = width;
        field = new Organism[height][width];
    }

    /**
    * Empties the field by removing all animals and plants from it.
    */
    public void clear() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
    * Removes an organism from the specified location.
    *
    * @param location The location to clear of an organism.
    */
    public void removeOrganism(Location location) {
        field[location.getRow()][location.getCol()] = null;
    }

    /**
    * Places an organism at the specified location.
    * If an organism exists at that location, it will be replaced.
    *
    * @param organism The organism to be placed.
    * @param location The location where the organism will be placed.
    */
    public void placeOrganism(Organism organism, Location location) {
        field[location.getRow()][location.getCol()] = organism;
    }

    /**
     * Places an animal at the specified location.
     * Overrides any existing organism at that location.
     *
     * @param animal The animal to be placed.
     * @param location The location where the animal will be placed.
     */
    public void placeOrganism(Animal animal, Location location) {
        field[location.getRow()][location.getCol()] = animal;
    }

    /**
    * Places a plant at the specified location.
    * Overrides any existing organism at that location.
    *
    * @param plant The plant to be placed.
    * @param location The location where the plant will be placed.
    */
    public void placeOrganism(Plant plant, Location location) {
        field[location.getRow()][location.getCol()] = plant;
    }

    /**
    * Replaces a dead animal at the given location with a new plant.
    *
    * @param location The location where the dead animal was removed.
    */
    public void replaceDeadAnimal(Location location) {
        removeOrganism(location); // Remove the dead animal
        Plant newPlant = new Plant(this, location);
        placeOrganism(newPlant, location);
    }

    /**
    * Retrieves the organism at the specified row and column.
    *
    * @param row The row coordinate.
    * @param col The column coordinate.
    * @return The organism at the given location, or null if there is none.
    */
    public Organism getOrganismAt(int row, int col) {
        return field[row][col];
    }


    /**
    * Retrieves the organism at the specified location.
    *
    * @param location The location to check.
    * @return The organism at the given location or null if there is none.
    */
    public Organism getOrganismAt(Location location) {
        return field[location.getRow()][location.getCol()];
    }

    /**
    * Retrieves the plant at the specified location.
    * Returns null if there is no plant at that location.
    *
    * @param row The row coordinate.
    * @param col The column coordinate.
    * @return The plant at the specified location, or null if there is no plant.
    */
    public Plant getPlantAt(int row, int col) {
        Organism organism = field[row][col];
        if(organism instanceof Plant plant) {
            return plant;
        }
        return null;
    }

    /**
    * Retrieves the animal at the specified location.
    * Returns null if there is no animal at that location.
    *
    * @param row The row coordinate.
    * @param col The column coordinate.
    * @return The animal at the specified location, or null if there is no animal.
    */
    public Animal getAnimalAt(int row, int col) {
        Organism organism = field[row][col];
        if(organism instanceof Animal animal) {
            return animal;
        }
        return null;
    }


    /**
    * Retrieves a shuffled list of locations adjacent to the given one.
    * The list will not include the given location itself.
    *
    * @param location The reference location.
    * @return A shuffled list of adjacent locations.
    */
    public List<Location> adjacentLocations(Location location) {
        assert location != null : "Null location passed to adjacent locations";
        
        List<Location> locations = new LinkedList<>();
        int row = location.getRow();
        int col = location.getCol();

        for (int roffset = -1; roffset <= 1; roffset++) {
            int nextRow = row + roffset;
            if (nextRow >= 0 && nextRow < height) {
                for (int coffset = -1; coffset <= 1; coffset++) {
                    int nextCol = col + coffset;

                    // Exclude invalid locations and the original location.
                    if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                        locations.add(new Location(nextRow, nextCol));
                    }
                }
            }
        }

        // Shuffle the list. Several other methods rely on the list
        // being in a random order.
        Collections.shuffle(locations, rand);
        return locations;
    }

    /**
    * Retrieves a shuffled list of living animals in the neighboring locations.
    *
    * @param location The reference location.
    * @return A shuffled list of living neighboring animals.
    */
    public List<Animal> getLivingNeighbours(Location location) {
        assert location != null : "Null location passed to adjacent locations";
        List<Animal> neighbours = new LinkedList<>();

        for (Location loc : adjacentLocations(location)) {
            Organism organism = getOrganismAt(loc);
            if (organism instanceof Animal animal && animal.isAlive()) {
                neighbours.add(animal);
            }
        }

        Collections.shuffle(neighbours, rand);
        return neighbours;
    }

    /**
    * Returns the height (number of rows) of the field.
    *
    * @return The height of the field.
    */
    public int getHeight() {
        return height;
    }

    /**
    * Returns the width (number of columns) of the field.
    *
    * @return The width of the field.
    */
    public int getWidth() {
        return width;
    }

    /**
    * Retrieves a shuffled list of free adjacent locations where no animals are present.
    *
    * @param location The reference location.
    * @return A list of free adjacent locations.
    */
    public List<Location> getFreeAdjacentLocations(Location location) {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = adjacentLocations(location);
        for(Location next : adjacent) {
            Organism organism = getOrganismAt(next);
            if (!(organism instanceof Animal)) {
                free.add(next);
            }
            
        }
        return free;
    }

    /**
    * Retrieves a single free adjacent location near the given location.
    * If no free locations are available, returns null.
    *
    * @param location The reference location.
    * @return A free adjacent location, or null if none are available.
    */
    public Location getFreeAdjacentLocation(Location location) {
        List<Location> free = getFreeAdjacentLocations(location);
        return free.isEmpty() ? null : free.getFirst();
    }

}
