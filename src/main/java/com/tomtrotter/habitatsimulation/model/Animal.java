package com.tomtrotter.habitatsimulation.model;

import java.util.List;

import javafx.scene.paint.Color;
import java.util.Random;

/**
* The Animal class serves as an abstract blueprint for all animal types in the habitat simulation.
* It defines common attributes such as age, food level, gender, disease status, and location within a field.
* <p>
* This class provides core functionality for:
* - Movement and survival (ageing, hunger, disease progression).
* - Reproduction and genetics-based inheritance.
* - Interaction with the environment, including finding food and dealing with disease.
* <p>
* Subclasses must implement methods for species-specific behaviors, such as `findFood()` and `createBaby()`.
*/

public abstract class Animal {

    private final Field field;
    private final Color colour;

    private Location location;
    private int age, disease;
    private double foodLevel;
    private boolean isAlive, isMale, hasDisease;

    protected static final int PLANT_FOOD_VALUE = 9;
    protected static final int PREY_FOOD_VALUE = 9;
    protected static final int DISEASE_DURATION = 5;

    public final Random rand = Randomizer.getRandom();
    public final Genetics genetics;

    /**
    * Creates a new animal at a specific location in a given field.
    * The animal is initialized with genetic information and a random gender.
    *
    * @param field: The field currently occupied by the animal.
    * @param location: The initial location of the animal within the field.
    * @param colour: The colour of the animal.
    * @param gene: The genetic material of the animal.
    */
    public Animal(Field field, Location location, Color colour, String gene) {
        isAlive = true;
        this.field = field;
        this.colour = colour;
        genetics = new Genetics(gene);

        setLocation(location);
        setGender(rand.nextBoolean());
    }

    /**
    * Defines the actions an animal performs in a simulation step.
    * The animal ages, experiences hunger, attempt to reproduce, and searches for food.
    * It may also contract or suffer from disease. If conditions become fatal, it dies.
    *
    * @param newAnimals: A list to store newly created animals (offspring).
    */
    public void act(List<Animal> newAnimals) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            // Only give birth if the animal is female.
            if (!this.getGender()) {
                giveBirth(newAnimals);
            }

            // If the animal has a disease, increase the counter; otherwise, it has a chance of getting the disease.
            if (this.hasDisease) {
                incrementDisease();
            } else {
                getDisease();
            }

            // Find a source of food; if unavailable, look for a free adjacent location.
            Location newLocation = findFood();
            if (newLocation == null) {
                newLocation = getField().getFreeAdjacentLocation(getLocation());
            }

