package com.tomtrotter.habitatsimulation.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
* Represents a rectangular grid of field positions.
* Each position in the grid can store an Animal or a Plant.
* This class provides methods to place, remove, and retrieve objects from the field.
* It also provides utilities for finding adjacent locations and free spaces.
*/

public class Field {
    private static final Random rand = Randomizer.getRandom();
    private final int height;
    private final int width;
    private final Animal[][] field;
    private final Plant[][] plantField;

    /**
    * Constructs a field with the specified dimensions.
    * The field consists of two grids: one for animals and one for plants.
    *
    * @param height: The number of rows in the field.
    * @param width: The number of columns in the field.
    */
    public Field(int height, int width) {
        this.height = height;
        this.width = width;
        field = new Animal[height][width];
        plantField = new Plant[height][width];
    }

    /**
    * Empties the field by removing all animals and plants from it.
    */
    public void clear() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = null;
                plantField[row][col] = null;
            }
        }
    }

    /**
    * Removes an animal from the specified location.
    *
    * @param location: The location to clear of an animal.
    */
    public void removeAnimal(Location location) {
        field[location.getRow()][location.getCol()] = null;
    }

    /**
    * Removes a plant from the specified location.
    *
    * @param location: The location to clear of a plant.
    */
    public void removePlant(Location location) {
        plantField[location.getRow()][location.getCol()] = null;
    }

    /**
    * Places an animal at the specified location.
    * If an animal already exists at that location, it will be replaced.
    *
    * @param animal: The animal to be placed.
    * @param location: The location where the animal will be placed.
    */
    public void placeAnimal(Animal animal, Location location) {
        field[location.getRow()][location.getCol()] = animal;
    }

    /**
    * Places a plant at the specified location.
    * If a plant already exists at that location, it will be replaced.
    *
    * @param plant: The plant to be placed.
    * @param location: The location where the plant will be placed.
    */
    public void placePlant(Plant plant, Location location) {
        plantField[location.getRow()][location.getCol()] = plant;
    }

    /**
    * Replaces a dead animal at the given location with a new plant.
    *
    * @param location: The location where the dead animal was removed.
    */
    public void replaceDeadAnimal(Location location) {
        Plant newPlant = new Plant(this, location);
        placePlant(newPlant, location);
    }

    /**
    * Retrieves the animal at the specified row and column.
    *
    * @param row: The row coordinate.
    * @param col: The column coordinate.
    * @return The animal at the given location, or null if there is none.
    */
    public Animal getAnimalAt(int row, int col) {
        return field[row][col];
    }

    /**
    * Retrieves the plant at the specified row and column.
    *
    * @param row: The row coordinate.
    * @param col: The column coordinate.
    * @return The plant at the given location, or null if there is none.
    */
    public Plant getPlantAt(int row, int col) {
        return plantField[row][col];
    }


    /**
    * Retrieves the object (either an animal or a plant) at the specified location.
    *
    * @param location: The location to check.
    * @return The object at the given location, or null if there is none.
    */
    public Object getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }

    /**
    * Retrieves the object (either an animal or a plant) at the specified row and column.
    *
    * @param row: The row coordinate.
    * @param col: The column coordinate.
    * @return The object at the given location, or null if there is none.
    */
    public Object getObjectAt(int row, int col) {
        Animal animal = getAnimalAt(row,col);
        Plant plant = getPlantAt(row,col);
        if (animal != null) {
            return animal;    
        }
        else return plant;
    }

    /**
    * Retrieves a shuffled list of locations adjacent to the given one.
    * The list will not include the given location itself.
    *
    * @param location: The reference location.
    * @return A shuffled list of adjacent locations.
    */
    public List<Location> adjacentLocations(Location location) {
        assert location != null : "Null location passed to adjacentLocations";
        
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
    * @param location: The reference location.
    * @return A shuffled list of living neighboring animals.
    */
    public List<Animal> getLivingNeighbours(Location location) {
      assert location != null : "Null location passed to adjacentLocations";
      List<Animal> neighbours = new LinkedList<>();

        List<Location> adjLocations = adjacentLocations(location);

        for (Location loc : adjLocations) {
          Animal animal = field[loc.getRow()][loc.getCol()];
          // If there's empty space
          if(animal == null) {
              continue;
          }
          if (animal.isAlive())
            neighbours.add(animal);
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
    * @param location: The reference location.
    * @return A list of free adjacent locations.
    */
    public List<Location> getFreeAdjacentLocations(Location location) {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = adjacentLocations(location);
        for(Location next : adjacent) {
            Object object = getObjectAt(next);
            if (!(object instanceof Animal)) {
                free.add(next);
            }
            
        }
        return free;
    }

    /**
    * Retrieves a single free adjacent location near the given location.
    * If no free locations are available, returns null.
    *
    * @param location: The reference location.
    * @return A free adjacent location, or null if none are available.
    */
    public Location getFreeAdjacentLocation(Location location) {
        
        List<Location> free = getFreeAdjacentLocations(location);
        if(!free.isEmpty()) {
            return free.getFirst();
        }
        else {
            return null;
        }
    }

}
