package com.tomtrotter.habitatsimulation.core.domain;

import com.tomtrotter.habitatsimulation.simulation.state.SimulatorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
* Unit tests for the Disease class.
* These tests verify the correctness of infection logic, mortality handling,
* and integration with the SimulatorState singleton.
*/
public class DiseaseTest {

    private Disease disease;
    private SimulatorState originalState;
    private SimulatorState testState;

    /**
    * Sets up a fresh Disease instance and a mocked SimulatorState before each test.
    * Uses reflection to override the singleton SimulatorState for controlled testing.
    */
    @BeforeEach
    public void setUp() throws Exception {
        originalState = SimulatorState.getInstance();

        Constructor<SimulatorState> constructor = SimulatorState.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        testState = constructor.newInstance();

        testState.setDuration(7);
        testState.setMortalityRate(0.3);

        Field instance = SimulatorState.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, testState);

        disease = new Disease();
        disease.setRandom(new TestRandom());
    }

    /**
    * Restores the original SimulatorState singleton after each test
    * to prevent state leakage across tests.
    */
    @AfterEach
    public void tearDown() throws Exception {
        Field instance = SimulatorState.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, originalState);
    }

    /**
    * Verifies that a newly created Disease instance is not infected
    * and has zero days infected.
    */
    @Test
    public void testInitialization() {
        assertNotNull(disease);
        assertFalse(disease.isInfected());
        assertEquals(0, disease.getDaysInfected());
    }

    /**
    * Tests if the Disease correctly fetches the infection duration from the SimulatorState.
    */
    @Test
    public void testGetDuration() {
        testState.setDuration(10);
        assertEquals(10, disease.getDuration());
    }

    /**
    * Tests if setting duration via the Disease reflects correctly in the SimulatorState.
    */
    @Test
    public void testSetDuration() {
        disease.setDuration(14);
        assertEquals(14, testState.getDuration());
    }

    /**
    * Verifies the Disease can toggle its infected state.
    */
    @Test
    public void testSetInfected() {
        disease.setInfected(true);
        assertTrue(disease.isInfected());

        disease.setInfected(false);
        assertFalse(disease.isInfected());
    }

    /**
    * Tests that incrementing the infected counter increases the number of infected days.
    */
    @Test
    public void testIncrementInfected() {
        assertEquals(0, disease.getDaysInfected());

        disease.incrementInfected();
        assertEquals(1, disease.getDaysInfected());

        disease.incrementInfected();
        assertEquals(2, disease.getDaysInfected());
    }

    /**
    * Tests that the Disease becomes infected when a random value is below the infection probability.
    */
    @Test
    public void testGetInfectionSuccessful() {
        ((TestRandom)disease.getRandom()).setNextDoubleValue(0.05);
        disease.getInfection(0.1); // Random < probability, should infect
        assertTrue(disease.isInfected());
    }

    /**
    * Tests that the Disease does not become infected when the random value is above the probability.
    */
    @Test
    public void testGetInfectionUnsuccessful() {
        ((TestRandom)disease.getRandom()).setNextDoubleValue(0.2);
        disease.getInfection(0.1); // Random > probability, should not infect
        assertFalse(disease.isInfected());
    }

    /**
    * Verifies that the Disease retrieves the correct mortality rate from SimulatorState.
    */
    @Test
    public void testGetMortalityRate() {
        testState.setMortalityRate(0.5);
        assertEquals(0.5, disease.getMortalityRate());
    }

    /**
    * Tests whether the Disease can set the mortality rate in SimulatorState.
    */
    @Test
    public void testSetMortalityRate() {
        disease.setMortalityRate(0.7);
        assertEquals(0.7, testState.getMortalityRate());
    }

    /**
    * Custom implementation of Random that allows tests to control the output
    * of random values deterministically for reproducible test behavior.
    */
    private static class TestRandom extends Random {
        private double nextDoubleValue = 0.0;

        /**
        * Sets the next value that will be returned by nextDouble().
        */
        public void setNextDoubleValue(double value) {
            this.nextDoubleValue = value;
        }

        @Override
        public double nextDouble() {
            return nextDoubleValue;
        }

        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }

}
