package com.inf2328.energytycoon.model.simulation;

import com.inf2328.energytycoon.model.city.GameState;
import com.inf2328.energytycoon.model.player.Player;
import com.inf2328.energytycoon.model.city.City;
import com.inf2328.energytycoon.model.building.Residence;
import com.inf2328.energytycoon.controller.GameController;
import com.inf2328.energytycoon.model.building.PowerPlant;
import com.inf2328.energytycoon.model.energy.EnergyType;

// Simulateur de jeu
public class Simulator {

    public static void main(String[] args) {

        // initialisation du joueur avec 1000€
        Player player = new Player(1000);

        // Création de la ville
        City city = new City();

        // Création de l'état du jeu
        GameState gameState = new GameState(player, city);

        // Création du contrôleur
        GameController controller = new GameController(gameState);

        // Ajout des résidence
        // Les coûts et niveaux max sont maintenant standardisés
        controller.buildResidence(new Residence());
        controller.buildResidence(new Residence());
        controller.buildResidence(new Residence());

        // Test achat direct niveau 2
        controller.buyResidence(2);

        // Ajout des centrales
        city.addPowerPlant(new PowerPlant(EnergyType.SOLAR));
        city.addPowerPlant(new PowerPlant(EnergyType.WIND));

        // Simulation du jeu
        while (!controller.isGameOver()) {
            controller.advanceTime();

            // Simulation de l'action du joueur : réparer si nécessaire
            for (PowerPlant p : city.getPowerPlants()) {
                if (p.isUnderMaintenance()) {
                    controller.repairPowerPlant(p);
                }
            }

            System.out.println(gameState);
            System.out.println("=================================");
        }

        System.out.println("GAME OVER");
    }
}
