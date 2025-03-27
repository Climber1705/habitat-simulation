package com.tomtrotter.habitatsimulation.model;

import java.util.Random;

/**
* This class represents genetic information for an animal.
* It handles gene creation, mutation, and validation of attributes like breeding age, max age, etc.
*/

public class Genetics {

    public enum ATTRIBUTE {BREEDING_AGE, MAX_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE, DISEASE_PROBABILITY, METABOLISM}

    private final String gene;
    private int breedingAge, maxAge, maxLitterSize;
    private double breedingProbability, diseaseProbability, metabolism;

    private static final double MUTATION_PROBABILITY = 0.2;

    public final Random rand = Randomizer.getRandom();

    /**
    * Constructs a Genetics object with a specified gene.
    * If no gene is provided, a random gene is generated.
    *
    * @param gene: The genetic material as a string.
    */
    public Genetics(String gene) {
        this.gene = gene.isEmpty() ? createGene() : gene;
        setGenes();
    }

    /**
    * Generates a random genetic material for an animal.
    * Each characteristic is assigned a random value within valid ranges.
    *
    * @return A string representing random genetic material.
    */
    private String createGene() {
        int breedingAgeValue = rand.nextInt(12, 91);
        int maxAgeValue = rand.nextInt(10, 121);
        int breedingProbabilityValue = rand.nextInt(0, 51);
        int maxLitterSizeValue = rand.nextInt(1, 13);
        int diseaseProbabilityValue = rand.nextInt(0, 51);
        int metabolismValue = rand.nextInt(25, 101);
        return formatCharacterValue(breedingAgeValue, 2) +
                formatCharacterValue(maxAgeValue, 3) +
                formatCharacterValue(breedingProbabilityValue, 2) +
                formatCharacterValue(maxLitterSizeValue, 2) +
                formatCharacterValue(diseaseProbabilityValue, 2) +
                formatCharacterValue(metabolismValue, 3);
    }

    /**
    * Sets the animal's genetic attributes by parsing the gene string.
    * The gene string is divided into its components and assigned to corresponding attributes.
    */
    private void setGenes() {
        setBreedingAge(Integer.parseInt(gene.substring(0,2)));
        setMaxAge(Integer.parseInt(gene.substring(2,5)));
        setBreedingProbability(Double.parseDouble(gene.substring(5,7)) / 100);
        setMaxLitterSize(Integer.parseInt(gene.substring(7,9)));
        setDiseaseProbability(Double.parseDouble(gene.substring(9,11)) / 100);
        setMetabolism(Double.parseDouble(gene.substring(11,14)) / 100);
    }

    /**
    * Extracts a specific segment from the gene string based on the given attribute.
    *
    * @param attribute: The characteristic we want to extract.
    * @param gene: The genetic material of the animal.
    * @return The gene segment corresponding to the specified attribute.
    */
    public String getGeneSegment(ATTRIBUTE attribute, String gene) {
        if(gene.length() != 14) {
            throw new IllegalStateException("Unexpected gene length");
        }
        return switch (attribute) {
            case BREEDING_AGE -> gene.substring(0,2);
            case MAX_AGE -> gene.substring(2,5);
            case BREEDING_PROBABILITY -> gene.substring(5,7);
            case MAX_LITTER_SIZE -> gene.substring(7,9);
            case DISEASE_PROBABILITY -> gene.substring(9,11);
            case METABOLISM -> gene.substring(11,14);
        };
    }

    /**
    * Validates whether a mutation is within the allowed range for a specific attribute.
    *
    * @param attribute: The attribute to check (e.g., breeding age, max age).
    * @param changeBy: The value by which the characteristic is mutated (+1 or -1).
    * @param characteristicValue: The current value of the characteristic.
    * @return true if the mutation is valid, otherwise false.
    */
    public boolean isValid(ATTRIBUTE attribute, int changeBy, int characteristicValue) {
        return switch (attribute) {
            case BREEDING_AGE -> isValidBreedingAge(changeBy, characteristicValue);
            case MAX_AGE -> isValidMaxAge(changeBy, characteristicValue);
            case BREEDING_PROBABILITY -> isValidBreedingProbability(changeBy, characteristicValue);
            case MAX_LITTER_SIZE -> isValidMaxLitterSize(changeBy, characteristicValue);
            case DISEASE_PROBABILITY -> isValidDiseaseProbability(changeBy, characteristicValue);
            case METABOLISM -> isValidMetabolism(changeBy, characteristicValue);
        };
    }

