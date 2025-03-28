package com.tomtrotter.habitatsimulation.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import javafx.scene.paint.Color;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
* Unit tests for verifying the behavior of plant and animal interactions within the habitat simulation.
* This class includes unit tests for scenarios such as plant replacement by newborns,
* animal deaths due to various causes, and plant consumption by herbivores.
* <p>
* The tests ensure the simulator correctly updates the ecosystem based on predefined rules.
*/
public class PlantTests {

    public static final int depth = 5;
    public static final int width = 5;

    private Field field;

    /**
    * Sets up the test fixture by initializing a field before each test case runs.
    */
    @BeforeEach
    public void setUp() {
        field = new Field(depth, width);
    }

    /**
    * Tests whether newborn animals correctly replace plants when they are born.
    * Ensures that plants in adjacent cells are removed and replaced with new animals
    * when birth occurs within a suitable location.
    */
    @Test
    public void testNewbornReplacesPlant() {
        // Initialize the field with plants in empty cells
        for (int row = 0; row < field.getHeight(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (field.getObjectAt(row, col) == null) {
                    Plant plant = new Plant(field, new Location(row, col));  // Assuming you have a Plant class and Location
                    field.placePlant(plant, new Location(row, col));
                }
            }
        }

        // Setup male and female deer with breeding capabilities
        Deer maleDeer = new Deer(true, field, new Location(0,0), Color.BROWN,  "15086350812080");
        Deer femaleDeer = new Deer(true, field, new Location(0,1), Color.BROWN,  "13043500323040");

        // Configure deer properties for breeding
        maleDeer.setGender(true);
        maleDeer.genetics.setBreedingAge(2);
        maleDeer.genetics.setMaxAge(100);
        maleDeer.setAge(maleDeer.genetics.getBreedingAge() + 1);
        maleDeer.genetics.setBreedingProbability(1);
        maleDeer.setFoodLevel(100);
        maleDeer.setDisease(false);
        
        femaleDeer.setGender(false);
        femaleDeer.genetics.setBreedingAge(2);
        femaleDeer.genetics.setMaxAge(100);
        femaleDeer.setAge(femaleDeer.genetics.getBreedingAge() + 1); 
        femaleDeer.genetics.setBreedingProbability(1);
        femaleDeer.setFoodLevel(100);
        femaleDeer.setDisease(false);

        field.placeAnimal(maleDeer, maleDeer.getLocation());
        field.placeAnimal(femaleDeer, femaleDeer.getLocation());

        // Verify correct field setup before birth
        assertNotNull(field.getPlantAt(0, 2), "Initially there is a plant in (0,1)");
        assertNotNull(field.getPlantAt(1, 1), "Initially there is a plant in (1,1)");
        assertNotNull(field.getAnimalAt(0, 0), "Initially there is a Deer in (0,0) (this is the male deer)");
        assertNotNull(field.getAnimalAt(0, 1), "Initially there is a Deer in (0,1) (this is the female deer)");

        // Simulate birth and check if plants were replaced
        List<Animal> newAnimals = new ArrayList<>();
        femaleDeer.giveBirth(newAnimals);

        // Ensure a newborn was created
        int correctNumber = 0;
        System.out.println("Newborns created: " + newAnimals.size());
        assertFalse(newAnimals.isEmpty(), "A newborn should have been created");

        if (field.getPlantAt(0,2) == null) {
            System.out.println("Deer ate plant at (0, 2)");
            assertNotNull(field.getAnimalAt(0,2), "There is now a deer in (0,2) (this is the newborn)");
            assertNull(field.getPlantAt(0,2), "There is no longer a plant at (0,2) as a newborn replaced it");
            assertNotNull(field.getAnimalAt(0, 0), "Initially there is still a Deer in (0,0) (this is the male deer)");
            assertNotNull(field.getAnimalAt(0, 1), "Initially there is still a Deer in (0,1) (this is the female deer)");
            correctNumber++;
        }
        if (field.getPlantAt(1,2) == null) {
            System.out.println("Deer ate plant at (1, 2)");
            assertNotNull(field.getAnimalAt(1,2), "There is now a deer in (1,2) (this is the newborn)");
            assertNull(field.getPlantAt(1,2), "There is no longer a plant at (1,2) as a newborn replaced it");
            assertNotNull(field.getAnimalAt(0, 0), "Initially there is still a Deer in (0,0) (this is the male deer)");
            assertNotNull(field.getAnimalAt(0, 1), "Initially there is still a Deer in (0,1) (this is the female deer)");
            correctNumber++;
        }
        if(field.getPlantAt(1,1) == null) {
            System.out.println("Deer ate plant at (1, 1)");
            assertNotNull(field.getAnimalAt(1,1), "There is now a deer in (1,1) (this is the newborn)");
            assertNull(field.getPlantAt(1,1), "There is no longer a plant at (1,1) as a newborn replaced it");
            assertNotNull(field.getAnimalAt(0, 0), "Initially there is still a Deer in (0,0) (this is the male deer)");
            assertNotNull(field.getAnimalAt(0, 1), "Initially there is still a Deer in (0,1) (this is the female deer)");
            correctNumber++;
        }
        if(field.getPlantAt(1,0) == null) {
            System.out.println("Deer ate plant at (1, 0)");
            assertNotNull(field.getAnimalAt(1,0), "There is now a deer in (1,0) (this is the newborn)");
            assertNull(field.getPlantAt(1,0), "There is no longer a plant at (1,0) as a newborn replaced it");
            assertNotNull(field.getAnimalAt(0, 0), "Initially there is still a Deer in (0,0) (this is the male deer)");
            assertNotNull(field.getAnimalAt(0, 1), "Initially there is still a Deer in (0,1) (this is the female deer)");
            correctNumber++;
        }
        assertEquals(correctNumber, newAnimals.size());
    }

