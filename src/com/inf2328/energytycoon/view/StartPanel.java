package com.inf2328.energytycoon.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/*
 * Panel de démarrage
 *
 * @param onStart = Action à effectuer lors du clic sur le bouton "Démarrer"
 * @param onQuit = Action à effectuer lors du clic sur le bouton "Quitter"
 */
public class StartPanel extends JPanel {

    // Constructeur
    public StartPanel(ActionListener onStart, ActionListener onQuit) {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // Titre
        JLabel title = new JLabel("Energy Tycoon");
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(new Color(33, 150, 243));
        gbc.gridy = 0;
        add(title, gbc);

        // Message d'accueil
        JLabel welcome = new JLabel(
                "<html><center>Bienvenue Maire 👋<br>Votre mission : alimenter la ville sans provoquer de crise énergétique.</center></html>");
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcome.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        add(welcome, gbc);

        // Espace
        gbc.gridy = 2;
        add(Box.createVerticalStrut(40), gbc);

        // Bouton Démarrer
        JButton startBtn = new JButton(" Démarrer une nouvelle partie");
        styleButton(startBtn, new Color(76, 175, 80));
        startBtn.addActionListener(onStart);
        gbc.gridy = 3;
        add(startBtn, gbc);

        // Bouton Quitter
        JButton quitBtn = new JButton("Quitter");
        styleButton(quitBtn, new Color(158, 158, 158));
        quitBtn.addActionListener(onQuit);
        gbc.gridy = 4;
        add(quitBtn, gbc);
    }

    // Style des boutons
    private void styleButton(JButton btn, Color bg) {
        btn.setPreferredSize(new Dimension(350, 60));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
