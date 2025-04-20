package com.tomtrotter.habitatsimulation.model.state;

/**
* Singleton class that manages the simulation's global state,
* including settings related to plant and prey food values,
* disease mortality rate, and infection duration.
* This class ensures that only one instance of the simulation state exists.
*/

public final class SimulatorState {

    private static SimulatorState INSTANCE;

    private int plantFoodValue = 9;
    private int preyFoodValue = 9;

    private int duration = 5;
    private double mortalityRate = 0.3;

    private SimulatorState() {
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

}
