package com.inf2328.energytycoon.model.simulation;

import com.inf2328.energytycoon.model.city.GameState;
import com.inf2328.energytycoon.model.player.Player;
import com.inf2328.energytycoon.model.city.City;
import com.inf2328.energytycoon.controller.GameController;
import com.inf2328.energytycoon.model.building.PowerPlant;
import com.inf2328.energytycoon.model.energy.EnergyType;

/*
 * Simulateur de jeu
 
 * @param player : le joueur
 * @param city : la ville
 * @param gameState : l'état du jeu
 * @param controller : le contrôleur*/

public class Simulator {

    public static void main(String[] args) {

        // initialisation du joueur avec 1200€
        Player player = new Player(1200);

        // Création de la ville
        City city = new City();

        // Création de l'état du jeu
        GameState gameState = new GameState(player, city);

        // Création du contrôleur
        GameController controller = new GameController(gameState);

        // Ajout des résidence
        controller.buyResidence(1);
        controller.buyResidence(1);
        controller.buyResidence(1);

        // Test achat direct niveau 2
        controller.buyResidence(2);

        // Ajout des centrales via le controleur (methode buy existante)
        controller.buyPowerPlant(EnergyType.SOLAR);
        controller.buyPowerPlant(EnergyType.WIND);

        // Simulation du jeu
        while (!controller.isGameOver()) {
            controller.advanceTime();

            // Simulation de l'action du joueur : réparer si nécessaire
            for (PowerPlant p : city.getPowerPlants()) {
                if (p.isUnderMaintenance()) {
                    controller.repairPowerPlant(p);
                }
            }
            // Affichage de l'état du jeu
            System.out.println(gameState);
            System.out.println("=================================");
        }
           // Fin du jeu
        System.out.println("GAME OVER");
    }
}
