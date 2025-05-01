package com.tomtrotter.habitatsimulation.core.domain;

import com.tomtrotter.habitatsimulation.simulation.environment.Field;
import com.tomtrotter.habitatsimulation.simulation.environment.Location;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
* Unit tests for the Organism class.
* These tests verify organism initialization, location updates,
* and lifecycle (alive/dead) behavior, while using test doubles
* to monitor interactions with the environment (Field and Location).
*/
class OrganismTest {

    private TestField field;
    private TestLocation location;
    private TestLocation newLocation;
    private Organism organism;

    /**
    * Set up a fresh Organism instance and test environment before each test.
    * Uses a mocked Field and Location to track interactions.
    */
    @BeforeEach
    public void setUp() {
        field = new TestField();
        location = new TestLocation(1, 1);
        newLocation = new TestLocation(2, 2);
        organism = new Organism(field, location, Color.BLUE);
    }

    /**
    * Verifies the organism is properly initialized, alive, and placed in the field.
    */
    @Test
    public void testInitialization() {
        assertNotNull(organism);
        assertTrue(organism.isAlive());
        assertEquals(field, organism.getField());
        assertEquals(location, organism.getLocation());
        assertEquals(Color.BLUE, organism.getColour());

        assertTrue(field.placeOrganismCalled);
        assertEquals(organism, field.placedOrganism);
        assertEquals(location, field.placedLocation);
    }

    /**
    * Tests that calling setDead() changes the organism's status to dead
    * and removes it from the field if it had a location.
    */
    @Test
    public void testSetDead() {
        field.resetTrackingFlags();

        assertTrue(organism.isAlive());
        organism.setDead();

        assertFalse(organism.isAlive());
        assertTrue(field.removeOrganismCalled);
        assertEquals(location, field.removedLocation);
    }

    /**
    * Verifies that setDead() does not try to remove the organism
    * from the field when its location is null.
    */
    @Test
    public void testSetDeadWithNullLocation() {
        Organism nullLocationOrganism = new Organism(field, null, Color.RED);

        field.resetTrackingFlags();

        nullLocationOrganism.setDead();

        assertFalse(nullLocationOrganism.isAlive());
        assertFalse(field.removeOrganismCalled);
    }

    /**
    * Tests that setLocation() correctly updates the organism's position,
    * removing it from its old location and placing it in the new one.
    */
    @Test
    public void testSetLocation() {
        field.resetTrackingFlags();

        organism.setLocation(newLocation);

        assertEquals(newLocation, organism.getLocation());
        assertTrue(field.removeOrganismCalled);
        assertEquals(location, field.removedLocation);
        assertTrue(field.placeOrganismCalled);
        assertEquals(organism, field.placedOrganism);
        assertEquals(newLocation, field.placedLocation);
    }

    /**
    * Ensures that setting the location from a null starting point
    * only results in a placement without a removal.
    */
    @Test
    public void testSetLocationFromNullLocation() {
        Organism nullLocationOrganism = new Organism(field, null, Color.RED);

        field.resetTrackingFlags();

        nullLocationOrganism.setLocation(newLocation);

        assertEquals(newLocation, nullLocationOrganism.getLocation());

        assertFalse(field.removeOrganismCalled);
        assertTrue(field.placeOrganismCalled);
        assertEquals(nullLocationOrganism, field.placedOrganism);
        assertEquals(newLocation, field.placedLocation);
    }

    /**
    * Verifies the getField() method returns the correct field reference.
    */
    @Test
    public void testGetField() {
        assertEquals(field, organism.getField());
    }

    /**
    * Ensures that the organism's color is correctly returned.
    */
    @Test
    public void testGetColour() {
        assertEquals(Color.BLUE, organism.getColour());
    }


    /**
    * Test double for Field that tracks method calls
    * without invoking actual environment logic.
    */
    private static class TestField extends Field {
        private boolean placeOrganismCalled = false;
        private Organism placedOrganism = null;
        private Location placedLocation = null;

        private boolean removeOrganismCalled = false;
        private Location removedLocation = null;

        public TestField() {
            super(5, 5);
        }

        /**
        * Resets the tracking flags for a fresh test state.
        */
        public void resetTrackingFlags() {
            placeOrganismCalled = false;
            placedOrganism = null;
            placedLocation = null;
            removeOrganismCalled = false;
            removedLocation = null;
        }

        @Override
        public void placeOrganism(Organism organism, Location location) {
            placeOrganismCalled = true;
            placedOrganism = organism;
            placedLocation = location;
        }

        @Override
        public void removeOrganism(Location location) {
            removeOrganismCalled = true;
            removedLocation = location;
        }
    }

    /**
     * Simple subclass of Location for instantiation in test cases.
     */
    private static class TestLocation extends Location {
        public TestLocation(int row, int col) {
            super(row, col);
        }
    }
}