package com.inf2328.energytycoon.model.building;
import java.util.Random;

/*
 * Class représentant une résidence

 * @param energyNeed : besoin énergétique de la résidence
 * @param satisfaction : niveau de satisfaction des résidents (0 - 100)
 * @param MIN_ENERGY_NEED : niveau minimum autorisé
 * @param MAX_ENERGY_NEED : niveau maximum autorisé
*/
public class Residence extends Building {

    private double energyNeed;
    private double satisfaction; // entre 0 et 100
    private final Random random = new Random();

    private static final double MIN_ENERGY_NEED = 5.0;
    private static final double MAX_ENERGY_NEED = 10.0;

    // Constructeur
    public Residence(double baseCost, double upgradeCost, int maxLevel) {
        super(baseCost, upgradeCost, maxLevel);
        this.energyNeed = generateEnergyNeed();
        this.satisfaction = 100.0;
    }

    // Génération aléatoire du besoin énergétique
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

    // Augmentation du besoin énergétique au fil du temps
    public void increaseEnergyNeedOverTime() {
        energyNeed *= 1.01; // augmentation de 1% / jour
    }

    // Méthode de mise à jour de la satisfaction en fonction de l'énergie
    public void updateSatisfaction(double energyReceived) {
        if (energyReceived < energyNeed) {
            satisfaction -= 15; // malus
        } else {
            satisfaction += 2; // récompence
        }

        // contrôle de la satisfaction
        if (satisfaction < 0) {
            satisfaction = 0;
        } else if (satisfaction > 100) {
            satisfaction = 100;
        }
    }

    // Seuil d'insatisfaction
    public boolean isUnsatisfied() {
        return satisfaction < 40;
    }

    // Augmentation du besoin énergétique lors d'un upgrade de la résidence
    @Override
    protected void onUpgrade() {
        energyNeed *= 1.1;
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
