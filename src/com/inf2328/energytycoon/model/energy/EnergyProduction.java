package com.inf2328.energytycoon.model.energy;

/*
 * Class représentant la production d'énergie
 * 
 * @param type : type d'énergie
 * @param baseAmount : quantité d'énergie
 */
public class EnergyProduction {

    private EnergyType type;
    private double baseAmount;

    public EnergyProduction(EnergyType type, double baseAmount) {
        this.type = type;
        this.baseAmount = baseAmount;
    }
    
    // Récupération du type d'énergie
    public EnergyType getType() {
        return type;
    }

    // Récupération de la production d'énergie
    public double getBaseAmount() {
        return baseAmount;
    }

    // Modification de la production d'énergie
    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
    }

    // Augmentation de la production d'énergie
    public void increaseAmount(double factor) {
        baseAmount *= factor;
    }

    // Diminution de la production d'énergie
    public void decreaseAmount(double factor) {
        baseAmount /= factor;
    }

    @Override
    public String toString() {
        return type + ": " + String.format("%.2f", baseAmount) + " units";
    } 
}
