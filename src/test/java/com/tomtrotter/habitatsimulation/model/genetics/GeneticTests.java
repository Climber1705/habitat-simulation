package com.tomtrotter.habitatsimulation.model.genetics;

import com.tomtrotter.habitatsimulation.core.domain.Animal;
import com.tomtrotter.habitatsimulation.simulation.entities.Tiger;
import com.tomtrotter.habitatsimulation.simulation.environment.Field;
import com.tomtrotter.habitatsimulation.simulation.environment.Location;
import com.tomtrotter.habitatsimulation.simulation.genetics.Genetics;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
* Unit tests for the Genetic system in the habitat simulation.
* This class tests the genetic properties and inheritance logic of the Animal class (specifically, Tigers).
*/
public class GeneticTests {

    private static final int height = 5;
    private static final int width = 5;

    private Field field;
    private final List<Animal> tigers = new ArrayList<>();

    /**
    * Sets up the testing environment before each test case.
    * Initializes the field and creates a set of tiger objects with different genetic attributes.
    */
    @BeforeEach
    public void setUp() {
        field = new Field(height, width);
        Tiger maleTiger1 = new Tiger(true, field, new Location(1,1), Color.BROWN, "15086350812080");
        Tiger femaleTiger1 = new Tiger(true, field, new Location(1,2), Color.BROWN,  "13043500323040");
        Tiger maleTiger2 = new Tiger(true, field, new Location(4,1), Color.BROWN,"15086350812080");
        Tiger femaleTiger2 = new Tiger(true, field, new Location(4,4), Color.BROWN, "15086350812080");

        maleTiger1.setGender(true);
        femaleTiger1.setGender(false);
        maleTiger2.setGender(true);
        femaleTiger2.setGender(false);

        tigers.add(maleTiger1);
        tigers.add(femaleTiger1);
        tigers.add(maleTiger2);
        tigers.add(femaleTiger2);
    }

    /**
    * Tests if the genetic material is successfully fragmented into its correct characteristics.
    */
    @Test
    public void fragmentationTest() {
        Animal maleTiger1 = new Tiger(true, field, new Location(1,1), Color.BROWN,"15086350812080");
        assertEquals(15, maleTiger1.genetics.getBreedingAge());
        assertEquals(86, maleTiger1.genetics.getMaxAge());
        assertEquals(0.35, maleTiger1.genetics.getBreedingProbability());
        assertEquals(8, maleTiger1.genetics.getMaxLitterSize());
        assertEquals(0.12, maleTiger1.genetics.getDiseaseProbability());
        assertEquals(0.8, maleTiger1.genetics.getMetabolism());
    }

    /**
    * Tests if genetic information is correctly shared between a male and a female tiger.
    */
    @Test
    public void sharingGenes() {
        String maleGene = tigers.getFirst().genetics.getGene();
        String femaleGene = tigers.get(1).genetics.getGene();
        assertEquals("15086350323040", tigers.get(1).genetics.getSharedGene(maleGene, femaleGene));
    }

