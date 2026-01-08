package com.inf2328.energytycoon.model.building;

import java.util.Random;

public class Residence extends Building {

    private double energyNeed;
    private double satisfaction; // entre 0 et 100
    private final Random random = new Random();

    private static final double MIN_ENERGY_NEED = 5.0;
    private static final double MAX_ENERGY_NEED = 10.0;

    public Residence(double baseCost, double upgradeCost, int maxLevel) {
        super(baseCost, upgradeCost, maxLevel);
        this.energyNeed = generateEnergyNeed();
        this.satisfaction = 100.0;
    }

    private double generateEnergyNeed() {
        return MIN_ENERGY_NEED +
               (MAX_ENERGY_NEED - MIN_ENERGY_NEED) * random.nextDouble();
    }

    // récupération du besoin énergétique
    public double getEnergyNeed() {
        return energyNeed;
    }

    // récupération de la satisfaction
    public double getSatisfaction() {
        return satisfaction;
    }

    // Méthode de mise à jour de la satisfaction en fonction de l'énergie
    public void updateSatisfaction(double energyReceived) {
        if (energyReceived < energyNeed) {
            satisfaction -= 10; // malus
        } else {
            satisfaction += 2;  // récompence
        }

        // contrôle de la satisfaction
        if (satisfaction < 0) {
            satisfaction = 0;
        } else if (satisfaction > 100) {
            satisfaction = 100;
        }
    }

    @Override
    protected void onUpgrade() {
        energyNeed += generateEnergyNeed();
    }
}
