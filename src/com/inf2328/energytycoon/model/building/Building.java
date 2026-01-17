package com.inf2328.energytycoon.model.building;

/*
 * Classe abstraite représentant un bâtiment
 */
public abstract class Building {

    protected int level;
    protected final int maxLevel;
    protected double baseCost;
    protected double upgradeCost;
    protected int x = -1;
    protected int y = -1;

    protected int daysSinceLastUpgrade = 0;
    protected boolean upgradeSuggested = false;

    public Building(double baseCost, double upgradeCost, int maxLevel, int initialLevel) {
        this.level = Math.max(1, initialLevel);
        this.maxLevel = maxLevel;
        this.baseCost = baseCost;
        this.upgradeCost = upgradeCost;
        this.daysSinceLastUpgrade = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void dailyUpdate() {
        daysSinceLastUpgrade++;
    }

    public boolean isUpgradeSuggested() {
        return upgradeSuggested && canUpgrade();
    }

    public void setUpgradeSuggested(boolean suggested) {
        this.upgradeSuggested = suggested;
    }

    protected abstract int getRequiredDaysForUpgrade();

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public double getPriceForLevel(int targetLevel) {
        double total = baseCost;
        for (int i = 1; i < targetLevel; i++) {
            total += upgradeCost * Math.pow(1.2, i - 1);
        }
        return total;
    }

    public double getUpgradeCost() {
        return upgradeCost * Math.pow(1.2, level - 1);
    }

    public boolean canUpgrade() {
        return level < maxLevel && daysSinceLastUpgrade >= getRequiredDaysForUpgrade();
    }

    public void upgrade() {
        if (canUpgrade()) {
            level++;
            daysSinceLastUpgrade = 0;
            upgradeSuggested = false; // Reset suggestion on success
            onUpgrade();
        }
    }

    protected abstract void onUpgrade();
}