    /**
    * Tests whether plants replace animals that die due to starvation.
    * Ensures that when an animal's food level reaches zero, it is removed from
    * the field and a plant appears.
    */
    @Test
    public void testAnimalDeathByStarvationReplacedByPlants() {
        Deer maleDeer = new Deer(true, field, new Location(0,0), Color.BROWN,  "13043500323040");
        maleDeer.setGender(true);
        maleDeer.setFoodLevel(0.1);
        maleDeer.setDisease(false);
        
        field.placeAnimal(maleDeer, maleDeer.getLocation());
        
        assertNull(field.getPlantAt(0,0), "Initially there is no plant at (0,0)");
        assertNotNull(field.getAnimalAt(0,0), "Initially there is a deer at (0,0)");
        
        maleDeer.incrementHunger();

        assertNull(field.getAnimalAt(0,0), "There is no longer a deer at (0,0), it died due to starvation");
        assertNotNull(field.getPlantAt(0,0), "A plant replaced the dead deer at (0,0)");
    }

    /**
    * Tests whether plants replace animals that die due to old age.
    * Ensures that when an animal exceeds its maximum age, it is removed from
    * the field and a plant appears.
    */
    @Test
    public void testAnimalDeathByOldAgeReplacedByPlants() {
        Deer maleDeer = new Deer(true, field, new Location(0,0), Color.BROWN,  "13043500323040");
        maleDeer.setGender(true);
        maleDeer.setAge(1);
        maleDeer.genetics.setMaxAge(1);
        maleDeer.setDisease(false);
        
        field.placeAnimal(maleDeer, maleDeer.getLocation());
        
        assertNull(field.getPlantAt(0,0), "Initially there is no plant at (0,0)");
        assertNotNull(field.getAnimalAt(0,0), "Initially there is a deer at (0,0)");

        maleDeer.incrementAge();

        assertNull(field.getAnimalAt(0,0), "There is no longer a deer at (0,0), it died due to old age");
        assertNotNull(field.getPlantAt(0,0), "A plant replaced the dead deer at (0,0)");
    }

    /**
    * Tests whether plants are replacing animals dying due to overcrowding.
    * Ensures that when an animal dies due to a lack of available space,
    * a plant appears in its place.
    */
    @Test
    public void testAnimalDeathByOvercrowdingReplacedByPlants() {
        // Place a male deer at (1,1)
        Deer maleDeer = new Deer(true, field, new Location(1,1), Color.BROWN,  "13043500323040");
        maleDeer.setGender(true);
        maleDeer.setFoodLevel(100);
        maleDeer.setDisease(false);
        
        field.placeAnimal(maleDeer, maleDeer.getLocation());

        // Place male hares in all adjacent cells to the deer to induce overcrowding
        Hare[] hares = {
                new Hare(true, field, new Location(0, 0), Color.BROWN, "13043500323040"),
                new Hare(true, field, new Location(0, 1), Color.BROWN, "13043500323040"),
                new Hare(true, field, new Location(0, 2), Color.BROWN, "13043500323040"),
                new Hare(true, field, new Location(1, 0), Color.BROWN, "13043500323040"),
                new Hare(true, field, new Location(1, 2), Color.BROWN, "13043500323040"),
                new Hare(true, field, new Location(2, 0), Color.BROWN, "13043500323040"),
                new Hare(true, field, new Location(2, 1), Color.BROWN, "13043500323040"),
                new Hare(true, field, new Location(2, 2), Color.BROWN, "13043500323040")
        };

        for (Hare hare: hares) {
            hare.setDisease(false);
            field.placeAnimal(hare, hare.getLocation());
        }

        // Verify correct field setup
        assertNull(field.getPlantAt(1,1), "Initially there is no plant at (1,1)");
        assertNotNull(field.getAnimalAt(1,1), "Initially there is a deer at (1,1)");

        // Simulate the deer's action, triggering its death due to overcrowding
        List<Animal> newAnimals = new ArrayList<>();
        maleDeer.act(newAnimals);

        // Verify that the deer is removed and replaced by a plant
        assertNull(field.getAnimalAt(1,1), "There is no longer a deer at (1,1), it died due to overcrowding");
        assertNotNull(field.getPlantAt(1,1), "A plant replaced the dead deer at (1,1)");
    }

