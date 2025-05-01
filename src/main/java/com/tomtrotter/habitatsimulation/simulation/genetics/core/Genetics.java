package com.tomtrotter.habitatsimulation.simulation.genetics.core;

import com.tomtrotter.habitatsimulation.simulation.genetics.attributes.AttributeDefinition;
import com.tomtrotter.habitatsimulation.simulation.genetics.attributes.Attributes;
import com.tomtrotter.habitatsimulation.simulation.genetics.attributes.GeneticAttributeManager;
import com.tomtrotter.habitatsimulation.simulation.genetics.mutation.MutationType;
import com.tomtrotter.habitatsimulation.util.Randomizer;

import java.util.*;

/**
* Represents the genetic blueprint of an entity.
* Supports random initialization, mutation, inheritance (breeding), and validation
* using attribute definitions from the global {@link GeneticAttributeManager}.
*/
public class Genetics {

    private final Map<Attributes, Object> attributeValues = new HashMap<>();

    private final Random rand = Randomizer.getRandom();

    private final GeneticAttributeManager attributeManager = GeneticAttributeManager.getInstance();

    /**
    * Constructs a new Genetics object by copying values from another instance.
    * Only active attributes are copied.
    *
    * @param genetics The genetics instance to copy from.
    */
    public Genetics(Genetics genetics) {
        for (Attributes attr : Attributes.values()) {
            if (attributeManager.isAttributeActive(attr) && genetics.hasAttribute(attr)) {
                attributeValues.put(attr, genetics.getAttributeValue(attr));
            }
        }
    }

    /**
    * Constructs a new Genetics object with randomly generated attribute values.
    * Only attributes marked as active will be initialized.
    */
    public Genetics() {
        generateRandomAttributes();
    }

    /**
    * Randomly generates valid values for all active attributes,
    * based on their type and value range.
    */
    private void generateRandomAttributes() {
        for (Attributes attr : Attributes.values()) {
            if (attributeManager.isAttributeActive(attr)) {
                generateRandomAttribute(attr);
            }
        }
    }

    /**
    * Generates a valid random value for a specific attribute,
    * based on its defined type (Integer, Double, Boolean).
    *
    * @param attribute The attribute to generate.
    * @param <T>       The attribute type.
    */
    @SuppressWarnings("unchecked")
    private <T> void generateRandomAttribute(Attributes attribute) {
        AttributeDefinition<T> definition = attributeManager.getAttributeDefinition(attribute);
        if (definition == null) {
            return;
        }

        T value;
        Class<T> type = definition.getType();

        if (type == Integer.class) {
            AttributeDefinition<Integer> intDef = (AttributeDefinition<Integer>) definition;
            int minValue = intDef.getMinValue();
            int maxValue = intDef.getMaxValue();
            value = (T) Integer.valueOf(rand.nextInt(minValue, maxValue + 1));
        } else if (type == Double.class) {
            AttributeDefinition<Double> doubleDef = (AttributeDefinition<Double>) definition;
            double minValue = doubleDef.getMinValue();
            double maxValue = doubleDef.getMaxValue();
            value = (T) Double.valueOf(minValue + (maxValue - minValue) * rand.nextDouble());
        } else if (type == Boolean.class) {
            value = (T) Boolean.valueOf(rand.nextBoolean());
        } else {
            throw new UnsupportedOperationException("Unsupported attribute type: " + type.getName());
        }

        attributeValues.put(attribute, value);
    }

    /**
    * Checks if this instance contains a value for the specified attribute.
    *
    * @param attribute The attribute to check.
    * @return true if a value is present; false otherwise.
    */
    public boolean hasAttribute(Attributes attribute) {
        return attributeValues.containsKey(attribute);
    }

    /**
    * Retrieves the value of a given attribute. If no value is set,
    * returns the default value defined in its AttributeDefinition.
    *
    * @param attribute The attribute to retrieve.
    * @param <T>       The expected type of the attribute.
    * @return The attribute's value.
    */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(Attributes attribute) {
        if (!attributeValues.containsKey(attribute)) {
            AttributeDefinition<T> definition = attributeManager.getAttributeDefinition(attribute);
            if (definition != null) {
                return definition.getDefaultValue();
            }
            throw new IllegalStateException("Attribute is not initialized: " + attribute);
        }

        return (T) attributeValues.get(attribute);
    }

    /**
    * Retrieves the internal value of an attribute without default fallback.
    * Used internally for mutation and breeding logic.
    *
    * @param attribute The attribute to retrieve.
    * @param <T>       The expected type of the attribute.
    * @return The raw value or null if unset.
    */
    @SuppressWarnings("unchecked")
    private <T> T getAttributeValue(Attributes attribute) {
        return (T) attributeValues.get(attribute);
    }

