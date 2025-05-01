package com.tomtrotter.habitatsimulation.core.domain;

import com.tomtrotter.habitatsimulation.simulation.environment.Field;
import com.tomtrotter.habitatsimulation.simulation.environment.Location;
import com.tomtrotter.habitatsimulation.simulation.genetics.core.Genetics;
import com.tomtrotter.habitatsimulation.simulation.state.SimulatorState;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
* Unit tests for the Prey interface and its grazing behavior.
* This class uses a custom simulation environment to isolate and verify
* specific behaviors related to how a prey animal interacts with its surroundings.
*/
class PreyTest {

    private TestField field;
    private TestLocation location;
    private TestLocation plantLocation;
    private TestGenetics genetics;
    private SimulatorState originalState;

    private TestPrey prey;
    private Plant plant;

    /**
    * Sets up a test environment before each test case runs.
    * This includes injecting a custom SimulatorState and placing
    * test organisms (prey and plant) into a controlled field.
    */
    @BeforeEach
    public void setUp() throws Exception {
        originalState = SimulatorState.getInstance();

        Constructor<SimulatorState> constructor = SimulatorState.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        SimulatorState testState = constructor.newInstance();
        testState.setPlantFoodValue(20);

        java.lang.reflect.Field instanceField = SimulatorState.class.getDeclaredField("INSTANCE");
        instanceField.setAccessible(true);
        instanceField.set(null, testState);

        // Set up test field and objects
        field = new TestField(5, 5);
        location = new TestLocation(1, 1);
        plantLocation = new TestLocation(1, 2);
        genetics = new TestGenetics();

        prey = new TestPrey(field, location, Color.BROWN, genetics);
        plant = new Plant(field, plantLocation);

        field.placeOrganism(plant, plantLocation);
    }

    /**
    * Restores the original SimulatorState after each test to ensure isolation between tests.
    */
    @AfterEach
    public void tearDown() throws Exception {
        java.lang.reflect.Field instanceField = SimulatorState.class.getDeclaredField("INSTANCE");
        instanceField.setAccessible(true);
        instanceField.set(null, originalState);
    }

    /**
    * Tests the grazing behavior when a plant is found in an adjacent location.
    * The prey should eat the plant, increase its food level, and the plant should die.
    */
    @Test
    public void testGrazePlantFound() {
        Location result = prey.graze(prey);

        assertEquals(plantLocation, result);
        assertFalse(plant.isAlive());
        assertEquals(20.0, prey.getFoodLevel());
    }

    /**
    * Tests grazing behavior when no plant is present at the expected location.
    * The result should be null, indicating no food was found.
    */
    @Test
    public void testGrazeNoPlantFound() {
        field.placeOrganism((Animal) null, plantLocation);

        Location result = prey.graze(prey);

        assertNull(result);
    }

    /**
    * Verifies that grazing fails when a non-plant organism is at the target location.
    * The prey should not attempt to eat other animals.
    */
    @Test
    public void testGrazeNonPlantFound() {
        TestPrey otherPrey = new TestPrey(field, plantLocation, Color.GRAY, genetics);
        field.placeOrganism(otherPrey, plantLocation);

        Location result = prey.graze(prey);

        assertNull(result);
    }

    /**
    * Tests grazing logic when multiple adjacent locations exist,
    * with the plant not being in the first checked location.
    */
    @Test
    public void testGrazeMultipleLocations() {
        TestLocation emptyLocation = new TestLocation(1, 3);
        field.placeOrganism((Animal) null, emptyLocation);

        List<Location> adjacent = new ArrayList<>();
        adjacent.add(emptyLocation);
        adjacent.add(plantLocation);
        field.setAdjacentLocations(location, adjacent);

        Location result = prey.graze(prey);

        assertEquals(plantLocation, result);
    }

    /**
    * A test implementation of a prey animal that overrides necessary behavior
    * for isolated testing of grazing and reproduction.
    */
    private static class TestPrey extends Animal implements Prey {
        public TestPrey(Field field, Location location, Color colour, Genetics genetics) {
            super(field, location, colour, genetics);
        }

        @Override
        protected Location findFood() {
            return graze(this);
        }

        @Override
        protected Animal createBaby(Field field, Location location, Color colour, Genetics genetics) {
            return new TestPrey(field, location, colour, genetics);
        }

        @Override
        public String getIcon() {
            return "🐰";
        }
    }

    /**
    * A mock field implementation with the ability to customize adjacent locations.
    * Useful for simulating specific field conditions.
    */
    private static class TestField extends Field {
        private final java.util.Map<Location, List<Location>> adjacentLocationsMap = new java.util.HashMap<>();

        public TestField(int width, int height) {
            super(height, width);
        }

        public void setAdjacentLocations(Location loc, List<Location> adjacent) {
            adjacentLocationsMap.put(loc, adjacent);
        }

        @Override
        public void clear() {
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    removeOrganism(new Location(y, x));
                }
            }
        }

        @Override
        public List<Location> getFreeAdjacentLocations(Location location) {
            List<Location> free = new ArrayList<>();
            for (Location loc : adjacentLocations(location)) {
                if (getOrganismAt(loc) == null) {
                    free.add(loc);
                }
            }
            return free;
        }

        @Override
        public Location getFreeAdjacentLocation(Location location) {
            List<Location> free = getFreeAdjacentLocations(location);
            return free.isEmpty() ? null : free.getFirst();
        }

        @Override
        public List<Location> adjacentLocations(Location location) {
            return adjacentLocationsMap.getOrDefault(location, List.of(new TestLocation(location.getRow(), location.getCol() + 1)));
        }

        @Override
        public List<Animal> getLivingNeighbours(Location location) {
            List<Animal> living = new ArrayList<>();
            for (Location loc : adjacentLocations(location)) {
                Organism org = getOrganismAt(loc);
                if (org instanceof Animal && org.isAlive()) {
                    living.add((Animal) org);
                }
            }
            return living;
        }
    }

    /**
    * A simple test extension of Location with overridden equals and hashCode methods.
    * This ensures test accuracy when comparing or storing locations.
    */
    private static class TestLocation extends Location {
        public TestLocation(int row, int col) {
            super(row, col);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Location other &&
                    getRow() == other.getRow() &&
                    getCol() == other.getCol();
        }

        @Override
        public int hashCode() {
            return (getRow() << 16) + getCol();
        }
    }

    /**
    * A test genetics implementation that provides fixed genetic values for prey behavior.
    */
    private static class TestGenetics extends Genetics {
        @Override public int getBreedingAge() { return 5; }
        @Override public int getMaxAge() { return 50; }
        @Override public double getBreedingProbability() { return 0.2; }
        @Override public int getMaxLitterSize() { return 4; }
        @Override public double getMetabolism() { return 1.0; }
        @Override public double getDiseaseProbability() { return 0.1; }
        @Override public Genetics copy() { return this; }
        @Override public Genetics mutate() { return this; }
    }

}
