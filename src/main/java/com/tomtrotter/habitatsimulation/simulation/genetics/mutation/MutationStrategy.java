package com.tomtrotter.habitatsimulation.simulation.genetics.mutation;

/**
* A strategy interface that defines the contract for genetic mutation implementations.
* This interface follows the Strategy design pattern to allow for different
* mutation algorithms that can be selected at runtime.
*
* @param <T> The type of value that this strategy can mutate (e.g., Boolean, Integer, Double)
*/
public interface MutationStrategy<T> {

    /**
    * Applies this mutation strategy to transform a value.
    * Implementations will define specific mutation logic such as
    * toggling, incrementing, randomizing, etc.
    *
    * @param value The original value to mutate
    * @return The mutated value after applying the strategy
    */
    T mutate(T value);

    /**
    * Returns a descriptive name for this mutation strategy.
    * This name is used for display purposes and should concisely
    * describe the mutation effect.
    *
    * @return A string representing the name of this mutation strategy
    */
    String getName();

}