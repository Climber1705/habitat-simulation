package com.tomtrotter.habitatsimulation.model;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
* Unit tests that verify the implementation of disease spread
* within the simulation. It ensures that diseases transfer correctly between
* animals of the same species while not affecting others.
*/
public class DiseaseTests {

    public static final int height = 5;
    public static final int width = 5;
    
    private Field field;
    
    private Tiger maleTiger1, femaleTiger1;
    private Deer deer;

    /**
    * Sets up the test environment by initializing the field and animals,
    * setting their attributes, and placing them in the field.
    * One tiger starts as diseased to test the spread of infection.
    */
    @BeforeEach
    public void setUp() {
        field = new Field(height, width);
    
        maleTiger1 = new Tiger(true, field, new Location(0,0), Color.BROWN, "15086350812080");
        femaleTiger1 = new Tiger(true, field, new Location(0,1), Color.BROWN, "13043500323040");
        
        deer = new Deer(true, field, new Location(1,1), Color.ORANGE, "15086350812080");
       
        setGenders();
        setAges();
        setBreedingProbabilities();
        setFoodLevels();
        setDiseaseStatus();
        placeAnimals();
    }

    /**
    * Assigns gender to each test animal.
    */
    private void setGenders() {
        maleTiger1.setGender(true);
        femaleTiger1.setGender(false);
        deer.setGender(true);
    }

    /**
    * Sets the ages of the animals to ensure they are fully developed.
    */
    private void setAges() {
        maleTiger1.setAge(maleTiger1.genetics.getBreedingAge() + 1);
        femaleTiger1.setAge(femaleTiger1.genetics.getBreedingAge() + 1);
        deer.setAge(deer.genetics.getBreedingAge() + 1);
    }

    /**
    * Disables breeding for all animals to prevent interference with disease testing.
    */
    private void setBreedingProbabilities() {
        maleTiger1.genetics.setBreedingProbability(0);
        femaleTiger1.genetics.setBreedingProbability(0);
        deer.genetics.setBreedingProbability(0);
    }

    /**
    * Ensures all animals have sufficient food levels,
    * removing starvation as a factor.
    */
    private void setFoodLevels() {
        maleTiger1.setFoodLevel(100);
        femaleTiger1.setFoodLevel(100);
        deer.setFoodLevel(100);
    }

    /**
    * Sets the disease status for animals:
    * - The male tiger starts as diseased.
    * - The female tiger and deer start healthy.
    * - The female tiger and deer have a high probability of contracting the disease.
    */
    private void setDiseaseStatus() {
        maleTiger1.setDisease(true);
        femaleTiger1.setDisease(false);
        deer.setDisease(false);

        femaleTiger1.genetics.setDiseaseProbability(1);
        deer.genetics.setDiseaseProbability(1);
    }

    /**
    * Places all animals in the field at their designated locations.
    */
    private void placeAnimals() {
        field.placeAnimal(maleTiger1, maleTiger1.getLocation());
        field.placeAnimal(femaleTiger1, femaleTiger1.getLocation());
        field.placeAnimal(deer, deer.getLocation());
    }

    /**
    * Tests if the disease spreads only to animals of the same species.
    * <p>
    * - The male tiger starts diseased.
    * - The female tiger is expected to become diseased after acting.
    * - Deer should remain healthy, as diseases do not cross species.
    */
    @Test
    public void testDiseaseSpread() {
        List<Animal> newAnimals = new ArrayList<>();
        maleTiger1.act(newAnimals);
        assertFalse(femaleTiger1.isDiseased(), "The female tiger isn't infected");
        assertFalse(deer.isDiseased(), "The male deer isn't infected");
        
        femaleTiger1.act(newAnimals);
        assertTrue(femaleTiger1.isDiseased(), "The female tiger is infected");
        assertFalse(deer.isDiseased(), "The male deer isn't infected");
    }

}

 
 
