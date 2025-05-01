package com.tomtrotter.habitatsimulation.core.domain;

import com.tomtrotter.habitatsimulation.simulation.environment.Field;
import com.tomtrotter.habitatsimulation.simulation.environment.Location;
import com.tomtrotter.habitatsimulation.simulation.genetics.core.Genetics;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
* Test class for the Animal class.
* This class contains unit tests for different functionalities of the Animal class
* such as initialization, age increment, hunger increment, breeding, and disease infection.
*/
class AnimalTest {

    private TestField field;
    private TestLocation location;
    private TestGenetics genetics;
    private TestAnimal animal;

    /**
    * Set up the test environment before each test.
    * Initializes the required objects like TestField, TestLocation, and TestGenetics.
    */
    @BeforeEach
    public void setUp() {
        field = new TestField();
        location = new TestLocation(0, 0);
        genetics = new TestGenetics();

        genetics.metabolism = 1.0;
        genetics.maxAge = 100;
        genetics.breedingAge = 5;
        genetics.breedingProbability = 0.5;
        genetics.maxLitterSize = 3;
        genetics.diseaseProbability = 0.1;

        animal = new TestAnimal(field, location, Color.RED, genetics);
    }

    /**
    * Test the initialization of an animal.
    * Verifies that the animal is correctly initialized with the provided field, location, color, and genetics.
    */
    @Test
    public void testInitialization() {
        assertNotNull(animal);
        assertEquals(field, animal.getField());
        assertEquals(location, animal.getLocation());
        assertEquals(Color.RED, animal.getColour());
        assertTrue(animal.isAlive());
        assertNotNull(animal.disease);
        assertFalse(animal.disease.isInfected());
    }

    /**
    * Test the incrementing of an animal's age.
    * Verifies that the animal's age is correctly incremented, and it dies when it reaches the max age.
    */
    @Test
    public void testIncrementAge() {
        assertEquals(0, animal.getAge());
        animal.incrementAge();
        assertEquals(1, animal.getAge());

        genetics.maxAge = 1;
        animal.incrementAge();
        assertFalse(animal.isAlive());
        assertTrue(field.replaceDeadAnimalCalled);
        assertEquals(location, field.replaceDeadAnimalLocation);
    }

    /**
    * Test the incrementing of an animal's hunger.
    * Verifies that the animal's food level decreases and that it dies if the food level reaches zero.
    */
    @Test
    public void testIncrementHunger() {
        animal.setFoodLevel(5.0);
        assertEquals(5.0, animal.getFoodLevel());

        animal.incrementHunger();
        assertEquals(4.0, animal.getFoodLevel());

        animal.setFoodLevel(0.5);
        animal.incrementHunger();
        assertFalse(animal.isAlive());
        assertTrue(field.replaceDeadAnimalCalled);
        assertEquals(location, field.replaceDeadAnimalLocation);
    }

    /**
    * Test whether the animal is considered young based on its age.
    * Verifies that the animal is considered young if its age is below the breeding age.
    */
    @Test
    public void testIsYoung() {
        genetics.breedingAge = 10;
        animal.setAge(5);
        assertTrue(animal.isYoung());

        animal.setAge(15);
        assertFalse(animal.isYoung());
    }

    /**
    * Test the gender of the animal.
    * Verifies that the animal's gender is correctly set and returned.
    */
    @Test
    public void testGender() {
        animal.setGender(true);
        assertTrue(animal.getGender());

        animal.setGender(false);
        assertFalse(animal.getGender());
    }

    /**
    * Test the finding of a mating partner.
    * Verifies that the animal can find a mating partner from its neighbors, provided they are of the opposite gender.
    */
    @Test
    public void testFindMatingPartner() {
        field.neighbours.clear();
        assertNull(animal.findMatingPartner());

        TestAnimal mate = new TestAnimal(field, location, Color.RED, genetics);
        mate.setGender(!animal.getGender());

        field.neighbours.add(mate);

        assertEquals(mate, animal.findMatingPartner());
    }

    /**
    * Test the breeding of an animal when below the breeding age.
    * Verifies that an animal that is below the breeding age does not breed.
    */
    @Test
    public void testBreedBelowBreedingAge() {
        genetics.breedingAge = 10;
        animal.setAge(5);

        assertEquals(0, animal.breed());
    }

    /**
    * Test the breeding of an animal when above the breeding age.
    * Verifies that the animal breeds when above the breeding age and the correct number of offspring is created.
    */
    @Test
    public void testBreedAboveBreedingAge() {
        genetics.breedingAge = 5;
        animal.setAge(10);

        TestRandom testRandom = new TestRandom();
        testRandom.nextDoubleValues.add(0.1);
        testRandom.nextIntValue = 2;
        animal.rand = testRandom;

        field.freeAdjacentLocations.add(new TestLocation(1, 1));
        field.freeAdjacentLocations.add(new TestLocation(1, 2));

        assertEquals(3, animal.breed());
    }

    /**
    * Test the behavior of an animal when it is dead.
    * Verifies that a dead animal does not perform any actions such as moving or breeding.
    */
    @Test
    public void testActDead() {
        animal.setDead();
        List<Animal> newAnimals = new ArrayList<>();

        animal.act(newAnimals);

        assertTrue(newAnimals.isEmpty());
        assertFalse(field.getFreeAdjacentLocationCalled);
    }

    /**
    * Test the behavior of an animal when it is alive.
    * Verifies that the animal performs actions such as moving to a new location and possibly breeding.
    */
    @Test
    public void testActAlive() {
        animal.setFoodLevel(10.0);
        List<Animal> newAnimals = new ArrayList<>();

        TestLocation newLocation = new TestLocation(1, 1);
        field.freeAdjacentLocation = newLocation;

        animal.act(newAnimals);

        assertTrue(field.placeOrganismCalled);
        assertEquals(animal, field.placedOrganism);
        assertEquals(newLocation, field.placedLocation);
    }

