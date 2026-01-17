package com.inf2328.energytycoon.model.building;

import com.inf2328.energytycoon.model.city.TimeCycle;
import com.inf2328.energytycoon.model.energy.EnergyProduction;
import com.inf2328.energytycoon.model.energy.EnergyType;

/*
 * Class représentant une centrale électrique
 
 * @param production : production d'énergie de la centrale
 * @param underMaintenance : état de maintenance de la centrale
 */
public class PowerPlant extends Building {

    private EnergyProduction production;
    private boolean underMaintenance = false;
    private int repairCount = 0;

    // Niveau max de la centrale
    public static final int POWER_PLANT_MAX_LEVEL = 4;

    public PowerPlant(EnergyType type, int initialLevel) {
        super(getCostForType(type), getUpgradeCostForType(type), POWER_PLANT_MAX_LEVEL, initialLevel);
        this.production = new EnergyProduction(type, getInitialOutputForType(type));
    }

    // Constructeur par défaut
    public PowerPlant(EnergyType type) {
        this(type, 1);
    }

    // récupération du coût de base de création d'une centrale
    private static double getCostForType(EnergyType type) {
        return switch (type) {
            case SOLAR -> 200;
            case WIND -> 150;
            case COAL -> 400;
            case NUCLEAR -> 2000;
            default -> 100;
        };
    }

    // récupération du coût de base d'amélioration d'une centrale
    private static double getUpgradeCostForType(EnergyType type) {
        return getCostForType(type) / 2; // 50% du prix de base
    }

    // récupération de la production initiale d'une centrale
    private static double getInitialOutputForType(EnergyType type) {
        return switch (type) {
            case SOLAR -> 60;
            case WIND -> 30;
            case COAL -> 100;
            case NUCLEAR -> 300; // Ou 1000 comme dans le commentaire, restons raisonnable
            default -> 50;
        };
    }

    // récupération de la production d'énergie
    public EnergyProduction getProduction() {
        return production;
    }

    public EnergyType getEnergyType() {
        return production.getType();
    }

    public double getEnergyProduction() {
        return production.getBaseAmount();
    }

    public double getMaintenanceLevel() {
        // Retourne la santé (100% = parfait, 0% = besoin de réparation)
        if (underMaintenance)
            return 0;
        return (double) Math.min(100, (safeOperationTime * 100.0) / 30.0);
    }

    // récupération de la production
    public double getEffectiveProduction(TimeCycle time) {
        if (underMaintenance)
            return 0;

        // Récupération de la production de base
        double base = production.getBaseAmount();

        // Calcul de la production en fonction du type d'énergie
        switch (production.getType()) {
            case SOLAR:
                return solarProduction(base, time);
            case WIND:
                return windProduction(base);
            case COAL:
                return base;
            case NUCLEAR:
                return base * 0.95;
            default:
                return base;
        }
    }

    // Calcul de la production solaire
    private double solarProduction(double base, TimeCycle time) {
        switch (time.getTimeOfDay()) {
            case MORNING:
                return base * 0.6;
            case DAY:
                return base;
            case EVENING:
                return base * 0.4;
            case NIGHT:
                return base * 0;
            default:
                return base;
        }
    }

    // Calcul de la production eolienne
    private double windProduction(double base) {
        double factor = 0.7 + Math.random() * 0.6; // 0.7 → 1.3
        return base * factor;
    }

    // Temps de maintenance
    private int safeOperationTime = 0;

    // Coût de maintenance quotidienne
    public double getDailyMaintenanceCost() {
        return baseCost * 0.05 * level;
    }

    // Coût de réparation
    public double getRepairCost() {
        return baseCost * 0.2; // 20% du prix d'achat pour réparer
    }

    // Mise à jour de l'état de la centrale (pannes aléatoires)
    public void updateStatus() {
        // Période de grâce après maintenance
        if (safeOperationTime > 0) {
            safeOperationTime--;
            return;
        }

        if (underMaintenance) {
            // Ne se réparer plus toute seule ! Le joueur doit payer.
        } else {
            // 5% de chance de panne
            if (Math.random() < 0.05) {
                underMaintenance = true;
            }
        }
    }

    // Vérification de l'état de maintenance
    public boolean isUnderMaintenance() {
        return underMaintenance;
    }

    // Action du joueur : réparer / maintenir
    public void performMaintenance() {
        underMaintenance = false;
        safeOperationTime = 30; // ~1.25 jours de tranquillité (24 slots par jour)
        repairCount++;
        if (repairCount >= 5 && level < maxLevel) {
            upgradeSuggested = true;
        }
    }

    // Nombre de jours requis pour améliorer la centrale
    @Override
    protected int getRequiredDaysForUpgrade() {
        return 3;
    }

    // Augmentation de la production d'énergie
    @Override
    protected void onUpgrade() {
        production.increaseAmount(1.2); // augmente la production de 20%
        repairCount = 0; // Reset repair count on upgrade
    }

    // Représentation de la centrale électrique
    @Override
    public String toString() {
        return "PowerPlant{" +
                "type=" + production.getType() +
                ", output=" + production.getBaseAmount() +
                ", level=" + level +
                ", maintenance=" + underMaintenance +
                '}';
    }
}