    /**
    * Tests whether plants are replacing animals dying due to disease.
    * Ensures that a plant appears in its place when an animal dies from disease.
    */
    @Test
    public void testAnimalDeathByDiseaseReplacedByPlants() {
        // Place a diseased deer at (1,1)
        Deer maleDeer = new Deer(true, field, new Location(1,1), Color.BROWN,  "13043500323040");
        maleDeer.setDisease(true);
        maleDeer.genetics.setDiseaseProbability(1); // Ensures 100% chance of disease spread

        // Simulate disease progression until death
        for(int i = 0; i < Animal.DISEASE_DURATION; i++) {
            maleDeer.incrementDisease();
        }
        // Simulate the deer's action, which should result in death
        List<Animal> newAnimals = new ArrayList<>();
        maleDeer.act(newAnimals);

        // Verify that the deer is removed and replaced by a plant
        assertNull(field.getAnimalAt(1,1), "There's no longer a deer at (1,1), it died due to disease");
        assertNotNull(field.getPlantAt(1,1), "A plant replaced the dead deer at (1,1)");
    }

    /**
    * Tests whether plants do NOT replace animals dying due to predation.
    * Ensures that no plant appears in its place when a predator eats an animal.
    */
    @Test
    public void testAnimalDeathByEatenNotReplacedByPlants() {
        // Place a tiger and a deer next to each other
        Tiger maleTiger = new Tiger(true, field, new Location(0,0), Color.ORANGE,  "13043500323040");
        maleTiger.setGender(true);
        maleTiger.setDisease(false);
        
        Deer deer = new Deer(true, field, new Location(0,1), Color.BROWN,  "13043500323040");
        deer.setDisease(false);
        
        field.placeAnimal(maleTiger, maleTiger.getLocation());
        field.placeAnimal(deer, deer.getLocation());

        // Verify initial conditions that Tiger at (0,0), deer at (0,1), no plants
        assertTrue((maleTiger.getLocation().getRow() == 0) && (maleTiger.getLocation().getCol() == 0), "Initially there is a tiger at (0,0)");
        assertTrue((deer.getLocation().getRow() == 0) && (deer.getLocation().getCol() == 1), "Initially there is a deer at (0,1)");
        assertNull(field.getPlantAt(0,1), "Initially there is no plant at (0,1)");

        // Simulate the tiger's action (hunting the deer)
        List<Animal> newAnimals = new ArrayList<>();
        maleTiger.act(newAnimals);

        // Verify that the deer was eaten but not replaced by a plant
        assertNull(field.getPlantAt(0,1), "There is still no plant at (0,1)");
    }

    /**
    * Tests whether plants eaten by herbivores are removed from the field.
    * Ensures that it is no longer present when a herbivore eats a plant.
    */
    @Test
    public void testPlantRemovedWhenEaten() {
        // Place a deer and a plant next to each other
        Deer maleDeer = new Deer(true, field, new Location(0,0), Color.ORANGE,  "13043500323040");
        maleDeer.setGender(true);
        maleDeer.setDisease(false);
        maleDeer.setFoodLevel(100);
        maleDeer.setAge(10);
        maleDeer.genetics.setMaxAge(100);
        
        Plant plant = new Plant(field, new Location(0,1));
        
        field.placeAnimal(maleDeer, maleDeer.getLocation());
        field.placePlant(plant, plant.getLocation());

        // Verify initial conditions that the Deer at (0,0), the plant at (0,1)
        assertTrue((maleDeer.getLocation().getRow() == 0) && (maleDeer.getLocation().getCol() == 0), "Initially there is a deer at (0,0)");
        assertNotNull(field.getPlantAt(0,1), "Initially there is a plant at (0,1)");

        // Simulate the deer's action (grazing on the plant)
        List<Animal> newAnimals = new ArrayList<>();
        maleDeer.act(newAnimals);

        // Verify that the plant has been eaten and is no longer in the field
        assertTrue((maleDeer.getLocation().getRow() == 0) && (maleDeer.getLocation().getCol() == 1), "The deer is now at (0,1), as it ate the plant");
        assertNull(field.getPlantAt(0,1), "There is no longer a plant at (0,1)");
    }

}

