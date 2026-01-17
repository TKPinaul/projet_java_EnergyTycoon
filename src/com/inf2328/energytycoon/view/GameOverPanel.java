package com.inf2328.energytycoon.view;

import com.inf2328.energytycoon.controller.GameController;
import com.inf2328.energytycoon.model.building.PowerPlant;
import com.inf2328.energytycoon.model.building.Residence;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameOverPanel extends JPanel {

    // Constructeur du panel de fin de partie
    public GameOverPanel(GameController controller, Runnable onRestart) {
        setLayout(new GridBagLayout());
        setBackground(new Color(25, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel title = new JLabel("PARTIE TERMINÉE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 60));
        title.setForeground(new Color(255, 80, 80));
        gbc.gridy = 0;
        add(title, gbc);

        JLabel reason = new JLabel("Bilan de votre mandat à la tête de la ville.");
        reason.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        add(reason, gbc);

        JPanel statsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(30, 0, 30, 0));

        int totalSlots = controller.getTotalTimeSlots();
        int days = totalSlots / 24;
        int hours = totalSlots % 24;

        statsPanel.add(createStatLabel("⌛ Durée du mandat : " + days + " jours et " + hours + "h"));

        Map<Integer, Integer> resByLevel = new HashMap<>();
        for (Residence r : controller.getGameState().getCity().getResidences()) {
            resByLevel.put(r.getLevel(), resByLevel.getOrDefault(r.getLevel(), 0) + 1);
        }

        Map<Integer, Integer> plantByLevel = new HashMap<>();
        for (PowerPlant p : controller.getGameState().getCity().getPowerPlants()) {
            plantByLevel.put(p.getLevel(), plantByLevel.getOrDefault(p.getLevel(), 0) + 1);
        }

        statsPanel.add(createStatLabel(
                "🏘️ " + controller.getGameState().getCity().getResidences().size() + " Habitations construites"));
        statsPanel.add(createStatLabel(
                "⚡ " + controller.getGameState().getCity().getPowerPlants().size() + " Centrales exploitées"));

        gbc.gridy = 2;
        add(statsPanel, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setOpaque(false);

        JButton restartBtn = new JButton("🔄 RECOMMENCER");
        styleButton(restartBtn, new Color(50, 150, 50));
        restartBtn.addActionListener(e -> onRestart.run());

        JButton quitBtn = new JButton("🚪 QUITTER");
        styleButton(quitBtn, new Color(150, 50, 50));
        quitBtn.addActionListener(e -> System.exit(0));

        btnPanel.add(restartBtn);
        btnPanel.add(quitBtn);

        gbc.gridy = 3;
        add(btnPanel, gbc);
    }

    // Méthode pour créer un label de statistique
    private JLabel createStatLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }

    // Méthode pour styliser un bouton
    private void styleButton(JButton btn, Color bg) {
        btn.setPreferredSize(new Dimension(180, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
