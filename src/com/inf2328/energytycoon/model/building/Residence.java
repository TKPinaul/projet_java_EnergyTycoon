package com.inf2328.energytycoon.model.building;

import java.util.Random;

/*
 * Class représentant une résidence
 * 
 * @param energyNeed : besoin énergétique de la résidence
 * @param satisfaction : niveau de satisfaction des résidents (0 - 100)
 */
public class Residence extends Building {

    private ResidenceType type;
    private double energyNeed;
    private double satisfaction; // entre 0 et 100
    private final Random random = new Random();

    // Constructeur avec niveau initial
    public Residence(int initialLevel) {
        super(ResidenceType.fromLevel(initialLevel).getCost(), 0, ResidenceType.SKYSCRAPER.getLevel(), initialLevel);

        this.type = ResidenceType.fromLevel(initialLevel);

        this.energyNeed = type.getEnergyConsumption() * (0.9 + random.nextDouble() * 0.2);
        this.satisfaction = 100.0;
    }

    // Constructeur par défaut (niveau 1)
    public Residence() {
        this(1);
    }

    // récupération du besoin énergétique
    public double getEnergyNeed() {
        return energyNeed;
    }

    // récupération de la satisfaction
    public double getSatisfaction() {
        return satisfaction;
    }

    // récupération du type de résidence
    public ResidenceType getType() {
        return type;
    }

    // Augmentation du besoin énergétique au fil du temps
    public void increaseEnergyNeedOverTime() {
        energyNeed *= 1.01; // augmentation plus légère (1% / jour) pour ne pas être injuste

        // Plafond basé sur le type suivant (ou x1.5 pour le dernier)
        double maxAllowed = type.getEnergyConsumption() * 2.0;
        energyNeed = Math.min(energyNeed, maxAllowed);
    }

    // Méthode de mise à jour de la satisfaction en fonction de l'énergie
    public void updateSatisfaction(double energyReceived) {
        double ratio = 0;
        if (energyNeed > 0) {
            ratio = energyReceived / energyNeed;
        } else {
            ratio = 1;
        }

        if (ratio >= 1) {
            satisfaction += 1; // bonus faible, difficile à remonter
        } else {
            satisfaction -= (1 - ratio) * 30; // malus
        }
        satisfaction = Math.max(0, Math.min(100, satisfaction)); // retourne
    }

    // Seuil d'insatisfaction
    public boolean isUnsatisfied() {
        return satisfaction < 40;
    }

    // Surcharge du coût d'amélioration
    @Override
    public double getUpgradeCost() {
        
        // Si le niveau est au max, on ne peut plus améliorer
        if (level >= maxLevel)
            return 0;
        ResidenceType next = ResidenceType.fromLevel(level + 1);

        return next.getCost() - type.getCost();
    }

    // Surcharge pour obtenir le coût TOTAL d'un niveau (pour achat direct)
    @Override
    public double getPriceForLevel(int targetLevel) {
        return ResidenceType.fromLevel(targetLevel).getCost();
    }

    // Surcharge du nombre de jours requis pour l'amélioration
    @Override
    protected int getRequiredDaysForUpgrade() {
        return 2;
    }

    // Surcharge de la vérification de possibilité d'amélioration
    @Override
    public boolean canUpgrade() {
        return super.canUpgrade() && satisfaction > 60;
    }

    // Augmentation du besoin énergétique lors d'un upgrade de la résidence
    @Override
    protected void onUpgrade() {
        // Mise à jour du type
        this.type = ResidenceType.fromLevel(level);
        // Mise à jour du coût de base pour la revente ou maintenance (field protected
        // de Building)
        this.baseCost = type.getCost();
        // Mise à jour besoin
        this.energyNeed = type.getEnergyConsumption() * (0.9 + random.nextDouble() * 0.2);
    }

    // Représentation de la résidence
    @Override
    public String toString() {
        return "Residence (" + type.getLabel() + "){" +
                "energyNeed=" + String.format("%.2f", energyNeed) +
                ", satisfaction=" + String.format("%.2f", satisfaction) +
                ", level=" + level +
                '}';
    }
}
