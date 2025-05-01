package com.tomtrotter.habitatsimulation.simulation.genetics;

import com.tomtrotter.habitatsimulation.simulation.genetics.attributes.*;
import com.tomtrotter.habitatsimulation.simulation.genetics.core.Genetics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
* Unit tests for the Genetics class.
* These tests validate attribute management, mutation logic, and inheritance behavior
* in the genetic system used within the habitat simulation.
*/
public class GeneticsTest {

    /**
    * Set up method to initialize the GeneticAttributeManager.
    * Currently, ensures that the singleton manager is available before tests.
    */
    @BeforeEach
    public void setUp() {
        GeneticAttributeManager manager = GeneticAttributeManager.getInstance();
    }

    /**
    * Verifies that a newly created {@link Genetics} instance assigns random values
    * within the defined range for the BREEDING_AGE attribute.
    */
    @Test
    public void testRandomAttributeGeneration() {
        Genetics genetics = new Genetics();
        int breedingAge = genetics.getBreedingAge();

        assertTrue(breedingAge >= 12 && breedingAge <= 90, "Breeding age should be within defined range.");
    }

    /**
    * Tests that setting a valid value for a genetic attribute works correctly.
    * Specifically checks if BREEDING_AGE is updated as expected.
    */
    @Test
    public void testSetAttributeValid() {
        Genetics genetics = new Genetics();
        genetics.setAttribute(Attributes.BREEDING_AGE, 14);
        assertEquals(14, genetics.getBreedingAge());
    }

    /**
    * Ensures that an IllegalArgumentException is thrown when attempting
    * to set a value for an attribute that exceeds its defined bounds.
    */
    @Test
    public void testSetAttributeOutOfBoundsThrows() {
        Genetics genetics = new Genetics();

        assertThrows(IllegalArgumentException.class, () -> {
            genetics.setAttribute(Attributes.BREEDING_AGE, 100);
        });
    }

    /**
    * Confirms that the copy constructor for {@link Genetics} correctly duplicates
    * the attributes from the original instance.
    */
    @Test
    public void testCopyConstructorCopiesValue() {
        Genetics original = new Genetics();
        original.setAttribute(Attributes.BREEDING_AGE, 14);

        Genetics copy = new Genetics(original);

        assertEquals(14, copy.getBreedingAge());
    }

    /**
    * Validates that the {@code breed} method produces offspring with attribute values
    * inherited exactly from one of the two parent instances.
    */
    @Test
    public void testBreedUsesParentValues() {
        Genetics parentA = new Genetics();
        Genetics parentB = new Genetics();

        parentA.setAttribute(Attributes.BREEDING_AGE, 14);
        parentB.setAttribute(Attributes.BREEDING_AGE, 16);

        Genetics offspring = Genetics.breed(parentA, parentB);
        int value = offspring.getBreedingAge();

        assertTrue(value == 14 || value == 16, "Offspring should inherit breeding age from either parent.");
    }

    /**
    * Tests the advanced breeding logic that blends values from both parents
    * using a specified weight ratio.
    */
    @Test
    public void testBreedAdvancedWithBlending() {
        Genetics parentA = new Genetics();
        Genetics parentB = new Genetics();

        parentA.setAttribute(Attributes.BREEDING_AGE, 14);
        parentB.setAttribute(Attributes.BREEDING_AGE, 16);

        Genetics child = Genetics.breedAdvanced(parentA, parentB, 0.75, true);

        int expected = (int) Math.round(14 * 0.75 + 16 * 0.25);
        assertEquals(expected, child.getBreedingAge());
    }

    /**
    * Ensures that mutation has no effect when the mutation probability is effectively zero.
    * This test helps guarantee stability when no mutation should occur.
    */
    @Test
    public void testMutateDoesNotChangeValueWhenProbabilityIsZero() {
        Genetics genetics = new Genetics();
        genetics.setAttribute(Attributes.BREEDING_AGE, 14);

        genetics.mutate();

        assertEquals(14, genetics.getBreedingAge(), "No mutation should occur when probability is 0.");
    }

}
