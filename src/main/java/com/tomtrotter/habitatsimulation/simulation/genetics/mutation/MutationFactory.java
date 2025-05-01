package com.tomtrotter.habitatsimulation.simulation.genetics.mutation;

/**
* A factory class that provides convenient methods for creating common mutation types.
* This class simplifies the creation of mutation types by encapsulating the instantiation
* of various mutation strategies and wrapping them in appropriate MutationType objects.
*/
public class MutationFactory {

    /**
    * Creates a mutation type that increments an integer value by a specified amount.
    *
    * @param increment The amount to add to the original integer value
    * @return A MutationType that applies an integer increment mutation
    */
    public static MutationType<Integer> intIncrement(int increment) {
        return new MutationType<>(new IntegerIncrementMutation(increment));
    }

    /**
    * Creates a mutation type that increments a double value by a specified amount.
    *
    * @param increment The amount to add to the original double value
    * @return A MutationType that applies a double increment mutation
    */
    public static MutationType<Double> doubleIncrement(double increment) {
        return new MutationType<>(new DoubleIncrementMutation(increment));
    }

    /**
    * Creates a mutation type that toggles a boolean value.
    *
    * @return A MutationType that applies a boolean toggle mutation
    */
    public static MutationType<Boolean> booleanToggle() {
        return new MutationType<>(new BooleanToggleMutation());
    }

    /**
    * Creates a mutation type that randomizes a boolean value.
    *
    * @return A MutationType that applies a boolean randomization mutation
    */
    public static MutationType<Boolean> booleanRandomize() {
        return new MutationType<>(new BooleanRandomizeMutation());
    }

}