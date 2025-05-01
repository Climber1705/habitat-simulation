package com.tomtrotter.habitatsimulation.simulation.genetics.mutation;

/**
* A mutation strategy that modifies an integer value by adding a specified increment.
* This strategy can increase or decrease an integer value by a fixed amount,
* determined by the increment provided during construction.
*
* @see MutationStrategy
*/
public class IntegerIncrementMutation implements MutationStrategy<Integer> {
    private final int increment;
    private final String name;

    /**
    * Creates a new integer increment mutation with the specified change amount.
    *
    * @param increment The amount to add to the original value (can be positive or negative)
    */
    public IntegerIncrementMutation(int increment) {
        this.increment = increment;
        this.name = (increment >= 0 ? "+" : "") + increment;
    }

    /**
    * Adds the configured increment to the given integer value.
    *
    * @param value The original integer value to modify
    * @return The original value plus the increment
    */
    @Override
    public Integer mutate(Integer value) {
        return value + increment;
    }

    /**
    * Returns a descriptive name for this mutation strategy.
    * The name includes the increment with a sign prefix (+ or -).
    *
    * @return A string representing the increment (e.g., "+1" or "-5")
    */
    @Override
    public String getName() {
        return name;
    }

}