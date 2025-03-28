package com.tomtrotter.habitatsimulation.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import javafx.scene.paint.Color;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
* Unit tests for the behavior of animals (like tigers, leopards, wild boars)
* in a habitat simulation. It focuses on checking the correct hunting priorities and behaviors
* of different species based on age and food availability.
*/
public class FoodTests {

    public static final int depth = 5;
    public static final int width = 5;

    private Field field;
    private Location start;

    /**
    * Sets up the test fixtures before each test case.
    * This method is called before each individual test method.
    */
    @BeforeEach
    public void setUp() {
        field = new Field(depth, width);
        start = new Location(0,1);
    }

    /**
    * Creates a young tiger with initial attributes.
    * @return a newly created young tiger.
    */
    private Tiger createYoungTiger() {
        Tiger tiger = new Tiger(true, field, new Location(0,0), Color.ORANGE, "13043500323040");
        tiger.setGender(true);
        tiger.setAge(0);
        tiger.setDisease(false);
        tiger.setFoodLevel(100);
        return tiger;
    }

    /**
    * Creates a teenage tiger with initial attributes.
    * @return a newly created teen tiger.
    */
    private Tiger createTeenTiger() {
        Tiger tiger = new Tiger(true, field, new Location(0,0), Color.ORANGE, "13043500323040");
        tiger.setGender(true);
        tiger.setDisease(false);
        tiger.setAge(tiger.genetics.getBreedingAge() - 1);
        tiger.setFoodLevel(100);
        return tiger;
    }

    /**
    * Creates an adult tiger with initial attributes.
    * @return a newly created adult tiger.
    */
    private Tiger createAdultTiger() {
        Tiger tiger = new Tiger(true, field, new Location(0,0), Color.ORANGE, "13043500323040");
        tiger.setGender(true);
        tiger.setDisease(false);
        tiger.setAge(tiger.genetics.getBreedingAge() + 1);
        tiger.setFoodLevel(100);
        return tiger;
    }

    /**
    * Tests whether animals are correctly considered "young" based on their age.
    * It also checks that animals are no longer considered young once they reach breeding age.
    */
    @Test
    public void testAnimalIsYoung() {
        // Place young, teen, and adult tigers and check their youth status.
        Tiger youngTiger = createYoungTiger();
        Tiger teenTiger = createTeenTiger();
        Tiger adultTiger = createAdultTiger();

        // Verify the initial youth status of the tigers.
        assertTrue(youngTiger.isYoung(), "Young tiger isn't young");
        assertTrue(teenTiger.isYoung(), "Teen tiger isn't young");
        assertFalse(adultTiger.isYoung(), "Adult tiger is young");

        // Call act() increments the age of animals. Test that the status changes correctly.
        List<Animal> newAnimals = new ArrayList<>();
        youngTiger.act(newAnimals);
        teenTiger.act(newAnimals);
        adultTiger.act(newAnimals);

        // Verify the final youth status after acting.
        assertTrue(youngTiger.isYoung(), "Young tiger isn't young");
        assertFalse(teenTiger.isYoung(), "Teen tiger is young");
        assertFalse(adultTiger.isYoung(), "Adult tiger is young");
    }

