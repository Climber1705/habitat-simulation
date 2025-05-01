package com.tomtrotter.habitatsimulation.simulation.genetics.mutation;

import java.util.function.Function;

/**
* A class that represents a specific type of mutation operation.
* MutationType serves as a wrapper around a MutationStrategy, providing a consistent
* interface for applying mutations and obtaining mutation metadata. It implements
* the Strategy design pattern by delegating the actual mutation logic to the
* encapsulated strategy object.
*
* @param <T> The type of value that this mutation can be applied to
*/
public class MutationType<T> {

    private final MutationStrategy<T> strategy;

    /**
    * Creates a new MutationType with the specified strategy.
    * This constructor is the preferred way to create a MutationType.
    *
    * @param strategy The mutation strategy that defines the mutation behavior
    */
    public MutationType(MutationStrategy<T> strategy) {
        this.strategy = strategy;
    }

    /**
    * Legacy constructor that creates a MutationType from a name and function.
    * This constructor adapts a Function to the MutationStrategy interface,
    * allowing for backward compatibility with code that uses function-based mutations.
    *
    * @param name The name of the mutation
    * @param mutationFunction The function that implements the mutation logic
    */
    public MutationType(String name, Function<T, T> mutationFunction) {
        this.strategy = new MutationStrategy<T>() {
            @Override
            public T mutate(T value) {
                return mutationFunction.apply(value);
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    /**
    * Gets the descriptive name of this mutation.
    * The name is obtained from the underlying strategy.
    *
    * @return The name of this mutation type
    */
    public String getName() {
        return strategy.getName();
    }

    /**
    * Applies this mutation to a value.
    * The mutation logic is delegated to the underlying strategy.
    *
    * @param value The value to mutate
    * @return The mutated value
    */
    public T apply(T value) {
        return strategy.mutate(value);
    }

    /**
    * Returns a string representation of this mutation type.
    * By default, this returns the name of the mutation.
    *
    * @return The name of this mutation type
    */
    @Override
    public String toString() {
        return strategy.getName();
    }

}