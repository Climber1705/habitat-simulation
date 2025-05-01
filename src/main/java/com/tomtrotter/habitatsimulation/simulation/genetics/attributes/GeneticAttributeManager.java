package com.tomtrotter.habitatsimulation.simulation.genetics.attributes;

import com.tomtrotter.habitatsimulation.simulation.genetics.builder.GeneticsBuilder;
import com.tomtrotter.habitatsimulation.simulation.genetics.mutation.MutationFactory;
import com.tomtrotter.habitatsimulation.simulation.genetics.mutation.MutationType;

import java.util.*;

/**
* Singleton class responsible for managing all genetic attribute definitions used in the simulation.
* This includes defining the properties of each attribute (type, range, mutation types, etc.),
* registering new attributes, toggling attribute activation, and providing access to definitions.
* <p>
* It acts as the central configuration point for the genetic system and is used globally.
*/
public class GeneticAttributeManager {

    private static GeneticAttributeManager instance;

    private final Map<Attributes, AttributeDefinition<?>> attributeDefinitions = new HashMap<>();
    private final Map<Attributes, Boolean> activeAttributes = new HashMap<>();

    /**
    * Private constructor to enforce singleton usage.
    * Initializes the manager with default attribute definitions and marks all as active.
    */
    private GeneticAttributeManager() {
        initializeDefaultAttributes();
    }

    /**
    * Retrieves the singleton instance of the manager.
    *
    * @return The singleton instance of {@code GeneticAttributeManager}.
    */
    public static synchronized GeneticAttributeManager getInstance() {
        if (instance == null) {
            instance = new GeneticAttributeManager();
        }
        return instance;
    }

    /**
    * Initializes a default set of core genetic attributes with predefined mutation types,
    * validation logic, ranges, and default values.
    */
    private void initializeDefaultAttributes() {
        // BREEDING_AGE
        registerAttribute(
                new GeneticsBuilder<>(Attributes.BREEDING_AGE, Integer.class)
                        .addMutationType(MutationFactory.intIncrement(1))
                        .addMutationType(MutationFactory.intIncrement(-1))
                        .setValidator((value, mutation) -> {
                            int newValue = mutation.apply(value);
                            return newValue >= 12 && newValue <= 90;
                        })
                        .setRange(12, 90)
                        .setDefaultValue(20)
                        .build()
        );
        // MAX_AGE
        registerAttribute(
                new GeneticsBuilder<>(Attributes.MAX_AGE, Integer.class)
                        .addMutationType(MutationFactory.intIncrement(1))
                        .addMutationType(MutationFactory.intIncrement(-1))
                        .setValidator((value, mutation) -> {
                            int newValue = mutation.apply(value);
                            return newValue >= 10 && newValue <= 120;
                        })
                        .setRange(10, 120)
                        .setDefaultValue(60)
                        .build()
        );
        // BREEDING_PROBABILITY
        registerAttribute(
                new GeneticsBuilder<>(Attributes.BREEDING_PROBABILITY, Double.class)
                        .addMutationType(MutationFactory.doubleIncrement(0.01))
                        .addMutationType(MutationFactory.doubleIncrement(-0.01))
                        .setValidator((value, mutation) -> {
                            double newValue = mutation.apply(value);
                            return newValue >= 0 && newValue <= 1.0;
                        })
                        .setRange(0.0, 1.0)
                        .setDefaultValue(0.2)
                        .build()
        );
        // MAX_LITTER_SIZE
        registerAttribute(
                new GeneticsBuilder<>(Attributes.MAX_LITTER_SIZE, Integer.class)
                        .addMutationType(MutationFactory.intIncrement(1))
                        .addMutationType(MutationFactory.intIncrement(-1))
                        .setValidator((value, mutation) -> {
                            int newValue = mutation.apply(value);
                            return newValue >= 1 && newValue <= 12;
                        })
                        .setRange(1, 12)
                        .setDefaultValue(4)
                        .build()
        );
        // DISEASE_PROBABILITY
        registerAttribute(
                new GeneticsBuilder<>(Attributes.DISEASE_PROBABILITY, Double.class)
                        .addMutationType(MutationFactory.doubleIncrement(0.01))
                        .addMutationType(MutationFactory.doubleIncrement(-0.01))
                        .setValidator((value, mutation) -> {
                            double newValue = mutation.apply(value);
                            return newValue >= 0 && newValue <= 1.0;
                        })
                        .setRange(0.0, 1.0)
                        .setDefaultValue(0.1)
                        .build()
        );
        // METABOLISM
        registerAttribute(
                new GeneticsBuilder<>(Attributes.METABOLISM, Double.class)
                        .addMutationType(MutationFactory.doubleIncrement(0.01))
                        .addMutationType(MutationFactory.doubleIncrement(-0.01))
                        .setValidator((value, mutation) -> {
                            double newValue = mutation.apply(value);
                            return newValue >= 0.25 && newValue <= 1.0;
                        })
                        .setRange(0.25, 1.0)
                        .setDefaultValue(0.5)
                        .build()
        );
        for (Attributes attr : Attributes.values()) {
            activeAttributes.put(attr, true);
        }
    }