    /**
    * Tests to verify the hunting priorities of young tigers.
    * Specifically, young tigers prioritize hunting hares over deers and never hunt wild boars.
    */
    @Test
    public void testYoungTigerHuntingPriority() {
        // Place a young tiger and prey animals (hare, deer, wild boar).
        Tiger youngTiger = new Tiger(true, field, start, Color.ORANGE,"13043500323040");
        youngTiger.setGender(true);
        youngTiger.setAge(0);
        youngTiger.setDisease(false);
        youngTiger.setFoodLevel(100);

        Hare hare = new Hare(true, field, new Location(0,2), Color.BROWN, "13043500323040");
        Deer deer = new Deer(true, field, new Location(1,1), Color.BROWN, "13043500323040");
        WildBoar wildBoar = new WildBoar(true, field, new Location(0,0), Color.BROWN, "13043500323040");

        // Verify the initial state of prey animals and tiger.
        assertTrue(hare.isAlive(), "Hare isn't alive");
        assertTrue(deer.isAlive(), "Deer isn't alive");
        assertTrue(wildBoar.isAlive(), "Wild boar isn't alive");

        // Call act() to allow the young tiger to hunt.
        List<Animal> newAnimals = new ArrayList<>();
        youngTiger.act(newAnimals);

        // Verify that the tiger hunted the hare and ignored the deer and wild boar.
        assertFalse(hare.isAlive(), "Hare is alive");
        assertTrue(deer.isAlive(), "Deer is dead");
        assertTrue(wildBoar.isAlive(), "Wild boar is dead");

        // Test when only the wild boar is left as prey.
        youngTiger.setLocation(start);
        field.placeAnimal(youngTiger, youngTiger.getLocation());
        deer.setDead();
        field.removeAnimal(deer.getLocation());

        // Verify that the tiger never hunts the wild boar.
        youngTiger.act(newAnimals);
        assertFalse(hare.isAlive(), "Hare is alive");
        assertFalse(deer.isAlive(), "Deer is alive");
        assertTrue(wildBoar.isAlive(), "Wild boar is dead");
    }

    /**
    * Tests to verify the hunting priority of adult tigers.
    * Adult tigers prefer wild boars to deers and hares.
    */
    @Test
    public void testTigerHuntingPriority() {
        // Place an adult tiger and prey animals (hare, deer, wild boar).
        Tiger adultTiger = new Tiger(true, field, start, Color.ORANGE, "13043500323040");
        adultTiger.setGender(true);
        adultTiger.setAge(adultTiger.genetics.getBreedingAge() + 1);
        adultTiger.setDisease(false);
        adultTiger.setFoodLevel(100);
        
        Hare hare = new Hare(true, field, new Location(0,2), Color.BROWN, "13043500323040");
        Deer deer = new Deer(true, field, new Location(1,1), Color.BROWN, "13043500323040");
        WildBoar wildBoar = new WildBoar(true, field, new Location(0,0), Color.BROWN, "13043500323040");

        // Verify the initial state of prey animals and tiger.
        assertTrue(hare.isAlive(), "Hare isn't alive");
        assertTrue(deer.isAlive(), "Deer isn't alive");
        assertTrue(wildBoar.isAlive(), "Wild boar isn't alive");

        // Call act() to allow the adult tiger to hunt.
        List<Animal> newAnimals = new ArrayList<>();
        adultTiger.act(newAnimals);

        // Verify that the tiger hunted the wild boar first.
        assertTrue(hare.isAlive(), "Hare is dead");
        assertTrue(deer.isAlive(), "Deer is dead");
        assertFalse(wildBoar.isAlive(), "Wild boar is alive");

        // Move tiger back and check preference for deer.
        adultTiger.setLocation(start);
        field.placeAnimal(adultTiger, adultTiger.getLocation());

        adultTiger.act(newAnimals);

        // Verify that the deer was hunted over the hare.
        assertTrue(hare.isAlive(), "Hare is dead");
        assertFalse(deer.isAlive(), "Deer is alive");
        assertFalse(wildBoar.isAlive(), "Wild boar is alive");
    }

