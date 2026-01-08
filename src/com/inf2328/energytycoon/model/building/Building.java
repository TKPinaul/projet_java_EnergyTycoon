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

    public Building(int maxLevel, double baseCost, double upgradeCost) {
        this.level = 1;
        this.maxLevel = maxLevel;
        this.baseCost = baseCost;
        this.upgradeCost = upgradeCost;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
    
    public double getBaseCost() {
        return baseCost;
    }
    
    public double getUpgradeCost() {
        return upgradeCost;
    }

    public boolean canUpgrade() {
        return level < maxLevel;
    }
    
    public void upgrade() {
        if (canUpgrade()) {
            level++;
            onUpgrade();
        }
    }

    public abstract void onUpgrade();
    
}
