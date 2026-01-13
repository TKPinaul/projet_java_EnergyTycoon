package com.inf2328.energytycoon.model.building;

/*
 * Class représentant une centrale électrique
 
 * @param energyOutput : production d'énergie de la centrale
 * @param initialOutput : production initiale d'énergie
 */
public class PowerPlant extends Building {

    private double energyOutput;

    public PowerPlant(double baseCost, double upgradeCost, int maxLevel, double initialOutput) {
        super(baseCost, upgradeCost, maxLevel);
        this.energyOutput = initialOutput;
    }

    // Récupération de la production d'énergie
    public double getEnergyOutput() {
        return energyOutput;
    }

    // Augmentation de la production d'énergie
    @Override
    protected void onUpgrade() {
        energyOutput *= 1.2; // augmentation de 20%
    }

    // Représentation de la centrale électrique
    @Override
    public String toString() {
        return "PowerPlant{" +
                "energyOutput=" + energyOutput +
                ", level=" + level +
                ", maxLevel=" + maxLevel +
                ", baseCost=" + baseCost +
                ", upgradeCost=" + upgradeCost +
                '}';
    }
    
}
