package com.tomtrotter.habitatsimulation.simulation.genetics.builder;

import com.tomtrotter.habitatsimulation.simulation.genetics.attributes.AttributeDefinition;
import com.tomtrotter.habitatsimulation.simulation.genetics.attributes.Attributes;
import com.tomtrotter.habitatsimulation.simulation.genetics.mutation.MutationType;
import com.tomtrotter.habitatsimulation.simulation.state.SimulatorState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
* A builder class used to construct AttributeDefinition objects with fluent configuration.
* This helps simplify the creation of complex genetic attribute definitions used in the simulation.
*
* @param <T> The data type of the attribute being built (e.g., Integer, Double).
*/
public class GeneticsBuilder<T> {
    private final Attributes attribute;
    private final Class<T> type;
    private final List<MutationType<T>> possibleMutations = new ArrayList<>();
    private BiFunction<T, MutationType<T>, Boolean> validator;
    private T minValue;
    private T maxValue;
    private T defaultValue;
    private double mutationProbability = SimulatorState.getInstance().getMutationProbability();

    /**
    * Constructs a new GeneticsBuilder for a specific attribute and type.
    *
    * @param attribute The genetic attribute being defined.
    * @param type      The Java type of the attribute (e.g., Integer.class).
    */
    public GeneticsBuilder(Attributes attribute, Class<T> type) {
        this.attribute = attribute;
        this.type = type;
    }

    /**
    * Adds a possible mutation type to this attribute.
    *
    * @param mutation A MutationType that defines how this attribute can mutate.
    * @return This builder instance for method chaining.
    */
    public GeneticsBuilder<T> addMutationType(MutationType<T> mutation) {
        this.possibleMutations.add(mutation);
        return this;
    }

    /**
    * Sets the validation function that checks whether a mutation is valid for a given value.
    *
    * @param validator A BiFunction that takes the current value and a mutation and returns true if valid.
    * @return This builder instance for method chaining.
    */
    public GeneticsBuilder<T> setValidator(BiFunction<T, MutationType<T>, Boolean> validator) {
        this.validator = validator;
        return this;
    }

    /**
    * Defines the allowed minimum and maximum values for the attribute.
    *
    * @param minValue The minimum permissible value.
    * @param maxValue The maximum permissible value.
    * @return This builder instance for method chaining.
    */
    public GeneticsBuilder<T> setRange(T minValue, T maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        return this;
    }

    /**
    * Sets the default value for this attribute.
    *
    * @param defaultValue The default value.
    * @return This builder instance for method chaining.
    */
    public GeneticsBuilder<T> setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
    * Sets the mutation probability for this attribute.
    *
    * @param probability A value between 0 and 1 representing the mutation chance.
    * @return This builder instance for method chaining.
    */
    public GeneticsBuilder<T> setMutationProbability(double probability) {
        this.mutationProbability = probability;
        return this;
    }

    /**
    * Builds and returns the final AttributeDefinition object based on the configuration.
    *
    * @return The constructed AttributeDefinition instance.
    */
    public AttributeDefinition<T> build() {
        return new AttributeDefinition<>(
                attribute, type, possibleMutations, validator, minValue, maxValue,
                defaultValue, mutationProbability
        );
    }

}
