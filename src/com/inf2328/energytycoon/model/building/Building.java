package com.inf2328.energytycoon.model.building;

/*
 * Classe abstraite représentant un bâtiment

 * @param maxLevel : le niveau maximum du bâtiment
 * @param baseCost : le coût de base du bâtiment
 * @param upgradeCost : le coût d'amélioration
 * @param daysSinceLastUpgrade : temps écoulé depuis la dernière amélioration
*/
public abstract class Building {

    protected int level;
    protected final int maxLevel;
    protected double baseCost;
    protected double upgradeCost;

    // temps écoulé depuis la dernière amélioration
    protected int daysSinceLastUpgrade = 0;

    public Building(double baseCost, double upgradeCost, int maxLevel, int initialLevel) {
        this.level = Math.max(1, initialLevel);
        this.maxLevel = maxLevel;
        this.baseCost = baseCost;
        this.upgradeCost = upgradeCost;

        this.daysSinceLastUpgrade = 0;
    }

    // mise à jour quotidienne
    public void dailyUpdate() {
        daysSinceLastUpgrade++;
    }

    // récupération du nombre de jours nécessaires pour améliorer
    protected abstract int getRequiredDaysForUpgrade();

    // Retourne le niveau actuel du bâtiment
    public int getLevel() {
        return level;
    }

    // Retourne le niveau maximum du bâtiment
    public int getMaxLevel() {
        return maxLevel;
    }

    // Retourne le coût de base du bâtiment (Niveau 1)
    public double getBaseCost() {
        return baseCost;
    }

    // Calcule le prix total pour acheter directement à un niveau donné le bâtiment
    public double getPriceForLevel(int targetLevel) {
        double total = baseCost;
        for (int i = 1; i < targetLevel; i++) {
            total += upgradeCost * Math.pow(1.2, i - 1);
        }
        return total;
    }

    // Retourne le coût du PROCHAIN amélioration
    public double getUpgradeCost() {
        return upgradeCost * Math.pow(1.2, level - 1);
    }

    // Vérifie si le bâtiment peut être amélioré (niveau max + temps d'attente)
    public boolean canUpgrade() {
        return level < maxLevel && daysSinceLastUpgrade >= getRequiredDaysForUpgrade();
    }

    // Améliore le bâtiment
    public void upgrade() {
        if (canUpgrade()) {
            level++;
            daysSinceLastUpgrade = 0; // Reset du timer
            onUpgrade();
        }
    }

    // Méthode de mise à jour du bâtiment (géré dans les classes filles)
    protected abstract void onUpgrade();

}