    /**
    * Tests if gene segments are correctly extracted from genetic data.
    */
    @Test
    public void gettingValidGeneSegment() {
        Animal tiger = tigers.getFirst();
        assertEquals("15", tiger.genetics.getGeneSegment(Genetics.ATTRIBUTE.BREEDING_AGE, "15086350812080"), "Breeding Age");
        assertEquals("086", tiger.genetics.getGeneSegment(Genetics.ATTRIBUTE.MAX_AGE, "15086350812080"), "Max Age");
        assertEquals("35", tiger.genetics.getGeneSegment(Genetics.ATTRIBUTE.BREEDING_PROBABILITY, "15086350812080"), "Breeding Probability");
        assertEquals("08", tiger.genetics.getGeneSegment(Genetics.ATTRIBUTE.MAX_LITTER_SIZE, "15086350812080"), "Max Litter Size");
        assertEquals("12", tiger.genetics.getGeneSegment(Genetics.ATTRIBUTE.DISEASE_PROBABILITY, "15086350812080"), "Disease Probability");
        assertEquals("080", tiger.genetics.getGeneSegment(Genetics.ATTRIBUTE.METABOLISM, "15086350812080"), "Metabolism");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> tiger.genetics.getGeneSegment(Genetics.ATTRIBUTE.BREEDING_AGE,"1508635081208"));
        assertEquals("Unexpected gene length", exception.getMessage(), "Invalid gene length");
    }

    /**
    * Tests if the breeding age gene segment is within valid bounds.
    */
    @Test
    public void isValidBreedingAge() {
        Genetics.ATTRIBUTE BREEDING_AGE = Genetics.ATTRIBUTE.BREEDING_AGE;
        Tiger tiger = new Tiger(true, field, new Location(1,1), Color.BROWN, "15086350812080");
        assertTrue(tiger.genetics.isValid(BREEDING_AGE, 1, 15), "Breeding age 15, +1 to gene");
        assertTrue(tiger.genetics.isValid(BREEDING_AGE, -1, 15), "Breeding age 15, -1 to gene");

        assertTrue(tiger.genetics.isValid(BREEDING_AGE, 1, 12), "Breeding age 12, +1 to gene");
        assertFalse(tiger.genetics.isValid(BREEDING_AGE, -1, 12), "Breeding age 12, -1 to gene");

        assertTrue(tiger.genetics.isValid(BREEDING_AGE, -1, 90), "Breeding age 90, -1 to gene");
        assertFalse(tiger.genetics.isValid(BREEDING_AGE, 1, 90), "Breeding age 90, +1 to gene");
    }

    /**
    * Tests if the maximum age gene segment is within valid bounds.
    */
    @Test
    public void isValidMaxAge() {
        Genetics.ATTRIBUTE MAX_AGE = Genetics.ATTRIBUTE.MAX_AGE;
        Tiger tiger = new Tiger(true, field, new Location(1,1), Color.BROWN, "15086350812080");
        assertTrue(tiger.genetics.isValid(MAX_AGE, 1, 86), "Max age 86, +1 to gene");
        assertTrue(tiger.genetics.isValid(MAX_AGE, -1, 86), "Max age 86, -1 to gene");

        assertTrue(tiger.genetics.isValid(MAX_AGE, 1, 10), "Max age 10, +1 to gene");
        assertFalse(tiger.genetics.isValid(MAX_AGE, -1, 10), "Max age 10, -1 to gene");

        assertTrue(tiger.genetics.isValid(MAX_AGE, -1, 120), "Max age 120, -1 to gene");
        assertFalse(tiger.genetics.isValid(MAX_AGE, 1, 120), "Max age 120, +1 to gene");
    }

    /**
    * Tests if the breeding probability gene segment is within valid bounds.
    */
    @Test
    public void isValidBreedingProbability() {
        Genetics.ATTRIBUTE BREEDING_PROBABILITY = Genetics.ATTRIBUTE.BREEDING_PROBABILITY;
        Tiger tiger = new Tiger(true, field, new Location(1,1), Color.BROWN, "15086350812080");
        assertTrue(tiger.genetics.isValid(BREEDING_PROBABILITY, 1, 35), "Breeding Probability 35, +1 to gene");
        assertTrue(tiger.genetics.isValid(BREEDING_PROBABILITY, -1, 35), "Breeding Probability 35, -1 to gene");

        assertTrue(tiger.genetics.isValid(BREEDING_PROBABILITY, 1, 0), "Breeding Probability 0, +1 to gene");
        assertFalse(tiger.genetics.isValid(BREEDING_PROBABILITY, -1, 0), "Breeding Probability 0, -1 to gene");

        assertTrue(tiger.genetics.isValid(BREEDING_PROBABILITY, -1, 50), "Breeding Probability 50, -1 to gene");
        assertFalse(tiger.genetics.isValid(BREEDING_PROBABILITY, 1, 50), "Breeding Probability 50, +1 to gene");
    }

    /**
    * Tests if the maximum litter size gene segment is within valid bounds.
    */
    @Test
    public void isValidMaxLitterSize() {
        Genetics.ATTRIBUTE MAX_LITTER_SIZE = Genetics.ATTRIBUTE.MAX_LITTER_SIZE;
        Tiger tiger = new Tiger(true, field, new Location(1,1), Color.BROWN, "15086350812080");
        assertTrue(tiger.genetics.isValid(MAX_LITTER_SIZE, 1, 8), "Max Litter Size 8, +1 to gene");
        assertTrue(tiger.genetics.isValid(MAX_LITTER_SIZE, -1, 8), "Max Litter Size 8, -1 to gene");

        assertTrue(tiger.genetics.isValid(MAX_LITTER_SIZE, 1, 1), "Max Litter Size 1, +1 to gene");
        assertFalse(tiger.genetics.isValid(MAX_LITTER_SIZE, -1, 1), "Max Litter Size 1, -1 to gene");

        assertTrue(tiger.genetics.isValid(MAX_LITTER_SIZE, -1, 12), "Max Litter Size 12, -1 to gene");
        assertFalse(tiger.genetics.isValid(MAX_LITTER_SIZE, 1, 12), "Max Litter Size 12, +1 to gene");
    }

    /**
    * Tests if the disease probability gene segment is within valid bounds.
    */
    @Test
    public void isValidDiseaseProbability() {
        Genetics.ATTRIBUTE DISEASE_PROBABILITY = Genetics.ATTRIBUTE.DISEASE_PROBABILITY;
        Tiger tiger = new Tiger(true, field, new Location(1,1), Color.BROWN, "15086350812080");
        assertTrue(tiger.genetics.isValid(DISEASE_PROBABILITY, 1, 12), "Disease Probability 12, +1 to gene");
        assertTrue(tiger.genetics.isValid(DISEASE_PROBABILITY, -1, 12), "Disease Probability 12, -1 to gene");

        assertTrue(tiger.genetics.isValid(DISEASE_PROBABILITY, 1, 0), "Disease Probability 0, +1 to gene");
        assertFalse(tiger.genetics.isValid(DISEASE_PROBABILITY, -1, 0), "Disease Probability 0, -1 to gene");

        assertTrue(tiger.genetics.isValid(DISEASE_PROBABILITY, -1, 50), "Disease Probability 50, -1 to gene");
        assertFalse(tiger.genetics.isValid(DISEASE_PROBABILITY, 1, 50), "Disease Probability 50, +1 to gene");
    }

    /**
    * Tests if the metabolism gene segment is within valid bounds.
    */
    @Test
    public void isValidMetabolism() {
        Genetics.ATTRIBUTE METABOLISM = Genetics.ATTRIBUTE.METABOLISM;
        Tiger tiger = new Tiger(true, field, new Location(1,1), Color.BROWN, "15086350812080");
        assertTrue(tiger.genetics.isValid(METABOLISM, 1, 80), "Metabolism 80, +1 to gene");
        assertTrue(tiger.genetics.isValid(METABOLISM, -1, 80), "Metabolism 80, -1 to gene");

        assertTrue(tiger.genetics.isValid(METABOLISM, 1, 25), "Metabolism 25, +1 to gene");
        assertFalse(tiger.genetics.isValid(METABOLISM, -1, 25), "Metabolism 25, -1 to gene");

        assertTrue(tiger.genetics.isValid(METABOLISM, -1, 100), "Metabolism 100, +1 to gene");
        assertFalse(tiger.genetics.isValid(METABOLISM, 1, 100), "Metabolism 100, -1 to gene");
    }

    /**
    * Tests the formatting of new gene values to ensure proper structure.
    */
    @Test
    public void formattingNewGeneTest() {
        Tiger tiger = new Tiger(true, field, new Location(1,1), Color.BROWN, "15086350812080");
        assertEquals("16", tiger.genetics.formatCharacterValue(16,2));
        assertEquals("085", tiger.genetics.formatCharacterValue(85, 3));
        assertEquals("081", tiger.genetics.formatCharacterValue(81, 3));
        assertEquals("100", tiger.genetics.formatCharacterValue(100, 3));
    }

}
