package com.tomtrotter.habitatsimulation.simulation.genetics.mutation;

/**
* A mutation strategy that randomly sets a boolean value regardless of its original state.
* This strategy always returns a random boolean value with a 50% chance of being true
* or false, ignoring the original input value.
*
* @see MutationStrategy
*/
public class BooleanRandomizeMutation implements MutationStrategy<Boolean> {

    /**
    * Generates a random boolean value with 50% probability.
    *
    * @param value The original boolean value (ignored by this implementation)
    * @return A random boolean value (true or false with equal probability)
    */
    @Override
    public Boolean mutate(Boolean value) {
        return Math.random() > 0.5;
    }

    /**
    * Returns a descriptive name for this mutation strategy.
    *
    * @return The string "Random"
    */
    @Override
    public String getName() {
        return "Random";
    }

}