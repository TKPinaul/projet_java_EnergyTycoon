package com.inf2328.energytycoon.model.player;

import com.inf2328.energytycoon.model.building.Building;
import com.inf2328.energytycoon.model.building.PowerPlant;
import com.inf2328.energytycoon.model.building.Residence;

import java.util.ArrayList;
import java.util.List;

/*
 * Classe représentant le joueur
 * 
 * @param money : l'argent du joueur
 * @param residences : la liste des résidences
 * @param powerPlants : la liste des centrales
 */
public class Player {

    private double money;
    private final List<Residence> residences;
    private final List<PowerPlant> powerPlants;

    public Player(double initialMoney) {
        this.money = initialMoney;
        this.residences = new ArrayList<>();
        this.powerPlants = new ArrayList<>();
    }

    // Récupération de l'argent
    public double getMoney() {
        return money;
    }

    // Augmentation de l'argent
    public void addMoney(double amount) {
        money += amount;
    }

    // Décrémentation de l'argent
    public boolean spendMoney(double amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false; // pas assez d'argent
    }

    // Construction d'une résidence (logique interne)
    public boolean buildResidence(Residence r) {
        if (spendMoney(r.getBaseCost())) {
            residences.add(r);
            return true;
        }
        return false;
    }

    // Ajout direct (pour le contrôleur)
    public void addResidence(Residence r) {
        residences.add(r);
    }

    // Ajout direct (pour le contrôleur)
    public void addPowerPlant(PowerPlant p) {
        powerPlants.add(p);
    }

    // Construction d'une centrale
    public boolean buildPowerPlant(PowerPlant p) {
        if (spendMoney(p.getBaseCost())) {
            powerPlants.add(p);
            return true;
        }
        return false;
    }

    // Amélioration d'un bâtiment
    public boolean upgradeBuilding(Building b) {
        if (b.canUpgrade() && spendMoney(b.getUpgradeCost())) {
            b.upgrade();
            return true; // amélioration reussie
        }
        return false; // amélioration echouée
    }

    // Récupération des résidences
    public List<Residence> getResidences() {
        return residences;
    }

    // Récupération des centrales
    public List<PowerPlant> getPowerPlants() {
        return powerPlants;
    }

    // Représentation du joueur
    @Override
    public String toString() {
        return String.format("Player [money=%.2f, residences=%d, powerPlants=%d]",
                money, residences.size(), powerPlants.size());
    }
}
