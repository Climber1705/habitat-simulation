package com.tomtrotter.habitatsimulation.model;


import java.util.Random;

/**
* Provide control over the randomization of the simulation. By using the
* shared, fixed-seed randomizer, repeated runs will perform exactly the same
* (which helps with testing). Set 'useShared' too false to get different random
* behaviour every time.
*/

public class Randomizer {
  
    private static final int SEED = 1111;
    private static final Random rand = new Random(SEED);
    private static final boolean useShared = true;

    /**
    * Provide a random generator.
    * @return A random object.
    */
    public static Random getRandom() {
        if (useShared) {
            return rand;
        }
        else {
            return new Random();
        }
    }

}