    /**
    * Checks if the breeding age mutation is within the valid boundaries.
    *
    * @param changeBy: The amount to change the characteristic (+1 or -1).
    * @param characteristicValue: The current value of the characteristic.
    * @return true if the breeding age is valid, otherwise false.
    */
    private boolean isValidBreedingAge(int changeBy, int characteristicValue) {
        characteristicValue += changeBy;
        return characteristicValue >= 12 && characteristicValue <= 90;
    }

    /**
    * Checks if the max age mutation is within the valid boundaries.
    *
    * @param changeBy: The amount to change the characteristic (+1 or -1).
    * @param characteristicValue: The current value of the characteristic.
    * @return true if the max age is valid, otherwise false.
    */
    private boolean isValidMaxAge(int changeBy, int characteristicValue) {
        characteristicValue += changeBy;
        return characteristicValue >= 10 && characteristicValue <= 120;
    }

    /**
    * Checks if the breeding probability mutation is within the valid boundaries.
    *
    * @param changeBy: The amount to change the characteristic (+1 or -1).
    * @param characteristicValue: The current value of the characteristic.
    * @return true if the breeding probability is valid, otherwise false.
    */
    private boolean isValidBreedingProbability(int changeBy, int characteristicValue) {
        characteristicValue += changeBy;
        return characteristicValue > 0 && characteristicValue < 50;
    }

    /**
    * Checks if the max litter size mutation is within the valid boundaries.
    *
    * @param changeBy: The amount to change the characteristic (+1 or -1).
    * @param characteristicValue: The current value of the characteristic.
    * @return true if the max litter size is valid, otherwise false.
    */
    private boolean isValidMaxLitterSize(int changeBy, int characteristicValue) {
        characteristicValue += changeBy;
        return characteristicValue >= 1 && characteristicValue <= 12;
    }

    /**
    * Checks if the disease probability mutation is within the valid boundaries.
    *
    * @param changeBy: The amount to change the characteristic (+1 or -1).
    * @param characteristicValue: The current value of the characteristic.
    * @return true if the disease probability is valid, otherwise false.
    */
    private boolean isValidDiseaseProbability(int changeBy, int characteristicValue) {
        characteristicValue += changeBy;
        return characteristicValue > 0 && characteristicValue < 50;
    }

    /**
    * Checks if the metabolism mutation is within the valid boundaries.
    *
    * @param changeBy: The amount to change the characteristic (+1 or -1).
    * @param characteristicValue: The current value of the characteristic.
    * @return true if the metabolism is valid, otherwise false.
    */
    private boolean isValidMetabolism(int changeBy, int characteristicValue) {
        characteristicValue += changeBy;
        return characteristicValue >= 25 && characteristicValue <= 100;
    }

    /**
    * Formats a characteristic value to fit the genetic material structure.
    * The value is padded with leading zeros to match the required length.
    *
    * @param characteristicValue: The value of the characteristic.
    * @param lengthOfCharacteristic: The required length of the characteristic.
    * @return A string representing the formatted characteristic value.
    */
    public String formatCharacterValue(int characteristicValue, int lengthOfCharacteristic) {
        int difference = lengthOfCharacteristic - String.valueOf(characteristicValue).length();
        return "0".repeat(difference) + characteristicValue;
    }

    /**
    * Combines the genetic material of two parents to produce the shared gene for offspring.
    *
    * @param maleGene: The gene of the male parent.
    * @param femaleGene: The gene of the female parent.
    * @return The shared gene for all the children.
    */
    public String getSharedGene(String maleGene, String femaleGene) {
        return maleGene.substring(0,7) + femaleGene.substring(7,14);
    }

