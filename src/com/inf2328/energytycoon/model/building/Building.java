package com.inf2328.energytycoon.model.building;

/*
 * Classe abstraite représentant un bâtiment

 * @param maxLevel : le niveau maximum du bâtiment
 * @param baseCost : le coût de base du bâtiment
 * @param upgradeCost : le coût d'amélioration
*/
public abstract class Building {

    protected int level;
    protected final int maxLevel;
    protected double baseCost;
    protected double upgradeCost;

    public Building(double baseCost, double upgradeCost, int maxLevel) {
        this.level = 1;
        this.maxLevel = maxLevel;
        this.baseCost = baseCost;
        this.upgradeCost = upgradeCost;
    }

    // Retourne le niveau actuel du bâtiment
    public int getLevel() {
        return level;
    }

    // Retourne le niveau maximum du bâtiment
    public int getMaxLevel() {
        return maxLevel;
    }

    // Retourne le coût de base du bâtiment
    public double getBaseCost() {
        return baseCost;
    }

    // Retourne le coût d'amélioration du bâtiment
    public double getUpgradeCost() {
        return upgradeCost * Math.pow(1.2, level - 1);
    }

    // Vérifie si le bâtiment peut être amélioré
    public boolean canUpgrade() {
        return level < maxLevel;
    }

    // Améliore le bâtiment
    public void upgrade() {
        if (canUpgrade()) {
            level++;
            onUpgrade();
        }
    }

    // Méthode de mise à jour du bâtiment (géré dans les classes filles)
    protected abstract void onUpgrade();

}
