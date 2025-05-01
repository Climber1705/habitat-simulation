package com.tomtrotter.habitatsimulation.core.domain;

import com.tomtrotter.habitatsimulation.simulation.environment.Field;
import com.tomtrotter.habitatsimulation.simulation.environment.Location;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
* Unit tests for the Plant class.
* These tests verify the correct initialization, state behavior, and environmental
* interactions (via placement and removal in a field) of a plant organism.
*/
public class PlantTest {

    private TestField field;
    private TestLocation location;
    private Plant plant;

    /**
    * Set up a new Plant instance and mock environment before each test.
    */
    @BeforeEach
    public void setUp() {
        field = new TestField();
        location = new TestLocation();
        plant = new Plant(field, location);
    }

    /**
    * Tests that a plant is properly initialized and placed into the field.
    */
    @Test
    public void testInitialization() {
        assertNotNull(plant);
        assertTrue(plant.isAlive());
        assertEquals(field, plant.getField());
        assertEquals(location, plant.getLocation());
        assertEquals(Color.GREEN, plant.getColour());

        assertTrue(field.placeOrganismCalled);
        assertEquals(plant, field.placedOrganism);
        assertEquals(location, field.placedLocation);
    }

    /**
    * Tests that the plant returns the correct icon for display.
    */
    @Test
    public void testGetIcon() {
        assertEquals("🌱", plant.getIcon());
    }

    /**
    * Tests that calling setDead() updates the plant’s state and removes it from the field.
    */
    @Test
    public void testSetDead() {
        assertTrue(plant.isAlive());

        plant.setDead();

        assertFalse(plant.isAlive());
        assertTrue(field.removeOrganismCalled);
        assertEquals(location, field.removedLocation);
    }

    /**
    * A test double for Field that captures calls to placeOrganism() and removeOrganism().
    * Allows verification of interactions without invoking real simulation logic.
    */
    private static class TestField extends Field {
        private boolean placeOrganismCalled = false;
        private Organism placedOrganism = null;
        private Location placedLocation = null;

        private boolean removeOrganismCalled = false;
        private Location removedLocation = null;

        public TestField() {
            super(1, 1);
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
    * A simple test implementation of Location representing position (0,0).
    */
    private static class TestLocation extends Location {
        public TestLocation() {
            super(0, 0);
        }
    }
}