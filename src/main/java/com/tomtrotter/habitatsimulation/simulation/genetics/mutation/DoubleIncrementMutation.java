package com.tomtrotter.habitatsimulation.simulation.genetics.mutation;

/**
* A mutation strategy that modifies a double value by adding a specified increment.
* This strategy can increase or decrease a double value by a fixed amount,
* determined by the increment provided during construction.
*
* @see MutationStrategy
*/
public class DoubleIncrementMutation implements MutationStrategy<Double> {
    private final double increment;
    private final String name;

    /**
    * Creates a new double increment mutation with the specified change amount.
    *
    * @param increment The amount to add to the original value (can be positive or negative)
    */
    public DoubleIncrementMutation(double increment) {
        this.increment = increment;
        this.name = (increment >= 0 ? "+" : "") + increment;
    }

    /**
    * Adds the configured increment to the given double value.
    *
    * @param value The original double value to modify
    * @return The original value plus the increment
    */
    @Override
    public Double mutate(Double value) {
        return value + increment;
    }

    /**
    * Returns a descriptive name for this mutation strategy.
    * The name includes the increment with a sign prefix (+ or -).
    *
    * @return A string representing the increment (e.g., "+0.1" or "-0.5")
    */
    @Override
    public String getName() {
        return name;
    }

}