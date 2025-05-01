package com.tomtrotter.habitatsimulation.core.domain;

import com.tomtrotter.habitatsimulation.simulation.environment.Field;
import com.tomtrotter.habitatsimulation.simulation.environment.Location;
import com.tomtrotter.habitatsimulation.simulation.genetics.core.Genetics;
import com.tomtrotter.habitatsimulation.simulation.state.SimulatorState;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
* This test class tests a concrete animal implementation that is both a Predator and Prey
* to verify the full animal lifecycle including:
* - Hunting behavior
* - Grazing behavior
* - Reproduction
* - Disease effects
* - Aging and death
*/
class ConcreteAnimalTest {

    private TestField field;
    private TestLocation location;
    private TestGenetics genetics;
    private Omnivore omnivore;
    private TestRandom testRandom;

    /**
    * Set up the test environment before each test.
    * This includes initializing test doubles, setting simulator state values using reflection,
    * and creating the Omnivore test subject.
    */
    @BeforeEach
    public void setUp() {
        field = new TestField();
        location = new TestLocation(0, 0);
        genetics = new TestGenetics();
        testRandom = new TestRandom();

        try {
            SimulatorState simulatorState = SimulatorState.getInstance();

            setPrivateField(simulatorState, "preyFoodValue", 50);
            setPrivateField(simulatorState, "plantFoodValue", 20);
            setPrivateField(simulatorState, "duration", 7);
            setPrivateField(simulatorState, "mortalityRate", 0.3);
        } catch (Exception e) {
            fail("Could not set up SimulatorState: " + e.getMessage());
        }

        omnivore = new Omnivore(field, location, Color.ORANGE, genetics);
        omnivore.setTestRandom(testRandom);
    }

    /**
    * Utility method to set private fields using reflection.
    */
    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    /**
    * Test that the omnivore correctly prefers prey to plants when both are available.
    */
    @Test
    public void testFindFoodPrefersPrey() {
        TestLocation preyLocation = new TestLocation(1, 0);
        TestLocation plantLocation = new TestLocation(0, 1);

        TestPrey prey = new TestPrey(field, preyLocation, Color.GREEN, genetics);
        Plant plant = new Plant(field, plantLocation);

        List<Location> adjacent = new ArrayList<>();
        adjacent.add(preyLocation);
        adjacent.add(plantLocation);
        field.setAdjacentLocations(adjacent);

        field.addOrganism(preyLocation, prey);
        field.placeOrganism(plant, plantLocation);

        Location foundLocation = omnivore.findFood();

        assertEquals(preyLocation, foundLocation);
        assertFalse(prey.isAlive());
        assertTrue(plant.isAlive());
        assertEquals(50.0, omnivore.getFoodLevel());
    }

    /**
    * Test that the omnivore reproduces correctly when conditions are met (age, gender, mate present).
    */
    @Test
    public void testGiveBirth() {
        omnivore.setAge(10);
        omnivore.setGender(false);

        Omnivore malePartner = new Omnivore(field, location, Color.ORANGE, genetics);
        malePartner.setGender(true);

        List<Animal> neighbors = new ArrayList<>();
        neighbors.add(malePartner);
        field.setLivingNeighbours(neighbors);

        TestLocation babyLocation = new TestLocation(1, 1);
        List<Location> freeLocations = new ArrayList<>();
        freeLocations.add(babyLocation);
        field.setFreeAdjacentLocations(freeLocations);

        testRandom.setNextDoubleValue(0.1);
        testRandom.setNextIntValue(1);

        List<Animal> newAnimals = new ArrayList<>();
        omnivore.giveBirth(newAnimals);

        assertEquals(1, newAnimals.size());
        assertInstanceOf(Omnivore.class, newAnimals.getFirst());
    }

    /**
    * Test the creation of a baby omnivore and its initial properties.
    */
    @Test
    public void testCreateBaby() {
        TestLocation babyLocation = new TestLocation(1, 1);

        Animal baby = omnivore.createBaby(field, babyLocation, Color.ORANGE, genetics);

        assertNotNull(baby);
        assertInstanceOf(Omnivore.class, baby);
        assertEquals(field, baby.getField());
        assertEquals(babyLocation, baby.getLocation());
        assertEquals(Color.ORANGE, baby.getColour());
    }

    /**
    * Test that an infected omnivore dies after the disease duration passes and mortality probability is met.
    */
    @Test
    public void testActDieFromDisease() {
        omnivore.disease.setInfected(true);
        for (int i = 0; i < 7; i++) {
            omnivore.disease.incrementInfected();
        }

        testRandom.setNextDoubleValue(0.1);

        TestLocation freeLocation = new TestLocation(1, 1);
        field.setFreeAdjacentLocation(freeLocation);

        List<Animal> newAnimals = new ArrayList<>();
        omnivore.act(newAnimals);

        assertFalse(omnivore.isAlive());
        assertTrue(field.isDeadAnimalReplaced());
    }

    /**
    * Test that an infected omnivore recovers after disease duration if it survives mortality check.
    */
    @Test
    public void testActRecoverFromDisease() {
        omnivore.disease.setInfected(true);
        for (int i = 0; i < 7; i++) {
            omnivore.disease.incrementInfected();
        }

        testRandom.setNextDoubleValue(0.8);

        TestLocation freeLocation = new TestLocation(1, 1);
        field.setFreeAdjacentLocation(freeLocation);

        List<Animal> newAnimals = new ArrayList<>();
        omnivore.act(newAnimals);

        assertTrue(omnivore.isAlive());
        assertFalse(omnivore.disease.isInfected());
    }

