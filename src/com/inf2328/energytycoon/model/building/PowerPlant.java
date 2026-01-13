package com.inf2328.energytycoon.model.building;
import com.inf2328.energytycoon.model.energy.EnergyProduction;
import com.inf2328.energytycoon.model.energy.EnergyType;

/*
 * Class représentant une centrale électrique
 
 * @param production : production d'énergie de la centrale
 */
public class PowerPlant extends Building {

    private EnergyProduction production;

    public PowerPlant(double baseCost, double upgradeCost, int maxLevel, EnergyType type, double initialOutput) {
        super(baseCost, upgradeCost, maxLevel);
        this.production = new EnergyProduction(type, initialOutput);
    }

    // Récupération de la production d'énergie
    public EnergyProduction getProduction() {
        return production;
    }

    // Augmentation de la production d'énergie
    @Override
    protected void onUpgrade() {
        production.increaseAmount(1.2); // augmente la production de 20%
    }

    // Représentation de la centrale électrique
    @Override
    public String toString() {
        return "PowerPlant{" +
                "production=" + production +
                ", level=" + level +
                ", maxLevel=" + maxLevel +
                ", baseCost=" + baseCost +
                ", upgradeCost=" + upgradeCost +
                '}';
    }
}