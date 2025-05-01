package com.tomtrotter.habitatsimulation.core.domain;

import com.tomtrotter.habitatsimulation.simulation.environment.Field;
import com.tomtrotter.habitatsimulation.simulation.environment.Location;
import com.tomtrotter.habitatsimulation.simulation.genetics.core.Genetics;
import com.tomtrotter.habitatsimulation.simulation.state.SimulatorState;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
* Unit tests for the Predator behavior in a simulated environment.
* This class uses mock implementations of core dependencies to test hunting logic
* in isolation, including prey detection, prioritization, and environment interaction.
*/
public class PredatorTest {

    private TestField field;
    private TestLocation preyLocation;
    private TestGenetics genetics;
    private SimulatorState originalState;

    private TestPredator predator;
    private TestPrey prey;

    /**
    * Sets up a test environment with custom SimulatorState, Field,
    * and predator/prey instances before each test.
    */
    @BeforeEach
    public void setUp() throws Exception {
        originalState = SimulatorState.getInstance();

        Constructor<SimulatorState> constructor = SimulatorState.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        SimulatorState testState = constructor.newInstance();

        testState.setPreyFoodValue(50);

        java.lang.reflect.Field instance = SimulatorState.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, testState);

        field = new TestField(5, 5);
        TestLocation location = new TestLocation(1, 1);
        preyLocation = new TestLocation(1, 2);
        genetics = new TestGenetics();

        predator = new TestPredator(field, location, Color.RED, genetics);
        prey = new TestPrey(field, preyLocation, Color.GREEN, genetics);

        field.placeOrganism(predator, location);
        field.placeOrganism(prey, preyLocation);

        List<Location> adjacent = new ArrayList<>();
        adjacent.add(preyLocation);
        field.setAdjacentLocations(location, adjacent);
    }

    /**
    * Restores the original SimulatorState after each test.
    */
    @AfterEach
    public void tearDown() throws Exception {
        java.lang.reflect.Field instance = SimulatorState.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, originalState);
    }

    /**
    * Tests hunting success when a matching prey is adjacent.
    */
    @Test
    public void testHuntPreyFound() {
        List<Class<? extends Animal>> preyTypes = new ArrayList<>();
        preyTypes.add(TestPrey.class);

        Location foundLocation = predator.hunt(predator, preyTypes);

        assertEquals(preyLocation, foundLocation);
        assertFalse(prey.isAlive());
        assertEquals(50.0, predator.getFoodLevel());
    }

    /**
    * Tests hunting behavior when no prey is found (null organism).
    */
    @Test
    public void testHuntNoPreyFound() {
        field.placeOrganism((Animal) null, preyLocation);

        List<Class<? extends Animal>> preyTypes = new ArrayList<>();
        preyTypes.add(TestPrey.class);

        Location foundLocation = predator.hunt(predator, preyTypes);

        assertNull(foundLocation);
    }

    /**
    * Tests behavior when prey is present but not listed as a valid prey type.
    */
    @Test
    public void testHuntPreyNotInHuntList() {
        List<Class<? extends Animal>> preyTypes = new ArrayList<>();

        Location foundLocation = predator.hunt(predator, preyTypes);

        assertNull(foundLocation);
        assertTrue(prey.isAlive());
    }

    /**
    * Tests that predators ignore already-dead prey.
    */
    @Test
    public void testHuntPreyAlreadyDead() {
        prey.setDead();

        List<Class<? extends Animal>> preyTypes = new ArrayList<>();
        preyTypes.add(TestPrey.class);

        Location foundLocation = predator.hunt(predator, preyTypes);

        assertNull(foundLocation);
    }

    /**
    * Tests prioritization of multiple prey types during hunting.
    * Ensures the predator selects the first available valid prey type.
    */
    @Test
    public void testHuntMultiplePreyTypes() {
        TestAnotherPrey anotherPrey = new TestAnotherPrey(field, preyLocation, Color.YELLOW, genetics);
        field.placeOrganism(anotherPrey, preyLocation);

        List<Class<? extends Animal>> preyTypes = new ArrayList<>();
        preyTypes.add(TestPrey.class);
        preyTypes.add(TestAnotherPrey.class);

        Location foundLocation = predator.hunt(predator, preyTypes);

        assertEquals(preyLocation, foundLocation);
        assertFalse(anotherPrey.isAlive());
    }

    /**
    * Mock predator that implements hunting logic with minimal simulation behavior.
    */
    private static class TestPredator extends Animal implements Predator {
        public TestPredator(Field field, Location location, Color colour, Genetics genetics) {
            super(field, location, colour, genetics);
        }

        @Override
        protected Location findFood() {
            return null;
        }

        @Override
        protected Animal createBaby(Field field, Location location, Color colour, Genetics genetics) {
            return new TestPredator(field, location, colour, genetics);
        }

        @Override
        public String getIcon() {
            return "🦁";
        }
    }

    /**
    * Mock prey used in hunt tests.
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
            return "🐇";
        }
    }

    /**
    * Alternate prey species used for prioritization testing.
    */
    private static class TestAnotherPrey extends Animal {
        public TestAnotherPrey(Field field, Location location, Color colour, Genetics genetics) {
            super(field, location, colour, genetics);
        }

        @Override
        protected Location findFood() {
            return null;
        }

        @Override
        protected Animal createBaby(Field field, Location location, Color colour, Genetics genetics) {
            return new TestAnotherPrey(field, location, colour, genetics);
        }

        @Override
        public String getIcon() {
            return "🐿️";
        }
    }

    /**
     * Custom test field that allows explicit control of adjacency and neighbors.
     */
    private static class TestField extends Field {

        private final java.util.Map<Location, List<Location>> adjacentLocationsMap = new java.util.HashMap<>();

        public TestField(int width, int height) {
            super(height, width);
        }

        public void setAdjacentLocations(Location loc, List<Location> adjacentLocations) {
            adjacentLocationsMap.put(loc, adjacentLocations);
        }

        @Override
        public void clear() {
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    removeOrganism(new Location(x, y));
                }
            }
        }

        @Override
        public List<Location> getFreeAdjacentLocations(Location location) {
            List<Location> free = new ArrayList<>();
            for (Location adjacent : adjacentLocations(location)) {
                if (getOrganismAt(adjacent) == null) {
                    free.add(adjacent);
                }
            }
            return free;
        }

        @Override
        public Location getFreeAdjacentLocation(Location location) {
            List<Location> free = getFreeAdjacentLocations(location);
            if (free.isEmpty()) {
                return null;
            }
            return free.getFirst();
        }

        @Override
        public List<Location> adjacentLocations(Location location) {
            List<Location> locations = adjacentLocationsMap.get(location);
            if (locations == null) {
                // Default adjacent locations if not specified
                locations = new ArrayList<>();
                locations.add(new TestLocation(location.getRow(), location.getCol() + 1)); // Above
            }
            return locations;
        }

        @Override
        public List<Animal> getLivingNeighbours(Location location) {
            List<Animal> list = new ArrayList<>();
            for (Location adjacent : adjacentLocations(location)) {
                Organism organism = getOrganismAt(adjacent);
                if (organism instanceof Animal && (organism).isAlive()) {
                    list.add((Animal) organism);
                }
            }
            return list;
        }

    }

    /**
    * Custom location class with overridden equality and hashing for test map support.
    */
    private static class TestLocation extends Location {

        public TestLocation(int row, int col) {
            super(row, col);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Location other) {
                return getRow() == other.getRow() && getCol() == other.getCol();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (getRow() << 16) + getCol();
        }
    }

    /**
    * Mock genetics providing fixed values for test consistency.
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

}