package com.inf2328.energytycoon.controller;

import com.inf2328.energytycoon.model.building.PowerPlant;
import com.inf2328.energytycoon.model.building.Residence;
import com.inf2328.energytycoon.model.city.City;
import com.inf2328.energytycoon.model.city.GameState;
import com.inf2328.energytycoon.model.city.TimeCycle;

/*
 * Contrôleur du jeu

 * @param gameState : l'état du jeu
 * @param gameOver : indique si le jeu est terminé
 */
public class GameController {
    private final GameState gameState;
    private boolean gameOver = false;

    public GameController(GameState gameState) {
        this.gameState = gameState;
    }

    // Avancement du jeu (par créneau horaire)
    public void advanceTime() {

        if (gameOver)
            return;

        // On garde le jour actuel pour savoir si on change de jour
        TimeCycle time = gameState.getTimeCycle();
        int currentDay = time.getDay();

        // Avancement du temps dans le GameState
        gameState.advanceTime();

        // Si le jour a changé, on applique les revenus et la maintenance
        if (time.getDay() != currentDay) {

            // Augmentation de la difficulté
            gameState.getCity().performDailyUpdates();

            // Calcul des finances
            double revenue = calculateDailyRevenue();
            double maintenance = calculateDailyMaintenance();

            // Mise à jour du compte
            gameState.getPlayer().addMoney(revenue - maintenance);

            System.out.println("--- End of Day " + currentDay + " --- Revenue: " + String.format("%.2f", revenue)
                    + " | Maintenance: " + String.format("%.2f", maintenance));
        }

        // Vérification des règles de défaite
        gameOver = gameState.updateAndCheckGameOver();
    }

    // Calcul du revenu
    private double calculateDailyRevenue() {

        double revenue = 0;
        City city = gameState.getCity();

        // Calcul du revenu
        for (Residence r : city.getResidences()) {
            double baseIncome = 30 * r.getLevel(); // Revenu proportionnel au niveau
            double s = r.getSatisfaction();

            // pénalités de satisfaction
            if (s < 25) {
                revenue += baseIncome * 0.25;
            } else if (s < 50) {
                revenue += baseIncome * 0.5;
            } else {
                revenue += baseIncome;
            }
        }
        return revenue;
    }

    // Calcul des coûts journaliers
    private double calculateDailyMaintenance() {
        double cost = 0;
        City city = gameState.getCity();

        // 10% du coût de base * niveau pour les résidences
        for (Residence r : city.getResidences()) {
            cost += r.getBaseCost() * 0.10 * r.getLevel();
        }

        // coût de maintenance des centrales
        for (PowerPlant p : city.getPowerPlants()) {
            cost += p.getDailyMaintenanceCost();
        }

        return cost;
    }

    // Réparation d'une centrale
    public boolean repairPowerPlant(PowerPlant p) {
        double cost = p.getRepairCost();

        if (gameState.getPlayer().getMoney() >= cost) {
            gameState.getPlayer().addMoney(-cost);
            p.performMaintenance();
            System.out.println("Reparation reussie pour " + cost + "$. La centrale est en securite pour 4 jours.");
            return true;
        } else {
            System.out.println("Pas assez d'argent pour reparer! Besoin de " + cost + "$");
            return false;
        }
    }

    // Achat d'une résidence supérieure
    public boolean buyResidence(int targetLevel) {

        Residence temp = new Residence();
        double cost = temp.getPriceForLevel(targetLevel);

        if (gameState.getPlayer().getMoney() >= cost) {
            gameState.getPlayer().addMoney(-cost);
            Residence newRes = new Residence(targetLevel);
            gameState.getCity().addResidence(newRes);
            gameState.getPlayer().addResidence(newRes);
            System.out.println("Niveau de résidence achetée " + targetLevel + " (" + newRes + ") pour "
                    + String.format("%.2f", cost) + "$");
            return true;
        } else {
            System.out.println("Pas assez d'argent pour acheter le niveau " + targetLevel + "! Besoin de "
                    + String.format("%.2f", cost) + "$");
            return false;
        }
    }

    // Ajout d'une résidence (mode debug / gratuit ou géré ailleurs)
    public void buildResidence(Residence r) {
        gameState.getCity().addResidence(r);
        gameState.getPlayer().addResidence(r);
    }

    // Ajout d'une centrale (mode debug / gratuit)
    public void buildPowerPlant(PowerPlant p) {
        gameState.getCity().addPowerPlant(p);
        gameState.getPlayer().addPowerPlant(p);
    }

    // Vérification de la fin du jeu
    public boolean isGameOver() {
        return gameOver;
    }

    // Récupération de l'état du jeu
    public GameState getGameState() {
        return gameState;
    }
}