    /**
    * Test the detection of infected animals.
    * Verifies that an animal can detect infected animals among its neighbors.
    */
    @Test
    public void testIsInfectedAnimals() {
        TestAnimal infectedAnimal = new TestAnimal(field, location, Color.RED, genetics);
        infectedAnimal.disease.setInfected(true);

        field.neighbours.clear();
        field.neighbours.add(infectedAnimal);

        assertTrue(animal.isInfectedAnimals(), "Should detect infected neighbors");
    }

    /**
    * Test the disease infection process.
    * Verifies that an animal can get infected based on its disease probability and the randomness provided by the animal's random instance.
    */
    @Test
    public void testDiseaseGetInfection() {
        assertFalse(animal.disease.isInfected(), "Animal should start not infected");

        TestRandom testRandom = new TestRandom();
        testRandom.nextDoubleValues.add(0.0);
        animal.rand = testRandom;

        double diseaseProbability = 1.0;

        animal.disease.getInfection(diseaseProbability);

        assertTrue(animal.disease.isInfected(), "Animal should be infected after getInfection");
    }

    /**
    * Helper concrete implementation of the abstract Animal class for testing.
    * This implementation overrides abstract methods to make it functional for testing purposes.
    */
    private static class TestAnimal extends Animal {
        public TestAnimal(Field field, Location location, Color colour, Genetics genetics) {
            super(field, location, colour, genetics);
            setFoodLevel(10);
        }

        @Override
        protected Location findFood() {
            return null;
        }

        @Override
        protected Animal createBaby(Field field, Location location, Color colour, Genetics genetics) {
            return new TestAnimal(field, location, colour, genetics);
        }

        @Override
        public String getIcon() {
            return "🐾";
        }
    }

    /**
    * Test implementation of Field.
    * This implementation provides a mock behavior for placing, removing, and replacing organisms in the field.
    */
    private static class TestField extends Field {
        private boolean placeOrganismCalled = false;
        private Organism placedOrganism = null;
        private Location placedLocation = null;

        private boolean replaceDeadAnimalCalled = false;
        private Location replaceDeadAnimalLocation = null;

        private boolean getFreeAdjacentLocationCalled = false;
        private TestLocation freeAdjacentLocation = null;

        private final List<Location> freeAdjacentLocations = new ArrayList<>();
        private final List<Animal> neighbours = new ArrayList<>();

        public TestField() {
            super(5, 5);
        }

        @Override
        public void placeOrganism(Organism organism, Location location) {
            placeOrganismCalled = true;
            placedOrganism = organism;
            placedLocation = location;
        }

        @Override
        public void replaceDeadAnimal(Location location) {
            replaceDeadAnimalCalled = true;
            replaceDeadAnimalLocation = location;
        }

        @Override
        public Location getFreeAdjacentLocation(Location location) {
            getFreeAdjacentLocationCalled = true;
            return freeAdjacentLocation;
        }

        @Override
        public List<Location> getFreeAdjacentLocations(Location location) {
            return new ArrayList<>(freeAdjacentLocations);
        }

        @Override
        public List<Animal> getLivingNeighbours(Location location) {
            return new ArrayList<>(neighbours);
        }
    }

    /**
    * Test implementation of Location.
    * This class represents a specific location within the field, with row and column coordinates.
    */
    private static class TestLocation extends Location {
        public TestLocation(int row, int col) {
            super(row, col);
        }
    }

    /**
    * Test implementation of Genetics.
    * This class provides the genetic parameters for an animal, such as metabolism, age, and disease probability.
    */
    private static class TestGenetics extends Genetics {
        private double metabolism = 1.0;
        private int maxAge = 100;
        private int breedingAge = 5;
        private double breedingProbability = 0.5;
        private int maxLitterSize = 3;
        private double diseaseProbability = 0.1;

        @Override
        public double getMetabolism() {
            return metabolism;
        }

        @Override
        public int getMaxAge() {
            return maxAge;
        }

        @Override
        public int getBreedingAge() {
            return breedingAge;
        }

        @Override
        public double getBreedingProbability() {
            return breedingProbability;
        }

        @Override
        public int getMaxLitterSize() {
            return maxLitterSize;
        }

        @Override
        public double getDiseaseProbability() {
            return diseaseProbability;
        }

        @Override
        public Genetics copy() {
            TestGenetics copy = new TestGenetics();
            copy.metabolism = this.metabolism;
            copy.maxAge = this.maxAge;
            copy.breedingAge = this.breedingAge;
            copy.breedingProbability = this.breedingProbability;
            copy.maxLitterSize = this.maxLitterSize;
            copy.diseaseProbability = this.diseaseProbability;
            return copy;
        }

        @Override
        public Genetics mutate() {
            return copy();
        }
    }

    /**
    * Test implementation of Random.
    * This class simulates randomness for testing, allowing for predictable results by overriding `nextDouble` and `nextInt`.
    */
    private static class TestRandom extends Random {
        private final List<Double> nextDoubleValues = new ArrayList<>();
        private int nextDoubleIndex = 0;
        private int nextIntValue = 0;

        @Override
        public double nextDouble() {
            if (nextDoubleIndex < nextDoubleValues.size()) {
                return nextDoubleValues.get(nextDoubleIndex++);
            }
            return 0.0;
        }

        @Override
        public int nextInt(int bound) {
            return nextIntValue;
        }
    }

}
