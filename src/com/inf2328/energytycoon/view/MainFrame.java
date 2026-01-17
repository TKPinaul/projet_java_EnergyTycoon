package com.inf2328.energytycoon.view;

import com.inf2328.energytycoon.controller.GameController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainContainer = new JPanel(cardLayout);
    private final GameController controller;
    private GamePanel gamePanel;
    private Timer autoTimer;

    // Constructeur
    public MainFrame(GameController controller) {
        this.controller = controller;
        setTitle("Energy Tycoon - Gestion Énergétique");
        setSize(1200, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initPanels();
        initTimer();

        add(mainContainer);
        cardLayout.show(mainContainer, "START");
    }

    // Initialisation des panels
    private void initPanels() {

        // Panel de démarrage
        StartPanel startPanel = new StartPanel(
                e -> showGame(),
                e -> System.exit(0));

        // Panel de jeu
        gamePanel = new GamePanel(controller, this::showHome);

        // Ajout des panels
        mainContainer.add(startPanel, "START");
        mainContainer.add(gamePanel, "GAME");
    }

    // Initialisation du timer
    private void initTimer() {
        // Timer de jeu en auto
        autoTimer = new Timer(2500, e -> {
            if (isShowingGame()) {
                if (controller.isGameOver()) {
                    // Cas de fin de partie
                    showGameOver();
                } else if (!controller.isPaused()) {
                    // Cas de jeu normal
                    controller.advanceTime();
                    gamePanel.refresh();
                }
            }
        });
        // Démarrage du timer
        autoTimer.start();
    }

    // Vérification si on est sur le panel de jeu
    private boolean isShowingGame() {
        // Vérification de la visibilité du GamePanel
        for (Component comp : mainContainer.getComponents()) {
            if (comp.isVisible() && comp == gamePanel)
                return true;
        }
        return false;
    }

    // Affichage du panel de jeu
    public void showGame() {
        cardLayout.show(mainContainer, "GAME");
        gamePanel.refresh();
    }

    // Affichage du panel de démarrage
    public void showHome() {
        controller.setPaused(true);
        cardLayout.show(mainContainer, "START");
    }

    // Affichage du panel de fin de partie
    private void showGameOver() {
        GameOverPanel gameOverPanel = new GameOverPanel(
                controller,
                this::restartGame);
        mainContainer.add(gameOverPanel, "GAMEOVER");
        cardLayout.show(mainContainer, "GAMEOVER");
    }

    // Reinitialisation du jeu
    private void restartGame() {
        controller.restartGame();
        // Nettoyage visuel : recréer le GamePanel pour s'assurer que tout est vierge
        mainContainer.remove(gamePanel);
        gamePanel = new GamePanel(controller, this::showHome);
        mainContainer.add(gamePanel, "GAME");
        showGame();
    }
}
