package com.tomtrotter.habitatsimulation.simulation.genetics.mutation;

/**
* A mutation strategy that inverts a boolean value.
* This strategy implements the logical NOT operation on the input value,
* turning true to false and false to true.
*
* @see MutationStrategy
*/
public class BooleanToggleMutation implements MutationStrategy<Boolean> {

    /**
    * Inverts the given boolean value.
    *
    * @param value The boolean value to invert
    * @return The logical inverse of the input value
    */
    @Override
    public Boolean mutate(Boolean value) {
        return !value;
    }

    /**
     * Returns a descriptive name for this mutation strategy.
     *
     * @return The string "Toggle"
     */
    @Override
    public String getName() {
        return "Toggle";
    }

}