            // Check if the disease has lasted long enough for the animal to die.
            if (disease >= DISEASE_DURATION) {
                setDead();
                field.replaceDeadAnimal(getLocation());
            }
            // Move the animal if a new location is available.
            else if (newLocation != null) {
                setLocation(newLocation);
            }
            // If no movement is possible, the animal dies due to overcrowding.
            else {
                setDead();
                field.replaceDeadAnimal(getLocation());
            }
        }
    }

    /**
    * Searches for food within adjacent locations.
    * Must be implemented by subclasses to define species-specific behavior (e.g., hunting, grazing).
    *
    * @return The location of found food, or null if no food is available.
    */
    protected abstract Location findFood();

    /**
     * Creates and returns a new baby animal of the same species.
     * Must be implemented by subclasses to handle species-specific reproduction.
     *
     * @param field: The field where the baby animal will be placed.
     * @param location: The initial location of the baby.
     * @param colour: The colour of the baby animal.
     * @param gene The genetic information passed to the baby.
     * @return A new instance of the baby animal.
     */
    protected abstract Animal createBaby(Field field, Location location, Color colour, String gene);

    /**
    * Retrieves the visual representation of the animal as an icon (emoji).
    * Must be implemented by subclasses to provide a species-specific icon.
    *
    * @return A string representing the animal's icon.
    */
    public abstract String getIcon();

    /**
    * Determines if the animal should give birth, given a suitable mate and available space.
    * Newborn animals are placed in adjacent free locations.
    *
    * @param newAnimals: A list to store newly created baby animals.
    */
    protected void giveBirth(List<Animal> newAnimals) {
        // Get a possible partner i.e. same animal and opposite sex.
        Animal successfulMate = findMatingPartner();
        if(successfulMate != null) {
            // Gets the shared gene for the children.
            String partnersGene = successfulMate.genetics.getGene();
            String sharedGene = genetics.getSharedGene(partnersGene, this.genetics.getGene());

            // Get a list of adjacent free locations.
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = breed();
            for (int b = 0; b < births && !free.isEmpty(); b++) {
                Location loc = free.removeFirst();
                field.removePlant(loc);
                String childGene = genetics.addMutations(sharedGene);
                Animal baby = createBaby(field, loc, getColour(), childGene);
                newAnimals.add(baby);
            }
        }
    }

    /**
    * Checks whether the animal is still alive.
    *
    * @return true if the animal is alive, false otherwise.
    */
    public boolean isAlive() {
        return isAlive;
    }

    /**
    * Marks the animal as dead, removing it from the field.
    * If the animal has a valid location, it is also removed from the simulation.
    */
    protected void setDead() {
        isAlive = false;
        if (location != null) {
            field.removeAnimal(location);
        }
    }

    /**
    * Retrieves the animal's current location.
    *
    * @return The location of the animal.
    */
    public Location getLocation() {
        return location;
    }

    /**
    * Updates the animal's location to a new position within the field.
    * If the animal had a previous location, it is removed from that location first.
    *
    * @param newLocation: The new location for the animal.
    */
    protected void setLocation(Location newLocation) {
        if(location != null) {
            field.removeAnimal(location);
        }
        location = newLocation;
        field.placeAnimal(this, newLocation);
    }

    /**
    * Retrieves the field in which the animal resides.
    *
    * @return The field containing the animal.
    */
    protected Field getField() {
        return field;
    }

    /**
    * Retrieves the colour of the animal.
    *
    * @return The colour of the animal.
    */
    public Color getColour() {
        return colour;
    }

    /**
    * Retrieves the gender of the animal.
    *
    * @return true if the animal is male, false if female.
    */
    protected boolean getGender() {
        return isMale;
    }

    /**
    * Sets the gender of the animal.
    *
    * @param isMale: true for male, false for female.
    */
    protected void setGender(boolean isMale) {
        this.isMale = isMale;
    }

    /**
     * Finds a suitable mating partner (same species, opposite gender).
     *
     * @return A matching animal partner if available, otherwise null.
     */
    protected Animal findMatingPartner() {
        // Finds all living animal around this animal
        List<Animal> nextByAnimals = field.getLivingNeighbours(getLocation());
        // Filters this list by if they're the same animal and different sex.
        List<Animal> possibleMates = nextByAnimals.stream().filter(
                (animal) -> animal.getClass() == this.getClass() && animal.getGender() != this.getGender()
        ).toList();
        if(possibleMates.isEmpty()) {
            return null;
        }
        return possibleMates.getFirst();
    }

    /**
    * Determines the number of births if the animal can breed.
    * The breeding probability is genetically determined.
    *
    * @return The number of offspring (can be zero).
    */
    protected int breed() {
        return age >= this.genetics.getBreedingAge() && rand.nextDouble() <= this.genetics.getBreedingProbability()
                ? rand.nextInt(this.genetics.getMaxLitterSize()) + 1: 0;
    }

    /**
    * Sets the age of the animal.
    *
    * @param age: The new age of the animal.
    */
    protected void setAge(int age) {
        this.age = age;
    }

    /**
    * Retrieves the current age of the animal.
    *
    * @return The animal's age.
    */
    protected int getAge() {
        return age;
    }

    /**
    * Determines if the animal is young (below breeding age).
    *
    * @return true if the animal is young, false otherwise.
    */
    protected boolean isYoung() {
        return age < this.genetics.getBreedingAge();
    }

    /**
    * Increases the animal's age by one step.
    * If the age exceeds the maximum lifespan, the animal dies.
    */
    protected void incrementAge() {
        age++;
        if(age > this.genetics.getMaxAge()) {
            setDead();
            field.replaceDeadAnimal(getLocation());    //Replaces dead animal with a plant
        }
    }

    /**
    * Sets the animal's food level to a specified value.
    *
    * @param foodLevel: The new food level.
    */
    protected void setFoodLevel(double foodLevel) {
        this.foodLevel = foodLevel;
    }

    /**
    * Retrieves the animal's current food level.
    *
    * @return The food level of the animal.
    */
    protected double getFoodLevel() {
        return foodLevel;
    }

    /**
    * Decreases the animal's food level by its metabolic rate.
    * If the food level drops to zero, the animal dies.
    */
    protected void incrementHunger() {
        foodLevel -= this.genetics.getMetabolism();
        if(foodLevel <= 0) {
            setDead();
            field.replaceDeadAnimal(getLocation());    //Replaces dead animal with a plant
        }
    }

    /**
    * Checks if the animal is currently diseased.
    *
    * @return true if the animal is diseased, false otherwise.
    */
    protected boolean isDiseased() {
        return hasDisease;
    }

    /**
    * Sets the disease status of the animal.
    *
    * @param disease: true if the animal is diseased, false otherwise.
    */
    protected void setDisease(boolean disease) {
        hasDisease = disease;
    }

    /**
    * Increases the duration of the animal's disease.
    * If the disease lasts longer than a threshold, the animal dies.
    */
    protected void incrementDisease() {
        disease++;
    }

    /**
    * Attempts to infect the animal if diseased animals are nearby.
    * The probability of infection is determined by genetics.
    */
    private void getDisease() {
        if(!isInfectedAnimals()) {
            return;
        }
        if(rand.nextDouble() <= this.genetics.getDiseaseProbability()) {
            setDisease(true);
        }
    }

    /**
    * Checks if there are infected animals of the same species nearby.
    *
    * @return true if there are infected animals nearby, false otherwise.
    */
    private boolean isInfectedAnimals() {
         // Finds all living animal around this animal
        List<Animal> nextByAnimals = field.getLivingNeighbours(getLocation());
        // Filters this list by if they're the same animal and infected.
        List<Animal> infectedAnimal = nextByAnimals.stream().filter(
                (animal) -> animal.getClass() == this.getClass() && animal.isDiseased()
        ).toList();
        return !infectedAnimal.isEmpty();
    }

}
