package com.tomtrotter.habitatsimulation.model.core;

import com.tomtrotter.habitatsimulation.model.util.Randomizer;
import com.tomtrotter.habitatsimulation.model.state.SimulatorState;

import java.util.Random;

/**
* The Disease class models a simple disease that can infect an animal in the simulation.
* It manages infection status, immunity, duration of illness, and the mortality rate associated with the disease.
*/
public class Disease {

    private boolean infected;
    private int daysInfected = 0;

    public final Random rand = Randomizer.getRandom();

    public Disease() {
        infected = false;
    }

    /**
    * Gets the number of days an infection lasts.
    *
    * @return The duration of the disease in days.
    */
    public int getDuration() {
        return SimulatorState.getInstance().getDuration();
    }

    /**
    * Sets the duration of the disease.
    *
    * @param duration Number of days the infection should last.
    */
    public void setDuration(int duration) {
        SimulatorState.getInstance().setDuration(duration);
    }

    /**
    * Checks if the animal is currently infected.
    *
    * @return true if the animal is infected, false otherwise.
    */
    public boolean isInfected() {
        return infected;
    }

    /**
    * Sets the infection status of the animal.
    *
    * @param infected true to mark the animal as infected, false to mark as healthy.
    */
    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    /**
    * Increments the number of days the animal has been infected.
    * This is used to determine recovery or death thresholds.
    */
    protected void incrementInfected() {
        daysInfected++;
    }

    /**
    * Attempts to infect the animal based on a given infection probability.
    * If a random value falls within the probability threshold, the animal becomes infected.
    *
    * @param diseaseProbability A value between 0.0 and 1.0 representing the chance of infection.
    */
    public void getInfection(double diseaseProbability) {
        if(rand.nextDouble() <= diseaseProbability) {
            setInfected(true);
        }
    }

    /**
    * Gets the number of days the animal has been infected.
    *
    * @return Number of days of current infection.
    */
    public int getDaysInfected() {
        return daysInfected;
    }

    /**
    * Gets the mortality rate of the disease.
    *
    * @return A value between 0.0 and 1.0 representing the probability of death due to the disease.
    */
    public double getMortalityRate() {
        return SimulatorState.getInstance().getMortalityRate();
    }

    /**
    * Sets the mortality rate of the disease.
    *
    * @param mortalityRate A value between 0.0 and 1.0 indicating death chance.
    */
    public void setMortalityRate(double mortalityRate) {
        SimulatorState.getInstance().setMortalityRate(mortalityRate);
    }

}