    /**
    * Introduces mutations to the parent's gene and returns the new mutated gene.
    * Each attribute has a certain probability of mutation, and if valid, the mutation is applied.
    *
    * @param parentsGene: The gene of the parent.
    * @return The new mutated gene.
    */
    protected String addMutations(String parentsGene) {
        StringBuilder newGene = new StringBuilder();
        for(ATTRIBUTE attribute : ATTRIBUTE.values()) {
            String characteristicGene = getGeneSegment(attribute, parentsGene);
            // There's no mutation therefore just add original characteristic gene.
            if(rand.nextDouble() >= MUTATION_PROBABILITY) {
                newGene.append(characteristicGene);
                continue;
            }
            int characteristicValue = Integer.parseInt(characteristicGene);
            int changeBy = rand.nextBoolean() ? -1 : 1;
            // If the mutation is valid therefore add it to new genetic material.
            if(isValid(attribute, changeBy, characteristicValue)) {
                characteristicValue += changeBy;
                String newCharacteristicGene = formatCharacterValue(characteristicValue, characteristicGene.length());
                newGene.append(newCharacteristicGene);
            }
            else {
                //If mutation isn't valid there add original characteristic gene.
                newGene.append(characteristicGene);
            }

        }
        return newGene.toString();
    }

    /**
    * Returns the breeding age of the animal.
    *
    * @return The breeding age of the animal.
    */
    public int getBreedingAge() {
        return breedingAge;
    }

    /**
    * Returns the maximum age of the animal.
    *
    * @return The maximum age of the animal.
    */
    public int getMaxAge() {
        return maxAge;
    }

    /**
    * Returns the maximum litter size of the animal.
    *
    * @return The maximum litter size of the animal.
    */
    public int getMaxLitterSize() {
        return maxLitterSize;
    }

    /**
    * Returns the breeding probability of the animal.
    *
    * @return The breeding probability of the animal.
    */
    public double getBreedingProbability() {
        return breedingProbability;
    }

    /**
    * Returns the disease probability of the animal.
    *
    * @return The disease probability of the animal.
    */
    public double getDiseaseProbability() {
        return diseaseProbability;
    }

    /**
    * Returns the metabolism rate of the animal.
    *
    * @return The metabolism rate of the animal.
    */
    public double getMetabolism() {
        return metabolism;
    }

    /**
    * Returns the genetic material (gene) of the animal as a string.
    *
    * @return The genetic material of the animal.
    */
    public String getGene() {
        return gene;
    }

    /**
    * Sets the breeding age of the animal.
    *
    * @param breedingAge: The new breeding age of the animal.
    */
    public void setBreedingAge(int breedingAge) {
        this.breedingAge = breedingAge;
    }

    /**
    * Sets the maximum age of the animal.
    *
    * @param maxAge: The new maximum age of the animal.
    */
    public void setMaxAge(int maxAge) { this.maxAge = maxAge; }

    /**
    * Sets the breeding probability of the animal.
    *
    * @param breedingProbability: The new breeding probability of the animal.
    */
    public void setBreedingProbability(double breedingProbability) { this.breedingProbability = breedingProbability; }

    /**
    * Sets the maximum litter size of the animal.
    *
    * @param maxLitterSize: The new maximum litter size of the animal.
    */
    public void setMaxLitterSize(int maxLitterSize) {
        this.maxLitterSize = maxLitterSize;
    }

    /**
    * Sets the disease probability of the animal.
    *
    * @param diseaseProbability: The new disease probability of the animal.
    */
    public void setDiseaseProbability(double diseaseProbability) { this.diseaseProbability = diseaseProbability; }

    /**
    * Sets the metabolism rate of the animal.
    *
    * @param metabolism: The new metabolism rate of the animal.
    */
    public void setMetabolism(double metabolism) {
        this.metabolism = metabolism;
    }

}
