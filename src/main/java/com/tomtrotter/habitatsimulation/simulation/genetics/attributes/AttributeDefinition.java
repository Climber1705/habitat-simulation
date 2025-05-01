package com.tomtrotter.habitatsimulation.simulation.genetics.attributes;

import com.tomtrotter.habitatsimulation.simulation.genetics.mutation.MutationType;

import java.util.List;
import java.util.function.BiFunction;

/**
* Represents the definition of a genetic attribute used in the simulation.
* This class encapsulates metadata about an attribute, such as its type,
* valid range, default value, mutation behavior, and validation logic.
*
* @param <T> The data type of the attribute (e.g., Integer, Double).
*/
public class AttributeDefinition<T> {

    private final Attributes attribute;
    private final Class<T> type;
    private final List<MutationType<T>> possibleMutations;
    private final BiFunction<T, MutationType<T>, Boolean> validator;
    private final T minValue;
    private final T maxValue;
    private final T defaultValue;
    private final double mutationProbability;

    /**
    * Constructs a new attribute definition with all necessary configuration values.
    *
    * @param attribute           The enum constant representing this attribute.
    * @param type                The class type of the attribute (e.g., Integer.class).
    * @param possibleMutations  List of mutation strategies that can be applied to this attribute.
    * @param validator           A function that validates a mutated value against the rules.
    * @param minValue            Minimum allowed value for this attribute.
    * @param maxValue            Maximum allowed value for this attribute.
    * @param defaultValue        Default value assigned to this attribute when not otherwise specified.
    * @param mutationProbability Probability that this attribute will mutate (0.0 to 1.0).
    */
    public AttributeDefinition(
            Attributes attribute,
            Class<T> type,
            List<MutationType<T>> possibleMutations,
            BiFunction<T, MutationType<T>, Boolean> validator,
            T minValue,
            T maxValue,
            T defaultValue,
            double mutationProbability) {
        this.attribute = attribute;
        this.type = type;
        this.possibleMutations = possibleMutations;
        this.validator = validator;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        this.mutationProbability = mutationProbability;
    }

    /**
    * @return The associated attribute enum constant.
    */
    public Attributes getAttribute() {
        return attribute;
    }

    /**
    * @return The class type of the attribute.
    */
    public Class<T> getType() {
        return type;
    }

    /**
    * @return A list of mutation strategies applicable to this attribute.
    */
    public List<MutationType<T>> getPossibleMutations() {
        return possibleMutations;
    }

    /**
    * @return A function used to validate potential mutations of the attribute.
    */
    public BiFunction<T, MutationType<T>, Boolean> getValidator() {
        return validator;
    }

    /**
    * @return The minimum valid value for the attribute.
    */
    public T getMinValue() {
        return minValue;
    }

    /**
    * @return The maximum valid value for the attribute.
    */
    public T getMaxValue() {
        return maxValue;
    }

    /**
    * @return The default value assigned to this attribute.
    */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
    * @return The probability that this attribute mutates during genetic evolution.
    */
    public double getMutationProbability() {
        return mutationProbability;
    }

}
