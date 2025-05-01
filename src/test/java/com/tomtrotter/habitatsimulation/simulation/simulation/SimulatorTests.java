package com.tomtrotter.habitatsimulation.simulation.simulation;

import com.tomtrotter.habitatsimulation.core.domain.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
* Unit tests for the Simulator class.
* These tests validate the initialization, population, step simulation, and reset functionality.
*/
class SimulatorTests {

    private Simulator simulator;
    private static final int TEST_HEIGHT = 10;
    private static final int TEST_WIDTH = 10;

    /**
    * Sets up a new Simulator instance before each test.
    * Ensures that each test starts with a fresh simulation environment.
    */
    @BeforeEach
    public void setUp() {
        simulator = new Simulator(TEST_HEIGHT, TEST_WIDTH);
    }

    /**
    * Tests whether the simulator initializes correctly.
    * Verifies that the field is created and the initial step count is zero.
    */
    @Test
    public void testSimulatorInitialization() {
        assertNotNull(simulator.getField(), "Field should be initialized.");
        assertEquals(0, simulator.getStep(), "Initial step should be 0.");
    }

    /**
    * Tests that the field is populated with animals when the simulation starts.
    * Ensures that after initialization, there are animals present in the simulation.
    */
    @Test
    public void testPopulateField() {
        List<Animal> animalsBefore = simulator.getAnimals();
        assertFalse(animalsBefore.isEmpty(), "Field should be populated with animals after initialization.");
    }

    /**
    * Tests whether calling simulateOneStep increments the step count.
    * Ensures that the simulation progresses by one step when simulated.
    */
    @Test
    public void testSimulateOneStepIncrementsStep() {
        int initialStep = simulator.getStep();
        simulator.simulateOneStep();
        assertEquals(initialStep + 1, simulator.getStep(), "Step count should increase by one after simulation step.");
    }

    /**
    * Tests if animals act during a simulation step.
    * Ensures that after one simulation step, animals either remain or reproduce.
    */
    @Test
    public void testSimulateOneStepAnimalsAct() {
        List<Animal> animalsBefore = simulator.getAnimals();
        simulator.simulateOneStep();
        List<Animal> animalsAfter = simulator.getAnimals();

        assertTrue(animalsAfter.size() >= animalsBefore.size(),
                "Animals should act and possibly reproduce after one step.");
    }

    /**
    * Tests whether the reset method correctly resets the simulation.
    * Ensures that the step count is reset to 0 and the field is repopulated.
    */
    @Test
    public void testResetResetsSimulation() {
        simulator.simulateOneStep();
        simulator.reset();
        assertEquals(0, simulator.getStep(), "Step count should reset to 0.");
        assertFalse(simulator.getAnimals().isEmpty(), "Field should be repopulated after reset.");
    }

}

