package com.tomtrotter.habitatsimulation.model.core;

import com.tomtrotter.habitatsimulation.core.domain.Animal;
import com.tomtrotter.habitatsimulation.simulation.entities.Deer;
import com.tomtrotter.habitatsimulation.simulation.entities.Tiger;
import com.tomtrotter.habitatsimulation.simulation.environment.Field;
import com.tomtrotter.habitatsimulation.simulation.environment.Location;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
* Unit tests for animal breeding behavior in a habitat simulation.
* This class tests the fertility conditions of tigers and deer,
* ensuring that breeding logic functions as expected.
*/
public class BreedingTests {

    public static final int height = 10;
    public static final int width = 10;

    private Field field;
    private final List<Animal> tigers = new ArrayList<>();
    private final List<Animal> deers = new ArrayList<>();

    private Tiger maleTiger1, femaleTiger1, maleTiger2, femaleTiger2;
    private Deer maleDeer1, femaleDeer1, maleDeer2, femaleDeer2;

    /**
    * Sets up the test environment by initializing the field,
    * creating animals, setting their attributes, and placing them in the field.
    */
    @BeforeEach
    public void setUp() {
        field = new Field(height, width);

        maleTiger1 = new Tiger(true, field, new Location(1,1), Color.BROWN, "15086350812080");
        femaleTiger1 = new Tiger(true, field, new Location(1,2), Color.BROWN, "13043500323040");
        maleTiger2 = new Tiger(true, field, new Location(4,1), Color.BROWN,"15086350812080");
        femaleTiger2 = new Tiger(true, field, new Location(4,4), Color.BROWN,"15086350812080");

        maleDeer1 = new Deer(true, field, new Location(5,5), Color.ORANGE,"15086350812080");
        femaleDeer1 = new Deer(true, field, new Location(5, 6), Color.ORANGE, "13043500323040");
        maleDeer2 = new Deer(true, field, new Location(8, 5), Color.ORANGE, "15086350812080");
        femaleDeer2 = new Deer(true, field, new Location(8,8), Color.BROWN, "15086350812080");

        setGenders();
        setAges();
        setBreedingProbabilities();
        setFoodLevels();
        placeAnimals();

        tigers.add(maleTiger1);
        tigers.add(femaleTiger1);
        tigers.add(maleTiger2);
        tigers.add(femaleTiger2);

        deers.add(maleDeer1);
        deers.add(femaleDeer1);
        deers.add(maleDeer2);
        deers.add(femaleDeer2);
    }

    /**
    * Assigns gender to each test animal.
    */
    private void setGenders() {
        maleTiger1.setGender(true);
        femaleTiger1.setGender(false);
        maleTiger2.setGender(true);
        femaleTiger2.setGender(false);

        maleDeer1.setGender(true);
        femaleDeer1.setGender(false);
        maleDeer2.setGender(true);
        femaleDeer2.setGender(false);
    }

    /**
    * Sets the ages of the animals to ensure they are old enough to breed.
    */
    private void setAges() {
        maleTiger1.setAge(maleTiger1.genetics.getBreedingAge() + 1);
        femaleTiger1.setAge(femaleTiger1.genetics.getBreedingAge() + 1);
        maleTiger2.setAge(maleTiger2.genetics.getBreedingAge() + 1);
        femaleTiger2.setAge(femaleTiger2.genetics.getBreedingAge() + 1);

        maleDeer1.setAge(maleDeer1.genetics.getBreedingAge() + 1);
        femaleDeer1.setAge(femaleDeer1.genetics.getBreedingAge() + 1);
        maleDeer2.setAge(maleDeer2.genetics.getBreedingAge() + 1);
        femaleDeer2.setAge(femaleDeer2.genetics.getBreedingAge() + 1);
    }

    /**
    * Sets the breeding probability of all animals to maximum (1)
    * to ensure breeding occurs in the test cases.
    */
    private void setBreedingProbabilities() {
        maleTiger1.genetics.setBreedingProbability(1);
        femaleTiger1.genetics.setBreedingProbability(1);
        maleTiger2.genetics.setBreedingProbability(1);
        femaleTiger2.genetics.setBreedingProbability(1);

        maleDeer1.genetics.setBreedingProbability(1);
        femaleDeer1.genetics.setBreedingProbability(1);
        maleDeer2.genetics.setBreedingProbability(1);
        femaleDeer2.genetics.setBreedingProbability(1);
    }

