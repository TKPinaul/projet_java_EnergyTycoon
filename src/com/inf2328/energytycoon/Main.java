package com.inf2328.energytycoon;

import javax.swing.SwingUtilities;
import com.inf2328.energytycoon.controller.GameController;
import com.inf2328.energytycoon.model.city.City;
import com.inf2328.energytycoon.model.city.GameState;
import com.inf2328.energytycoon.model.player.Player;
import com.inf2328.energytycoon.view.MainFrame;

/*
 * Point d'entrée du jeu
 */
public class Main {
    public static void main(String[] args) {
        // Initialisation des données du jeu
        Player player = new Player(1200);
        City city = new City();
        GameState gameState = new GameState(player, city);
        GameController controller = new GameController(gameState);

        // Initialisation du jeu
        controller.restartGame();

        // Lancement de l'interface graphique
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(controller);
            frame.setVisible(true);
        });
    }
}