    /**
    * Assigns a value to an attribute after validating:
    * - That the attribute is active
    * - That the value matches the expected type
    * - That numeric values fall within the defined range
    *
    * @param attribute The attribute to set.
    * @param value     The new value.
    * @param <T>       The expected type.
    * @throws IllegalStateException    If the attribute is inactive or missing a definition.
    * @throws IllegalArgumentException If the value type is incorrect or out of bounds.
    */
    public <T> void setAttribute(Attributes attribute, T value) {
        if (!attributeManager.isAttributeActive(attribute)) {
            throw new IllegalStateException("Cannot set inactive attribute: " + attribute);
        }

        AttributeDefinition<T> definition = attributeManager.getAttributeDefinition(attribute);
        if (definition == null) {
            throw new IllegalStateException("No definition found for attribute: " + attribute);
        }

        Class<T> expectedType = definition.getType();
        if (value != null && !expectedType.isInstance(value)) {
            throw new IllegalArgumentException("Invalid type for attribute " + attribute +
                    ". Expected: " + expectedType.getSimpleName() +
                    ", Got: " + value.getClass().getSimpleName()
            );
        }

        if (expectedType == Integer.class && value != null) {
            Integer intValue = (Integer) value;
            Integer min = (Integer) definition.getMinValue();
            Integer max = (Integer) definition.getMaxValue();

            if ((min != null && intValue < min) || (max != null && intValue > max)) {
                throw new IllegalArgumentException(
                        "Value " + intValue + " for attribute " + attribute +
                                " is outside allowed range [" + min + ", " + max + "]"
                );
            }
        } else if (expectedType == Double.class && value != null) {
            Double doubleValue = (Double) value;
            Double min = (Double) definition.getMinValue();
            Double max = (Double) definition.getMaxValue();

            if ((min != null && doubleValue < min) || (max != null && doubleValue > max)) {
                throw new IllegalArgumentException(
                        "Value " + doubleValue + " for attribute " + attribute +
                                " is outside allowed range [" + min + ", " + max + "]"
                );
            }
        }

        attributeValues.put(attribute, value);
    }

    /**
    * Attempts to apply mutations to all active attributes in this instance,
    * based on each attribute's defined mutation probability.
    *
    * @return This mutated Genetics instance.
    */
    public Genetics mutate() {
        for (Attributes attr : attributeManager.getActiveAttributes()) {
            if (hasAttribute(attr) && attributeManager.isAttributeActive(attr)) {
                AttributeDefinition<?> definition = attributeManager.getAttributeDefinition(attr);
                if (rand.nextDouble() < definition.getMutationProbability()) {
                    applyMutation(attr, definition);
                }
            }
        }
        return this;
    }

    /**
    * Applies a random mutation to a single attribute, if:
    * - A mutation is available
    * - The current value is valid
    * - The validator allows the mutation
    *
    * @param attribute   The attribute to mutate.
    * @param definition  Its definition, including type and validator.
    * @param <T>         The attribute type.
    */
    @SuppressWarnings("unchecked")
    private <T> void applyMutation(Attributes attribute, AttributeDefinition<?> definition) {
        AttributeDefinition<T> typedDef = (AttributeDefinition<T>) definition;

        T currentValue = getAttributeValue(attribute);
        if (currentValue == null) {
            return;
        }

        List<MutationType<T>> mutations = typedDef.getPossibleMutations();
        if (mutations.isEmpty()) {
            return;
        }

        MutationType<T> mutation = mutations.get(rand.nextInt(mutations.size()));

        if (typedDef.getValidator().apply(currentValue, mutation)) {
            T newValue = mutation.apply(currentValue);
            attributeValues.put(attribute, newValue);
        }
    }

    /**
    * Breeds two Genetics instances to create an offspring with mixed attributes.
    * Each attribute is inherited randomly from either parent.
    *
    * @param parentA The first parent.
    * @param parentB The second parent.
    * @return A new Genetics instance representing the offspring.
    * @throws IllegalArgumentException If either parent is null.
    */
    public static Genetics breed(Genetics parentA, Genetics parentB) {
        if (parentA == null || parentB == null) {
            throw new IllegalArgumentException("Both parents must be non-null");
        }

        Genetics offspring = new Genetics();
        offspring.attributeValues.clear();

        GeneticAttributeManager attributeManager = GeneticAttributeManager.getInstance();
        Random rand = Randomizer.getRandom();

        for (Attributes attr : attributeManager.getActiveAttributes()) {
            if (parentA.hasAttribute(attr) && parentB.hasAttribute(attr)) {
                Genetics selectedParent = rand.nextBoolean() ? parentA : parentB;

                offspring.attributeValues.put(attr, selectedParent.getAttributeValue(attr));
            } else if (parentA.hasAttribute(attr)) {
                offspring.attributeValues.put(attr, parentA.getAttributeValue(attr));
            } else if (parentB.hasAttribute(attr)) {
                offspring.attributeValues.put(attr, parentB.getAttributeValue(attr));
            }
        }

        return offspring;
    }

