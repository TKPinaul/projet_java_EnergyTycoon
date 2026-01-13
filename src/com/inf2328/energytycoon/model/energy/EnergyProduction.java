package com.inf2328.energytycoon.model.energy;

/*
 * Class représentant la production d'énergie
 * 
 * @param type : type d'énergie
 * @param amount : quantité d'énergie
 */
public class EnergyProduction {

    private EnergyType type;
    private double amount;

    public EnergyProduction(EnergyType type, double amount) {
        this.type = type;
        this.amount = amount;
    }
    
    // Récupération du type d'énergie
    public EnergyType getType() {
        return type;
    }

    // Récupération de la production d'énergie
    public double getAmount() {
        return amount;
    }

    // Modification de la production d'énergie
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Augmentation de la production d'énergie
    public void increaseAmount(double factor) {
        amount *= factor;
    }

    // Diminution de la production d'énergie
    public void decreaseAmount(double factor) {
        amount /= factor;
    }

    @Override
    public String toString() {
        return type + ": " + String.format("%.2f", amount) + " units";
    } 
}
