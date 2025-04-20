package com.tomtrotter.habitatsimulation.model.core;

import java.util.List;

import com.tomtrotter.habitatsimulation.model.environment.Field;
import com.tomtrotter.habitatsimulation.model.environment.Location;
import com.tomtrotter.habitatsimulation.model.genetics.Genetics;
import com.tomtrotter.habitatsimulation.model.util.Randomizer;
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

public abstract class Animal extends Organism {

    private int age;
    private double foodLevel;
    private boolean isMale;

    public final Random rand = Randomizer.getRandom();
    public final Genetics genetics;
    public final Disease disease;

    /**
    * Creates a new animal at a specific location in a given field.
    * The animal is initialized with genetic information and a random gender.
    *
    * @param field The field currently occupied by the animal.
    * @param location The initial location of the animal within the field.
    * @param colour The colour of the animal.
    * @param gene The genetic material of the animal.
    */
    public Animal(Field field, Location location, Color colour, String gene) {
        super(field, location, colour);

        genetics = new Genetics(gene);
        disease = new Disease();

        setLocation(location);
        setGender(rand.nextBoolean());
    }

    /**
    * Defines the actions an animal performs in a simulation step.
    * The animal ages, experiences hunger, attempts to reproduce, and searches for food.
    * It may also contract or suffer from disease. If conditions become fatal, it dies.
    *
    * @param newAnimals A list to store newly created animals (offspring).
    */
    public void act(List<Animal> newAnimals) {
        incrementAge();
        incrementHunger();

        if (!isAlive()){
            return;
        }

        // Reproduce if the animal is female.
        if (!this.getGender()) {
            giveBirth(newAnimals);
        }

        // Handle disease progression or infection.
        if (disease.isInfected()) {
            disease.incrementInfected();
        } else if (isInfectedAnimals()) {
            disease.getInfection(genetics.getDiseaseProbability());
        }

        // Attempt to move to a food source or an adjacent location.
        Location newLocation = findFood();
        if (newLocation == null) {
            newLocation = getField().getFreeAdjacentLocation(getLocation());
        }

        // Handle death or recovery after disease duration has passed.
        if (disease.getDaysInfected() >= disease.getDuration()) {
            if (rand.nextDouble() <= disease.getMortalityRate()) {
                setDead();
                getField().replaceDeadAnimal(getLocation());
                return;
            }
            disease.setInfected(false);
        }

        // Move to a new location if available.
        if (newLocation != null) {
            setLocation(newLocation);
        } else {
            // Die due to overcrowding.
            setDead();
            getField().replaceDeadAnimal(getLocation());
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
     * @param field The field where the baby animal will be placed.
     * @param location The initial location of the baby.
     * @param colour The colour of the baby animal.
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
    * @param newAnimals A list to store newly created baby animals.
    */
    protected void giveBirth(List<Animal> newAnimals) {
        // Get a possible partner, i.e. same animal and opposite sex.
        Animal successfulMate = findMatingPartner();

        if(successfulMate == null) {
            return;
        }

        // Gets the shared gene for the children.
        String partnersGene = successfulMate.genetics.getGene();
        String sharedGene = genetics.getSharedGene(partnersGene, this.genetics.getGene());

        // Get a list of adjacent free locations.
        List<Location> free = getField().getFreeAdjacentLocations(getLocation());
        int births = breed();

        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.removeFirst();
            getField().removeOrganism(loc);
            String childGene = genetics.addMutations(sharedGene);
            Animal baby = createBaby(getField(), loc, getColour(), childGene);
            newAnimals.add(baby);
        }

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
    * @param isMale true for male, false for female.
    */
    public void setGender(boolean isMale) {
        this.isMale = isMale;
    }

    /**
     * Finds a suitable mating partner (same species, opposite gender).
     *
     * @return A matching animal partner if available, otherwise null.
     */
    protected Animal findMatingPartner() {
        // Finds all living animals around this animal
        List<Animal> nextByAnimals = getField().getLivingNeighbours(getLocation());

        // Filters this list by if they're the same animal and a different sex.
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
    * @param age The new age of the animal.
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
            getField().replaceDeadAnimal(getLocation());
        }
    }

    /**
    * Sets the animal's food level to a specified value.
    *
    * @param foodLevel The new food level.
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
    * Decreases the animal's food level due to its metabolic rate.
    * If the food level drops to zero, the animal dies.
    */
    protected void incrementHunger() {
        foodLevel -= this.genetics.getMetabolism();
        if(foodLevel <= 0) {
            setDead();
            getField().replaceDeadAnimal(getLocation());
        }
    }

    /**
     * Checks if there are infected animals of the same species nearby.
     *
     * @return true if there are infected animals nearby, false otherwise.
     */
    private boolean isInfectedAnimals() {
        // Finds all living animals around this animal
        List<Animal> nextByAnimals = getField().getLivingNeighbours(getLocation());
        // Filters this list by if they're the same animal and infected.
        List<Animal> infectedAnimal = nextByAnimals.stream().filter(
                (animal) -> animal.getClass() == this.getClass() && animal.disease.isInfected()
        ).toList();
        return !infectedAnimal.isEmpty();
    }

}