    /**
    * Breeds two Genetics instances using advanced options:
    * - Parent bias controls inheritance likelihood (0.0 to 1.0)
    * - Blending averages numeric values (Integer, Double) if enabled
    *
    * @param parentA        First parent.
    * @param parentB        Second parent.
    * @param parentABias    Weight towards parentA (e.g. 0.7 = 70% from A).
    * @param blendNumerics  Whether to average numeric values.
    * @return A new Genetics instance with blended/inherited traits.
    * @throws IllegalArgumentException If bias is outside 0.0–1.0 or parents are null.
    */
    public static Genetics breedAdvanced(Genetics parentA, Genetics parentB, double parentABias, boolean blendNumerics) {
        if (parentA == null || parentB == null) {
            throw new IllegalArgumentException("Both parents must be non-null");
        }

        if (parentABias < 0.0 || parentABias > 1.0) {
            throw new IllegalArgumentException("Parent bias must be between 0.0 and 1.0");
        }

        Genetics offspring = new Genetics();
        offspring.attributeValues.clear();

        GeneticAttributeManager attributeManager = GeneticAttributeManager.getInstance();
        Random rand = Randomizer.getRandom();

        for (Attributes attr : attributeManager.getActiveAttributes()) {
            if (!parentA.hasAttribute(attr) && !parentB.hasAttribute(attr)) {
                continue;
            }

            if (!parentA.hasAttribute(attr)) {
                offspring.attributeValues.put(attr, parentB.getAttributeValue(attr));
                continue;
            }
            if (!parentB.hasAttribute(attr)) {
                offspring.attributeValues.put(attr, parentA.getAttributeValue(attr));
                continue;
            }

            Object valueA = parentA.getAttributeValue(attr);
            Object valueB = parentB.getAttributeValue(attr);

            AttributeDefinition<?> definition = attributeManager.getAttributeDefinition(attr);
            if (definition == null) {
                continue;
            }

            Class<?> type = definition.getType();

            if (blendNumerics && (type == Integer.class || type == Double.class)) {
                if (type == Integer.class) {
                    int intA = (Integer) valueA;
                    int intB = (Integer) valueB;
                    int blendedValue = (int) Math.round(intA * parentABias + intB * (1.0 - parentABias));
                    offspring.attributeValues.put(attr, blendedValue);
                } else {
                    double doubleA = (Double) valueA;
                    double doubleB = (Double) valueB;
                    double blendedValue = doubleA * parentABias + doubleB * (1.0 - parentABias);
                    offspring.attributeValues.put(attr, blendedValue);
                }
            } else {
                Genetics selectedParent = (rand.nextDouble() < parentABias) ? parentA : parentB;
                offspring.attributeValues.put(attr, selectedParent.getAttributeValue(attr));
            }
        }

        return offspring;
    }

    /**
    * Returns the value of the MAX_AGE attribute.
    *
    * @return Maximum age.
    */
    public int getBreedingAge() {
        return getAttribute(Attributes.BREEDING_AGE);
    }

    /**
    * Returns the value of the BREEDING_PROBABILITY attribute.
    *
    * @return Breeding probability.
    */
    public int getMaxAge() {
        return getAttribute(Attributes.MAX_AGE);
    }

    /**
    * Returns the value of the MAX_LITTER_SIZE attribute.
    *
    * @return Maximum number of offspring per birth.
    */
    public double getBreedingProbability() {
        return getAttribute(Attributes.BREEDING_PROBABILITY);
    }

    /**
    * Returns the value of the DISEASE_PROBABILITY attribute.
    *
    * @return Chance of disease.
    */
    public int getMaxLitterSize() {
        return getAttribute(Attributes.MAX_LITTER_SIZE);
    }

    /**
    * Returns the value of the DISEASE_PROBABILITY attribute.
    *
    * @return Chance of disease.
    */
    public double getDiseaseProbability() {
        return getAttribute(Attributes.DISEASE_PROBABILITY);
    }

    /**
    * Returns the value of the METABOLISM attribute.
    *
    * @return Metabolism rate.
    */
    public double getMetabolism() {
        return getAttribute(Attributes.METABOLISM);
    }

    /**
    * Creates a deep copy of this Genetics instance,
    * preserving values only for active attributes.
    *
    * @return A new, cloned Genetics object.
    */
    public Genetics copy() {
        return new Genetics(this);
    }

}