    /**
    * Registers a new genetic attribute definition.
    *
    * @param definition The attribute definition to register.
    * @param <T>        The type of the attribute.
    */
    public <T> void registerAttribute(AttributeDefinition<T> definition) {
        attributeDefinitions.put(definition.getAttribute(), definition);
        activeAttributes.putIfAbsent(definition.getAttribute(), false);
    }

    /**
    * Enables or disables a specific attribute for use in the simulation.
    *
    * @param attribute The attribute to activate or deactivate.
    * @param active    True to activate, false to deactivate.
    */
    public void setAttributeActive(Attributes attribute, boolean active) {
        if (!attributeDefinitions.containsKey(attribute)) {
            throw new IllegalArgumentException("Unknown attribute: " + attribute);
        }
        activeAttributes.put(attribute, active);
    }

    /**
    * Checks whether a specific attribute is currently active.
    *
    * @param attribute The attribute to check.
    * @return True if the attribute is active, false otherwise.
    */
    public boolean isAttributeActive(Attributes attribute) {
        return activeAttributes.getOrDefault(attribute, false);
    }

    /**
    * Returns a set of all currently active attributes.
    *
    * @return Set of active attributes.
    */
    public Set<Attributes> getActiveAttributes() {
        Set<Attributes> active = new HashSet<>();
        for (Map.Entry<Attributes, Boolean> entry : activeAttributes.entrySet()) {
            if (entry.getValue()) {
                active.add(entry.getKey());
            }
        }
        return active;
    }

    /**
    * Returns a read-only view of all attribute definitions.
    *
    * @return Map of attributes to their definitions.
    */
    public Map<Attributes, AttributeDefinition<?>> getAttributeDefinitions() {
        return Collections.unmodifiableMap(attributeDefinitions);
    }

    /**
    * Retrieves the definition for a specific attribute.
    *
    * @param attribute The attribute whose definition is requested.
    * @param <T>       The type of the attribute.
    * @return The attribute definition for the given attribute.
    */
    @SuppressWarnings("unchecked")
    public <T> AttributeDefinition<T> getAttributeDefinition(Attributes attribute) {
        return (AttributeDefinition<T>) attributeDefinitions.get(attribute);
    }

    /**
    * Adds a mutation type to an existing attribute.
    *
    * @param attribute The attribute to which the mutation should be added.
    * @param mutation  The mutation logic to add.
    * @param <T>       The type of the attribute.
    * @throws IllegalArgumentException If the attribute is not registered.
    */
    public <T> void addMutationType(Attributes attribute, MutationType<T> mutation) {
        @SuppressWarnings("unchecked")
        AttributeDefinition<T> definition = (AttributeDefinition<T>) attributeDefinitions.get(attribute);
        if (definition == null) {
            throw new IllegalArgumentException("Unknown attribute: " + attribute);
        }
        definition.getPossibleMutations().add(mutation);
    }

}
