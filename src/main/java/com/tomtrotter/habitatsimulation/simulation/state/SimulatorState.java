// First, let's update the SimulatorState class to manage organism weights

package com.tomtrotter.habitatsimulation.simulation.state;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class that manages the simulation's global state,
 * including settings related to plant and prey food values,
 * disease mortality rate, infection duration, and organism weights.
 * This class ensures that only one instance of the simulation state exists.
 */
public class SimulatorState {

    private static SimulatorState INSTANCE;

    private int plantFoodValue = 9;
    private int preyFoodValue = 9;

    private int duration = 5;
    private double mortalityRate = 0.3;

    private double mutationProbability = 0.2;

    // Maps to store organism weights
    private final Map<String, Double> predatorWeights = new HashMap<>();
    private final Map<String, Double> preyWeights = new HashMap<>();

    private SimulatorState() {
        // Initialize default weights
        predatorWeights.put("Tiger", 50.0);
        predatorWeights.put("Leopard", 50.0);

        preyWeights.put("Deer", 34.0);
        preyWeights.put("Hare", 33.0);
        preyWeights.put("WildBoar", 33.0);
    }

    /**
     * Retrieves the single instance of SimulatorState.
     * If the instance doesn't exist, it creates one.
     *
     * @return The Singleton instance of SimulatorState.
     */
    public static SimulatorState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SimulatorState();
        }
        return INSTANCE;
    }

    /**
     * Gets the food value of plants.
     *
     * @return The current food value for plants.
     */
    public int getPlantFoodValue() {
        return plantFoodValue;
    }

    /**
     * Sets the food value of plants.
     *
     * @param plantFoodValue The new food value for plants.
     */
    public void setPlantFoodValue(int plantFoodValue) {
        this.plantFoodValue = plantFoodValue;
    }

    /**
     * Gets the food value of prey.
     *
     * @return The current food value for prey.
     */
    public int getPreyFoodValue() {
        return preyFoodValue;
    }

    /**
     * Sets the food value of prey.
     *
     * @param preyFoodValue The new food value for prey.
     */
    public void setPreyFoodValue(int preyFoodValue) {
        this.preyFoodValue = preyFoodValue;
    }

    /**
     * Gets the mortality rate of the disease.
     *
     * @return A value between 0.0 and 1.0 representing the probability of death due to the disease.
     */
    public double getMortalityRate() {
        return mortalityRate;
    }

    /**
     * Sets the mortality rate of the disease.
     *
     * @param mortalityRate A value between 0.0 and 1.0 indicating the death chance due to the disease.
     */
    public void setMortalityRate(double mortalityRate) {
        this.mortalityRate = mortalityRate;
    }

    /**
     * Gets the duration of the disease (in days).
     *
     * @return The number of days the disease lasts.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the disease.
     *
     * @param duration The number of days the disease should last.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the mutation probability for genetic attributes.
     *
     * @return A value representing the mutation probability.
     */
    public double getMutationProbability() {
        return mutationProbability;
    }

    /**
     * Sets the mutation probability for genetic attributes.
     *
     * @param mutationProbability The new mutation probability value.
     */
    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    /**
     * Gets the weight for a specific predator type.
     *
     * @param predatorType The type of predator.
     * @return The weight value for the specified predator type.
     */
    public double getPredatorWeight(String predatorType) {
        return predatorWeights.getOrDefault(predatorType, 0.0);
    }

    /**
     * Sets the weight for a specific predator type.
     *
     * @param predatorType The type of predator.
     * @param weight The new weight value for the predator type.
     */
    public void setPredatorWeight(String predatorType, double weight) {
        predatorWeights.put(predatorType, weight);
    }

    /**
     * Gets the weight for a specific prey type.
     *
     * @param preyType The type of prey.
     * @return The weight value for the specified prey type.
     */
    public double getPreyWeight(String preyType) {
        return preyWeights.getOrDefault(preyType, 0.0);
    }

    /**
     * Sets the weight for a specific prey type.
     *
     * @param preyType The type of prey.
     * @param weight The new weight value for the prey type.
     */
    public void setPreyWeight(String preyType, double weight) {
        preyWeights.put(preyType, weight);
    }

    /**
     * Gets all predator weights as a map.
     *
     * @return A map of predator types to their weights.
     */
    public Map<String, Double> getPredatorWeights() {
        return new HashMap<>(predatorWeights);
    }

    /**
     * Gets all prey weights as a map.
     *
     * @return A map of prey types to their weights.
     */
    public Map<String, Double> getPreyWeights() {
        return new HashMap<>(preyWeights);
    }

}