    /**
    * Sets the food levels of all animals to 100,
    * ensuring they have sufficient energy for breeding.
    */
    private void setFoodLevels() {
        maleTiger1.setFoodLevel(100);
        femaleTiger1.setFoodLevel(100);
        maleTiger2.setFoodLevel(100);
        femaleTiger2.setFoodLevel(100);

        maleDeer1.setFoodLevel(100);
        femaleDeer1.setFoodLevel(100);
        maleDeer2.setFoodLevel(100);
        femaleDeer2.setFoodLevel(100);
    }

    /**
    * Places all animals in the field at their designated locations.
    */
    private void placeAnimals() {
        field.placeOrganism(maleTiger1, maleTiger1.getLocation());
        field.placeOrganism(femaleTiger1, femaleTiger1.getLocation());
        field.placeOrganism(maleTiger2, maleTiger2.getLocation());
        field.placeOrganism(femaleTiger2, femaleTiger2.getLocation());

        field.placeOrganism(maleDeer1, maleDeer1.getLocation());
        field.placeOrganism(femaleDeer1, femaleDeer1.getLocation());
        field.placeOrganism(maleDeer2, maleDeer2.getLocation());
        field.placeOrganism(femaleDeer2, femaleDeer2.getLocation());
    }

    /**
    * Tests that male tigers cannot reproduce.
    */
    @Test
    public void fertilityTigerMaleTest() {
        List<Animal> newAnimals = new ArrayList<>();
        Animal maleTiger1 = tigers.getFirst();
        maleTiger1.genetics.setBreedingAge(2);
        maleTiger1.act(newAnimals);
        assertTrue(newAnimals.isEmpty());
    }

    /**
    * Tests that female tigers can reproduce if a male is nearby.
    */
    @Test
    public void fertilityTigerFemaleTest() {
        List<Animal> newAnimals = new ArrayList<>();
        Animal femaleTiger1 = tigers.get(1);
        femaleTiger1.genetics.setBreedingAge(2);
        femaleTiger1.act(newAnimals);
        assertFalse(newAnimals.isEmpty());
    }

    /**
    * Tests that female tigers cannot reproduce if no male is nearby.
    */
    @Test
    public void fertilityTigerFemaleAloneTest() {
        List<Animal> newAnimals = new ArrayList<>();
        Animal femaleTiger2 = tigers.getLast();
        femaleTiger2.genetics.setBreedingAge(2);
        femaleTiger2.act(newAnimals);
        assertTrue(newAnimals.isEmpty());
    }

    /**
    * Tests that male tigers cannot reproduce alone.
    */
    @Test
    public void fertilityTigerMaleAloneTest() {
        List<Animal> newAnimals = new ArrayList<>();
        Animal maleTiger2 = tigers.get(2);
        maleTiger2.genetics.setBreedingAge(2);
        maleTiger2.act(newAnimals);
        assertTrue(newAnimals.isEmpty());
    }

    /**
    * Tests that male deer cannot reproduce.
    */
    @Test
    public void fertilityDeerMaleTest() {
        List<Animal> newAnimals = new ArrayList<>();
        Animal maleDeer1 = deers.getFirst();
        maleDeer1.genetics.setBreedingAge(2);
        maleDeer1.act(newAnimals);
        assertTrue(newAnimals.isEmpty());
    }

    /**
    * Tests that female deer can reproduce if a male is nearby.
    */
    @Test
    public void fertilityDeerFemaleTest() {
        List<Animal> newAnimals = new ArrayList<>();
        Animal femaleDeer1 = deers.get(1);
        femaleDeer1.genetics.setBreedingAge(2);
        femaleDeer1.act(newAnimals);
        assertFalse(newAnimals.isEmpty());
    }

    /**
    * Tests that female deer cannot reproduce if no male is nearby.
    */
    @Test
    public void fertilityDeerFemaleAloneTest() {
        List<Animal> newAnimals = new ArrayList<>();
        Animal femaleDeer2 = deers.getLast();
        femaleDeer2.genetics.setBreedingAge(2);
        femaleDeer2.act(newAnimals);
        assertTrue(newAnimals.isEmpty());
    }

    /**
    * Tests that male deer cannot reproduce alone.
    */
    @Test
    public void fertilityDeerMaleAloneTest() {
        List<Animal> newAnimals = new ArrayList<>();
        Animal maleDeer2 = deers.get(2);
        maleDeer2.genetics.setBreedingAge(2);
        maleDeer2.act(newAnimals);
        assertTrue(newAnimals.isEmpty());
    }

}
