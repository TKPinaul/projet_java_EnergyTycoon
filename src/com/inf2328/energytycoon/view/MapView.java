package com.inf2328.energytycoon.view;

import com.inf2328.energytycoon.controller.GameController;
import com.inf2328.energytycoon.model.building.PowerPlant;
import com.inf2328.energytycoon.model.building.Residence;
import com.inf2328.energytycoon.model.energy.EnergyType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Vue Map avec rendu vectoriel détaillé, couleurs intensifiées par niveau.
 */
public class MapView extends JPanel {
    private final GameController controller;
    private final int cellSize = 65;
    private final int gridWidth = 16;
    private final int gridHeight = 10;

    // Constructeur de la vue de la map
    public MapView(GameController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(gridWidth * cellSize, gridHeight * cellSize));
        setBackground(new Color(25, 27, 32));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int gx = e.getX() / cellSize;
                int gy = e.getY() / cellSize;
                handleMapClick(gx, gy);
            }
        });
    }

    // Gestion du clic sur la map
    private void handleMapClick(int x, int y) {

        // Gestion du clic en cas de pause
        if (controller.isPaused())
            return;
        
        // Gestion du clic sur une residence
        for (Residence r : controller.getGameState().getCity().getResidences()) {
            if (r.getX() == x && r.getY() == y) {
                showResidenceDialog(r);
                return;
            }
        }

        // Gestion du clic sur une centrale
        for (PowerPlant p : controller.getGameState().getCity().getPowerPlants()) {
            if (p.getX() == x && p.getY() == y) {
                showPowerPlantDialog(p);
                return;
            }
        }
    }

    // Affiche la fenetre de la residence
    private void showResidenceDialog(Residence r) {
        String title = "DETAIL BATIMENT - " + r.getType().getLabel().toUpperCase();
        double currentTax = (20 * Math.pow(r.getLevel(), 2)) + 230;

        // Contenu de la fenetre HTML
        String content = "<html><body style='width: 250px; font-family: sans-serif; background-color: #222; color: #fff; padding: 10px;'>"
                +
                "<h1 style='color: #4A90E2;'>" + r.getType().getLabel() + "</h1>" +
                "<p>Niveau actuel : <b style='color: #F1C40F;'>L" + r.getLevel() + "</b></p>" +
                "<div style='background: #555; height: 2px; margin: 10px 0;'></div>" +
                "<b>📊 BILAN REEL :</b><br>" +
                "• Satisfaction : <span style='color: " + getSatColorHex(r.getSatisfaction()) + "'>"
                + String.format("%.1f", r.getSatisfaction()) + "%</span><br>" +
                "• Alimentation : <b>" + String.format("%.1f", r.getEnergyNeed()) + " kW</b><br>" +
                "• Revenus fiscaux : <b style='color: #2ECC71;'>+" + String.format("%.0f", currentTax)
                + "$ / jour</b><br>" +
                "<br><b>⬆️ EVOLUTION :</b><br>";

        // Gestion des actions disponibles
        if (r.getLevel() < r.getMaxLevel()) {
            double nextTax = (20 * Math.pow(r.getLevel() + 1, 2)) + 230;
            content += "• Cout upgrade : <b style='color: #E67E22;'>" + String.format("%.2f", r.getUpgradeCost())
                    + "$</b><br>" +
                    "• Gain impots : <b>+" + String.format("%.0f", nextTax - currentTax) + "$ / jour</b><br>";
            
            // Gestion des actions disponibles
            if (!r.canUpgrade())
                content += "<br><span style='color: #E74C3C;'>⚠️ Satisfaction > 60% requise</span>";
        } else { // Niveau maximum atteint
            content += "<span style='color: #95A5A6;'>Niveau Maximum Atteint</span>";
        }
        content += "</body></html>";

        // Gestion des options
        String[] options = (r.getLevel() < r.getMaxLevel() && r.canUpgrade()) ? new String[] { "Ameliorer", "Fermer" }
                : new String[] { "Fermer" };

        // Affichage de la fenetre
        int choice = JOptionPane.showOptionDialog(this, content, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        // Gestion des choix
        if (choice == 0 && options.length > 1) {
            if (controller.upgradeBuilding(r))
                refreshUI();
            else
                JOptionPane.showMessageDialog(this, "Fonds insuffisants !");
        }
    }

    // Affiche la fenetre de la centrale
    private void showPowerPlantDialog(PowerPlant p) {
        // Titre
        String title = "DETAIL CENTRALE - " + p.getEnergyType().name();
        // Contenu
        String content = "<html><body style='width: 250px; font-family: sans-serif; padding: 10px;'>" +
                "<h1 style='color: #F1C40F;'>" + p.getEnergyType().name() + "</h1>" +
                "<p>Niveau actuel : <b style='color: #4A90E2;'>L" + p.getLevel() + "</b></p>" +
                "<div style='background: #555; height: 2px; margin: 10px 0;'></div>" +
                "<b>⚡ PERFORMANCE :</b><br>" +
                "• Production : <b>" + String.format("%.1f", p.getEnergyProduction()) + " kW</b><br>" +
                "• Sante materielle : <b>" + (int) p.getMaintenanceLevel() + "%</b><br>" +
                "• Status : "
                + (p.isUnderMaintenance() ? "<b style='color: #E74C3C;'>EN PANNE</b>"
                        : "<b style='color: #2ECC71;'>OPERATIONNEL</b>")
                + "<br>" +
                "<br><b>🛠️ ACTIONS :</b><br>";

        // Actions disponibles
        boolean canRepair = p.isUnderMaintenance();
        boolean canUpgrade = p.canUpgrade() && p.getLevel() < p.getMaxLevel();

        // Ajout des actions
        if (canRepair)
            content += "• Reparation : <b style='color: #E67E22;'>" + String.format("%.2f", p.getRepairCost())
                    + "$</b><br>";
        if (p.getLevel() < p.getMaxLevel()) {
            content += "• Upgrade : <b style='color: #E67E22;'>" + String.format("%.2f", p.getUpgradeCost())
                    + "$</b><br>" +
                    "• Gain production : <b>+20%</b><br>";
        }

        // Options
        Object[] options;
        if (canRepair && canUpgrade)
            options = new Object[] { "Reparer", "Ameliorer", "Fermer" };
        else if (canRepair)
            options = new Object[] { "Reparer", "Fermer" };
        else if (canUpgrade)
            options = new Object[] { "Ameliorer", "Fermer" };
        else
            options = new Object[] { "Fermer" };

        // ouverture de la fenetre de dialogue
        int choice = JOptionPane.showOptionDialog(this, content, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        // Traitement de la reponse du joueur
        if (choice == 0) {
            if (canRepair) { // Reparation
                if (controller.repairPowerPlant(p))
                    refreshUI();
                else
                    JOptionPane.showMessageDialog(this, "Fonds insuffisants !");
            } else if (canUpgrade) { // Upgrade
                if (controller.upgradeBuilding(p))
                    refreshUI();
                else
                    JOptionPane.showMessageDialog(this, "Fonds insuffisants !");
            }
        } else if (choice == 1 && canRepair && canUpgrade) { // Fermer
            if (controller.upgradeBuilding(p))
                refreshUI();
            else
                JOptionPane.showMessageDialog(this, "Fonds insuffisants !");
        }
    }

    // Convertion du pourcentage en couleur
    private String getSatColorHex(double sat) {
        if (sat > 70)
            return "#2ECC71";
        if (sat > 40)
            return "#F1C40F";
        return "#E74C3C";
    }

    // Mise à jour de l'interface utilisateur
    private void refreshUI() {
        repaint();
        Container parent = getParent();
        while (parent != null && !(parent instanceof GamePanel))
            parent = parent.getParent();
        if (parent != null)
            ((GamePanel) parent).refresh();
    }

    // Met à jour la vue
    public void update() {
        repaint();
    }

    // Redessine la vue
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g2d);
        drawNetwork(g2d);
        drawBuildings(g2d);

        // Condition de pause
        if (controller.isPaused()) {
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
            String msg = "JEU EN PAUSE";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
        }
    }

    // Construction de la grille
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 6));

        // Construction des lignes horizontales
        for (int x = 0; x <= gridWidth; x++)
            g2d.drawLine(x * cellSize, 0, x * cellSize, gridHeight * cellSize);

        // Construction des lignes verticales
        for (int y = 0; y <= gridHeight; y++)
            g2d.drawLine(0, y * cellSize, gridWidth * cellSize, y * cellSize);
    }

    // Construction du réseau
    private void drawNetwork(Graphics2D g2d) {
        // Récupération des maisons et centrales
        List<Residence> residences = controller.getGameState().getCity().getResidences();
        List<PowerPlant> plants = controller.getGameState().getCity().getPowerPlants();

        // Construction du réseau
        for (Residence r : residences) {
            // Si la maison n'est pas construite
            if (r.getX() < 0)
                continue;

            // Position de la maison
            int rx = r.getX() * cellSize + cellSize / 2;
            int ry = r.getY() * cellSize + cellSize / 2;

            // Construction des lignes
            for (PowerPlant p : plants) {
                if (p.getX() < 0)
                    continue;

                // Position de la centrale
                int px = p.getX() * cellSize + cellSize / 2;
                int py = p.getY() * cellSize + cellSize / 2;

                // Construction des lignes
                if (!p.isUnderMaintenance()) {
                    g2d.setColor(new Color(0, 180, 255, 60));
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(new Color(150, 150, 150, 20));
                    g2d.setStroke(
                            new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[] { 5 }, 0));
                }
                g2d.drawLine(rx, ry, px, py);
            }
        }
    }

    // Construction des maisons et centrales
    private void drawBuildings(Graphics2D g2d) {
        for (Residence r : controller.getGameState().getCity().getResidences()) {
            if (r.getX() >= 0) {
                drawResidenceIcon(g2d, r);
                drawSatisfaction(g2d, r);
            }
        }
        // Centrales
        for (PowerPlant p : controller.getGameState().getCity().getPowerPlants()) {
            if (p.getX() >= 0) {
                drawPowerPlantIcon(g2d, p);
            }
        }

        // Reminder for upgrades
        for (Residence r : controller.getGameState().getCity().getResidences()) {
            if (r.getX() >= 0 && r.isUpgradeSuggested())
                drawUpgradeReminder(g2d, r.getX(), r.getY());
        }

        // Reminder for upgrades
        for (PowerPlant p : controller.getGameState().getCity().getPowerPlants()) {
            if (p.getX() >= 0 && p.isUpgradeSuggested())
                drawUpgradeReminder(g2d, p.getX(), p.getY());
        }
    }

    // Maison
    private void drawResidenceIcon(Graphics2D g2d, Residence r) {
        int x = r.getX() * cellSize + 8;
        int y = r.getY() * cellSize + 8;
        int size = cellSize - 16;

        // Couleur s'intensifie avec le niveau
        int intensity = 60 + (r.getLevel() * 30);
        Color baseColor = new Color(intensity / 2, intensity, intensity + 40);

        // Corps
        g2d.setColor(baseColor);
        g2d.fillRoundRect(x, y + size / 3, size, size - size / 3, 4, 4);

        // Toit
        g2d.setColor(baseColor.darker());
        int[] tx = { x - 2, x + size / 2, x + size + 2 };
        int[] ty = { y + size / 3, y, y + size / 3 };
        g2d.fillPolygon(tx, ty, 3);

        // Fenetres (details selon niveau)
        g2d.setColor(new Color(255, 255, 200, 150));
        int winSize = 6 + r.getLevel();
        int rows = r.getLevel() >= 3 ? 2 : 1;
        for (int i = 0; i < rows; i++) {
            g2d.fillRect(x + 5, y + size / 2 + (i * 10), winSize, winSize);
            g2d.fillRect(x + size - winSize - 5, y + size / 2 + (i * 10), winSize, winSize);
        }

        // Label Niv
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.drawString("L" + r.getLevel(), x + 2, y + 10);
    }

    // Centrale
    private void drawPowerPlantIcon(Graphics2D g2d, PowerPlant p) {
        int x = p.getX() * cellSize + 10;
        int y = p.getY() * cellSize + 10;
        int size = cellSize - 20;

        Color baseColor = getPlantColor(p.getEnergyType());
        // Intensite
        float hsb[] = Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), null);
        baseColor = Color.getHSBColor(hsb[0], hsb[1], Math.min(1.0f, hsb[2] + (p.getLevel() * 0.1f)));

        // Corps
        g2d.setColor(baseColor);
        g2d.fillOval(x, y, size, size);

        // Panneton / Details
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x + 5, y + 5, size - 10, size - 10);

        // Panne
        if (p.isUnderMaintenance()) {
            g2d.setColor(new Color(255, 0, 0, 200));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawOval(x, y, size, size);
            g2d.drawLine(x + 5, y + 5, x + size - 5, y + size - 5);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("PANNE", x + 5, y + size / 2 + 5);
        }

        // Label Niv
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.drawString("L" + p.getLevel(), x + 2, y + 15);
    }

    // Satisfaction
    private void drawSatisfaction(Graphics2D g2d, Residence r) {
        int x = r.getX() * cellSize + 15;
        int y = r.getY() * cellSize + cellSize - 8;
        int width = cellSize - 30;

        // Barre de satisfaction
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRect(x, y, width, 4);

        double sat = r.getSatisfaction();

        g2d.setColor(sat > 70 ? Color.GREEN : (sat > 40 ? Color.ORANGE : Color.RED));
        g2d.fillRect(x, y, (int) (width * sat / 100.0), 4);
    }

    // Rappel d'upgrade
    private void drawUpgradeReminder(Graphics2D g2d, int gx, int gy) {
        int x = (gx + 1) * cellSize - 15;
        int y = gy * cellSize + 5;

        // Triangle de rappel
        g2d.setColor(new Color(255, 140, 0)); // Orange
        int[] tx = { x, x + 10, x + 5 };
        int[] ty = { y + 10, y + 10, y };
        g2d.fillPolygon(tx, ty, 3);

        // Glow effect
        g2d.setColor(new Color(255, 140, 0, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(tx, ty, 3);
    }

    // Couleur des centrales
    private Color getPlantColor(EnergyType type) {
        return switch (type) {
            case SOLAR -> new Color(240, 190, 0);
            case WIND -> new Color(100, 180, 200);
            case NUCLEAR -> new Color(130, 100, 200);
            case COAL -> new Color(80, 80, 80);
            default -> Color.GRAY;
        };
    }
}
