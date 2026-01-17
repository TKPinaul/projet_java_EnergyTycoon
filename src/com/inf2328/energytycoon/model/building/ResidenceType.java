package com.inf2328.energytycoon.model.building;

/*
 * Enum des types de résidences

 * @param label : le nom du type de résidence
 * @param cost : le coût du type de résidence
 * @param energyConsumption : la consommation d'énergie du type de résidence
 * @param level : le niveau du type de résidence
 */
public enum ResidenceType {
    HOUSE("Maison", 100, 10, 1),
    VILLA("Villa", 500, 30, 2),
    APARTMENT("Immeuble", 2000, 100, 3),
    HOTEL("Hôtel", 5000, 200, 4),
    SKYSCRAPER("Gratte-Ciel", 10000, 500, 5);

    private final String label;
    private final double cost;
    private final double energyConsumption;
    private final int level;

    ResidenceType(String label, double cost, double energyConsumption, int level) {
        this.label = label;
        this.cost = cost;
        this.energyConsumption = energyConsumption;
        this.level = level;
    }

    // récupération du label
    public String getLabel() {
        return label;
    }

    // récupération du coût
    public double getCost() {
        return cost;
    }

    // récupération de la consommation d'énergie
    public double getEnergyConsumption() {
        return energyConsumption; // Consommation de base
    }

    // récupération du niveau
    public int getLevel() {
        return level;
    }

    // récupération du type de résidence à partir du niveau
    public static ResidenceType fromLevel(int level) {
        for (ResidenceType type : values()) {
            if (type.level == level) {
                return type;
            }
        }
        return HOUSE; // Par défaut
    }
}
