package com.inf2328.energytycoon.model.building;
import java.util.Random;

/*
 * Class représentant une résidence

 * @param energyNeed : besoin énergétique de la résidence
 * @param satisfaction : niveau de satisfaction des résidents (0 - 100)
*/
public class Residence extends Building {

    private double energyNeed;
    private double satisfaction; // entre 0 et 100
    private final Random random = new Random();

    // Constructeur
    public Residence(double baseCost, double upgradeCost, int maxLevel) {
        super(baseCost, upgradeCost, maxLevel);
        this.energyNeed = generateEnergyNeedForLevel(level);
        this.satisfaction = 100.0;
    }

    // récupération du niveau maximum de besoin énergétique
    public double generateEnergyNeedForLevel(int level) {
        double min;
        double max;

        switch (level) {
            case 1 -> { min = 5; max = 10; }
            case 2 -> { min = 15; max = 35; }
            default -> {
                min = 20 * level;
                max = 30 * level;
            }
        }

        return min + (max - min) * random.nextDouble();
    }

    // récupération du besoin énergétique
    public double getEnergyNeed() {
        return energyNeed;
    }

    // récupération de la satisfaction
    public double getSatisfaction() {
        return satisfaction;
    }

    // Augmentation du besoin énergétique au fil du temps
    public void increaseEnergyNeedOverTime() {
        energyNeed *= 1.03; // augmentation de 3% / jour

        double maxAllowed = getMaxEnergyForLevel(level);
        energyNeed = Math.min(energyNeed, maxAllowed);
    }

    // Maximum DÉTERMINISTE par niveau
    private double getMaxEnergyForLevel(int level) {
        return switch (level) {
            case 1 -> 10;
            case 2 -> 35;
            default -> 30 * level;
        };
    }

    // Méthode de mise à jour de la satisfaction en fonction de l'énergie
    public void updateSatisfaction(double energyReceived) {
        if (energyReceived < energyNeed) {
            satisfaction -= 15; // malus
        } else {
            satisfaction += 2; // récompence
        }
        satisfaction = Math.max(0, Math.min(100, satisfaction)); // retourne
    }

    // Seuil d'insatisfaction
    public boolean isUnsatisfied() {
        return satisfaction < 40;
    }

    // Augmentation du besoin énergétique lors d'un upgrade de la résidence
    @Override
    protected void onUpgrade() {
        if (satisfaction < 40) return;
        energyNeed = generateEnergyNeedForLevel(level);
    }

    // Représentation de la résidence
    @Override
    public String toString() {
        return "Residence{" +
                "energyNeed=" + energyNeed +
                ", satisfaction=" + satisfaction +
                ", level=" + level +
                ", maxLevel=" + maxLevel +
                ", baseCost=" + baseCost +
                ", upgradeCost=" + upgradeCost +
                '}';
    }

}