    /**
    * Concrete test animal that implements both Predator and Prey behaviors.
    * Used to simulate complex omnivorous behavior.
    */
    private static class Omnivore extends Animal implements Predator, Prey {
        private TestRandom testRandom;

        public Omnivore(Field field, Location location, Color colour, Genetics genetics) {
            super(field, location, colour, genetics);
            setFoodLevel(20.0);
        }

        /**
        * Inject test random object for deterministic behavior in tests.
        */
        public void setTestRandom(TestRandom testRandom) {
            this.testRandom = testRandom;
            this.rand = testRandom;
        }

        /**
        * Chooses food source by prioritizing prey first, then plants.
        */
        @Override
        protected Location findFood() {
            List<Class<? extends Animal>> preyTypes = new ArrayList<>();
            preyTypes.add(TestPrey.class);

            Location foodLocation = hunt(this, preyTypes);

            if (foodLocation == null) {
                foodLocation = graze(this);
            }

            return foodLocation;
        }

        /**
        * Factory method for creating a baby omnivore, preserving test random injection.
        */
        @Override
        protected Animal createBaby(Field field, Location location, Color colour, Genetics genetics) {
            Omnivore baby = new Omnivore(field, location, colour, genetics);
            if (testRandom != null) {
                baby.setTestRandom(testRandom);
            }
            return baby;
        }

        @Override
        public String getIcon() {
            return "🦝";
        }
    }

    /**
    * Minimal test subclass of Animal used as prey target.
    */
    private static class TestPrey extends Animal {
        public TestPrey(Field field, Location location, Color colour, Genetics genetics) {
            super(field, location, colour, genetics);
        }

        @Override
        protected Location findFood() {
            return null;
        }

        @Override
        protected Animal createBaby(Field field, Location location, Color colour, Genetics genetics) {
            return new TestPrey(field, location, colour, genetics);
        }

        @Override
        public String getIcon() {
            return "🐁";
        }
    }

    /**
     * Test double of the Field class allowing controlled responses for testing.
     */
    private static class TestField extends Field {
        private List<Location> adjacentLocations = new ArrayList<>();
        private List<Location> freeAdjacentLocations = new ArrayList<>();
        private Location freeAdjacentLocation;
        private List<Animal> livingNeighbours = new ArrayList<>();
        private final java.util.Map<Location, Animal> organisms = new java.util.HashMap<>();
        private boolean deadAnimalReplaced = false;

        public TestField() {
            super(10, 10);
        }

        public void setAdjacentLocations(List<Location> locations) {
            this.adjacentLocations = locations;
        }

        public void setFreeAdjacentLocations(List<Location> locations) {
            this.freeAdjacentLocations = locations;
        }

        public void setFreeAdjacentLocation(Location location) {
            this.freeAdjacentLocation = location;
        }

        public void setLivingNeighbours(List<Animal> animals) {
            this.livingNeighbours = animals;
        }

        public void addOrganism(Location location, Animal animal) {
            organisms.put(location, animal);
        }

        public boolean isDeadAnimalReplaced() {
            return deadAnimalReplaced;
        }

        @Override
        public Animal getOrganismAt(Location location) {
            return organisms.get(location);
        }

        @Override
        public List<Location> adjacentLocations(Location location) {
            return adjacentLocations;
        }

        @Override
        public List<Location> getFreeAdjacentLocations(Location location) {
            return freeAdjacentLocations;
        }

        @Override
        public Location getFreeAdjacentLocation(Location location) {
            return freeAdjacentLocation;
        }

        @Override
        public List<Animal> getLivingNeighbours(Location location) {
            return livingNeighbours;
        }

        @Override
        public void replaceDeadAnimal(Location location) {
            deadAnimalReplaced = true;
        }

        @Override
        public void clear() {}

        @Override
        public int getWidth() { return 10; }

        @Override
        public int getHeight() { return 10; }
    }

    /**
    * Custom test version of Location that overrides equals and hashCode for reliable comparisons.
    */
    private static class TestLocation extends Location {

        public TestLocation(int row, int col) {
            super(row, col);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Location other)) return false;
            return getRow() == other.getRow() && getCol() == other.getCol();
        }

        @Override
        public int hashCode() {
            return (getRow() << 16) + getCol();
        }
    }

    /**
    * Test double for Genetics with fixed values to allow controlled test conditions.
    */
    private static class TestGenetics extends Genetics {
        @Override
        public int getBreedingAge() {
            return 5;
        }

        @Override
        public int getMaxAge() {
            return 50;
        }

        @Override
        public double getBreedingProbability() {
            return 0.2;
        }

        @Override
        public int getMaxLitterSize() {
            return 4;
        }

        @Override
        public double getMetabolism() {
            return 1.0;
        }

        @Override
        public double getDiseaseProbability() {
            return 0.1;
        }

        @Override
        public Genetics copy() {
            return this;
        }

        @Override
        public Genetics mutate() {
            return this;
        }
    }

    /**
    * Test double for Random, allowing test code to deterministically control
    * random values such as breeding chance and litter size.
    */
    private static class TestRandom extends Random {
        private double nextDoubleValue;
        private int nextIntValue;

        public void setNextDoubleValue(double value) {
            this.nextDoubleValue = value;
        }

        public void setNextIntValue(int value) {
            this.nextIntValue = value;
        }

        @Override
        public double nextDouble() {
            return nextDoubleValue;
        }

        @Override
        public int nextInt(int bound) {
            return nextIntValue;
        }
    }

}