    /**
    * Tests to verify the hunting priority of leopards.
    * Leopards prioritize hunting deer over wild boars and hares.
    */
    @Test
    public void testLeopardHuntingPriority() {
        // Place a leopard and its prey animals.
        Leopard adultLeopard = new Leopard(true, field, start, Color.ORANGE, "13043500323040");
        adultLeopard.setGender(true);
        adultLeopard.setAge(adultLeopard.genetics.getBreedingAge() + 1);
        adultLeopard.setFoodLevel(100);
        adultLeopard.setDisease(false);
        
        Hare hare = new Hare(true, field, new Location(0,2), Color.BROWN, "13043500323040");
        Deer deer = new Deer(true, field, new Location(1,1), Color.BROWN, "13043500323040");
        WildBoar wildBoar = new WildBoar(true, field, new Location(0,0), Color.BROWN, "13043500323040");

        // Verify the initial state of prey animals and leopard.
        assertTrue(hare.isAlive(), "Hare is dead");
        assertTrue(deer.isAlive(), "Deer is dead");
        assertTrue(wildBoar.isAlive(), "Wild boar is dead");

        // Call act() to allow the leopard to hunt.
        List<Animal> newAnimals = new ArrayList<>();
        adultLeopard.act(newAnimals);

        // Verify that the leopard hunted the deer first.
        assertTrue(hare.isAlive(), "Hare is dead");
        assertFalse(deer.isAlive(), "Deer is alive");
        assertTrue(wildBoar.isAlive(), "Wild boar is dead");

        // Move Leopard back and check preference for hare over wild boar.
        adultLeopard.setLocation(start);
        field.placeAnimal(adultLeopard, adultLeopard.getLocation());
        
        // Call act() to allow Leopard to hunt.
        adultLeopard.act(newAnimals);

        // Verify that the leopard hunted the hare over the wild boar.
        assertFalse(hare.isAlive(), "Hare is alive");
        assertFalse(deer.isAlive(), "Deer is alive");
        assertTrue(wildBoar.isAlive(), "Wild boar is dead");
    }

    /**
    * Tests to verify the hunting and grazing priority of wild boars.
    * Wild boars graze plants but may occasionally hunt hares when no plants are available.
    */
    @Test
    public void testWildBoarHuntingPriority() {
        // Place a wild boar and surrounding prey.
        WildBoar wildBoar = new WildBoar(true, field, start, Color.BLACK, "13043500323040");
        wildBoar.setAge(10);
        wildBoar.setFoodLevel(100);
        wildBoar.setDisease(false);
        wildBoar.setGender(true);

        Hare hare = new Hare(true, field, new Location(0,2), Color.BROWN, "13043500323040");
        Deer deer = new Deer(true, field, new Location(1,1), Color.BROWN, "13043500323040");
        Plant plant = new Plant(field, new Location(0,0));
        field.placePlant(plant, plant.getLocation());

        // Verify initial positions and that the hare is alive.
        assertTrue(hare.isAlive(), "Hare is dead");
        assertNotNull(field.getPlantAt(0,0), "There isn't a plant present at (0,0)");

        // Force the wild boar to hunt and verify the prey preference.
        wildBoar.setHuntProbability(1);
        List<Animal> newAnimals = new ArrayList<>();
        wildBoar.act(newAnimals);

        // Wild boar should have hunted the hare over the plant.
        assertFalse(hare.isAlive(), "Hare is alive");
        assertTrue(deer.isAlive(), "Deer is dead");
        assertNotNull(field.getPlantAt(0,0), "There's no plant present at (0,0)");

        // Make the wild boar graze when there is no hare and check its preference.
        wildBoar.setLocation(start);
        field.placeAnimal(wildBoar, wildBoar.getLocation());
        deer.setDead();
        field.removeAnimal(deer.getLocation());

        wildBoar.setHuntProbability(0); // Set to graze instead of hunt
        wildBoar.act(newAnimals);

        // Verify the wild boar ate the plant instead of hunting.
        assertFalse(hare.isAlive(), "Hare is alive");
        assertNull(field.getPlantAt(0,0), "There is a plant present at (0,0)");

        // Move the wild boar back and add another hare and plant for grazing.
        wildBoar.setLocation(start);
        field.placeAnimal(wildBoar, wildBoar.getLocation());
        Hare newHare = new Hare(true, field, new Location(0,2), Color.BROWN, "13043500323040");
        Plant newPlant = new Plant(field, new Location(0,0));
        field.placePlant(newPlant, plant.getLocation());

        // Wild boar should now graze.
        wildBoar.setHuntProbability(0);
        wildBoar.act(newAnimals);

        // Verify the wild boar grazes the plant.
        assertTrue(newHare.isAlive(), "New hare is dead");
        assertNull(field.getPlantAt(0,0), "There is a plant present at (0,0)");
    }

}

