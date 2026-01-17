package com.inf2328.energytycoon.view;

import com.inf2328.energytycoon.controller.GameController;
import com.inf2328.energytycoon.model.energy.EnergyType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/*
 * Panel principal du jeu

 *@param TopStatBar = Barre d'état supérieure
 *@param MapView = Vue de la carte
 *@param ActionSidebar = Barre latérale d'actions
 *@param PurchaseBar = Barre inférieure d'achat
*/
public class GamePanel extends JPanel {
    private final TopStatBar topBar;
    private final MapView mapView;
    private final ActionSidebar sidebar;
    private final PurchaseBar bottomBar;

    public GamePanel(GameController controller, Runnable onHome) {
        setLayout(new BorderLayout());

        // Initialisation des composants
        topBar = new TopStatBar(controller);
        mapView = new MapView(controller);
        sidebar = new ActionSidebar(controller, onHome);
        bottomBar = new PurchaseBar(controller, () -> {
            refresh();
        });

        // Container pour la carte
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setBackground(new Color(25, 27, 32));
        centerContainer.add(mapView);

        // Ajout des composants au panel principal
        add(topBar, BorderLayout.NORTH);
        add(new JScrollPane(centerContainer), BorderLayout.CENTER);
        add(sidebar, BorderLayout.EAST);
        add(bottomBar, BorderLayout.SOUTH);
    }

    // Méthode pour rafraîchir tous les composants
    public void refresh() {
        topBar.update();
        mapView.update();
        bottomBar.update();
        sidebar.update();
    }
}



/*
 * Barre d'état supérieure

 *@param moneyValue = Valeur de l'argent
 *@param dayValue = Valeur du jour
 *@param timeValue = Valeur de l'heure
 *@param periocValue = Valeur de la période
 *@param prodValue = Valeur de la production
 *@param demandValue = Valeur de la demande
 *@param alertLabel = Label d'alerte
*/
class TopStatBar extends JPanel {
    private final GameController controller;
    private final JLabel moneyValue = new JLabel();
    private final JLabel dayValue = new JLabel();
    private final JLabel timeValue = new JLabel();
    private final JLabel periocValue = new JLabel();
    private final JLabel prodValue = new JLabel();
    private final JLabel demandValue = new JLabel();
    private final JLabel alertLabel = new JLabel();

    // Constructeur de la barre d'état supérieure
    public TopStatBar(GameController controller) {
        this.controller = controller;
        setLayout(new FlowLayout(FlowLayout.CENTER, 35, 12));
        setBackground(new Color(18, 20, 24));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 40, 40)));

        // Ajout des groupes de statistiques
        add(createStatGroup("ARGENT", moneyValue, new Color(100, 255, 100)));
        add(createStatGroup("JOUR", dayValue, new Color(200, 200, 200)));
        add(createStatGroup("HORAIRE", timeValue, Color.WHITE));
        add(createStatGroup("PERIODE", periocValue, new Color(255, 200, 100)));
        add(createStatGroup("PRODUCTION", prodValue, new Color(100, 200, 255)));
        add(createStatGroup("DEMANDE", demandValue, new Color(255, 120, 120)));

        // Label d'alerte
        alertLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        alertLabel.setForeground(Color.RED);
        add(alertLabel);

        update();
    }

    // Méthode pour créer un groupe de statistiques
    private JPanel createStatGroup(String label, JLabel valueLabel, Color valueColor) {
        
        // Panel pour le groupe de statistiques
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setOpaque(false);

        // Label du groupe de statistiques
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(new Color(120, 120, 120));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setPreferredSize(new Dimension(80, 15));

        // Label de la valeur
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(valueColor);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Ajout des labels au panel
        p.add(l);
        p.add(valueLabel);
        return p;
    }

    // Méthode pour mettre à jour la barre d'état supérieure
    public void update() {
        var state = controller.getGameState();
        var cycle = state.getTimeCycle();
        var city = state.getCity();

        // Mise à jour des valeurs
        moneyValue.setText(String.format("%.2f $", state.getPlayer().getMoney()));
        dayValue.setText(String.valueOf(cycle.getTotalDays()));
        timeValue.setText(cycle.getFormattedTime());
        periocValue.setText(cycle.getTimeOfDay().name());

        // Calcul de la production et de la demande
        double demand = city.getTotalEnergyDemand();
        double production = city.getTotalEnergyProduction(cycle);

        // Mise à jour des valeurs
        prodValue.setText(String.format("%.1f kW", production));
        demandValue.setText(String.format("%.1f kW", demand));

        // Mise à jour de l'alerte
        if (production < demand) {
            alertLabel.setText("PENURIE !");
        } else {
            alertLabel.setText("");
        }
    }
}


/*
 * Barre d'action latérale

 *@param controller = Contrôleur du jeu
 *@param onHome = Action à effectuer lors du clic sur le bouton "ACCUEIL"
*/
class ActionSidebar extends JPanel {
    private final GameController controller;
    private final JButton pauseBtn;

    // Constructeur de la barre d'action latérale
    public ActionSidebar(GameController controller, Runnable onHome) {
        this.controller = controller;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 33, 40));
        setPreferredSize(new Dimension(220, 0));
        setBorder(new EmptyBorder(25, 15, 25, 15));

        // Label du titre
        JLabel title = new JLabel("CONTROLE");
        title.setForeground(new Color(100, 110, 120));
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        title.setAlignmentX(CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(20));

        // Bouton réparer tout
        JButton repairBtn = createBtn("RÉPARER TOUT", new Color(0, 120, 215));
        repairBtn.addActionListener(e -> {
            controller.repairAllPowerPlants();
            ((GamePanel) getParent()).refresh();
        });
        add(repairBtn);

        // Espace vide
        add(Box.createVerticalGlue());

        // Zone de légende
        add(createLegendPanel());
        add(Box.createVerticalStrut(20));

        // Bouton pause
        pauseBtn = createBtn("PAUSE", new Color(70, 70, 70));
        pauseBtn.addActionListener(e -> {
            controller.setPaused(!controller.isPaused());
            update();
            ((GamePanel) getParent()).refresh();
        });

        // Bouton quitter
        JButton quitBtn = createBtn("QUITTER", new Color(150, 50, 50));
        quitBtn.addActionListener(e -> onHome.run());

        // Ajout des boutons au panel
        add(pauseBtn);
        add(Box.createVerticalStrut(10));
        add(quitBtn);
    }

    // Méthode pour créer un panel de légende
    private JPanel createLegendPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                "LÉGENDE", 0, 0,
                new Font("Segoe UI", Font.BOLD, 10), Color.GRAY));
        
        // Ajout des légendes
        p.add(createLegendItem("PANNE", Color.RED, "Cercle barre rouge"));
        p.add(createLegendItem("UPGRADE", new Color(255, 140, 0), "Triangle orange ▲"));
        p.add(createLegendItem("RÉSEAU", new Color(0, 180, 255, 150), "Ligne bleue"));
        p.add(createLegendItem("COUPÉ", new Color(150, 150, 150, 100), "Pointillés gris"));

        // Taille maximale du panel
        p.setMaximumSize(new Dimension(190, 150));
        return p;
    }

    // Méthode pour créer un item de légende
    private JPanel createLegendItem(String label, Color c, String desc) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        p.setOpaque(false);

        // Label de la légende
        JLabel dot = new JLabel("●");
        dot.setForeground(c);
        dot.setFont(new Font("Arial", Font.BOLD, 14));

        // Label du texte
        JLabel txt = new JLabel("<html><b style='font-size: 9px; color: #BBB;'>" + label + "</b><br>" +
                "<i style='font-size: 8px; color: #777;'>" + desc + "</i></html>");

        p.add(dot);
        p.add(txt);
        return p;
    }

    // Méthode pour mettre à jour le bouton de pause
    public void update() {
        if (controller.isPaused()) {
            pauseBtn.setText("REPRENDRE");
            pauseBtn.setBackground(new Color(60, 150, 60));
        } else {
            pauseBtn.setText("PAUSE");
            pauseBtn.setBackground(new Color(70, 70, 70));
        }
    }

    // Méthode pour créer un bouton
    private JButton createBtn(String txt, Color bg) {
        JButton b = new JButton(txt);
        b.setMaximumSize(new Dimension(170, 50));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setAlignmentX(CENTER_ALIGNMENT);
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}


/*
 * Barre d'achat

 *@param controller = Contrôleur du jeu
 *@param onUpdate = Action à effectuer lors du clic sur un bouton d'achat
*/
class PurchaseBar extends JPanel {
    private final GameController controller;

    // Constructeur de la barre d'achat
    public PurchaseBar(GameController controller, Runnable onUpdate) {
        this.controller = controller;
        
        // Layout de la barre d'achat
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 10));
        setBackground(new Color(25, 27, 32));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(50, 50, 50)));

        // Ajout des boutons d'achat
        add(new PurchaseBtn("Maison", "100$", e -> {
            controller.buyResidence(1);
            onUpdate.run();
        }));
        add(new PurchaseBtn("Villa", "500$", e -> {
            controller.buyResidence(2);
            onUpdate.run();
        }));
        add(new PurchaseBtn("Immeuble", "2000$", e -> {
            controller.buyResidence(3);
            onUpdate.run();
        }));
        add(new PurchaseBtn("Hôtel", "5000$", e -> {
            controller.buyResidence(4);
            onUpdate.run();
        }));
        add(new PurchaseBtn("Gratte-Ciel", "10000$", e -> {
            controller.buyResidence(5);
            onUpdate.run();
        }));

        // Séparateur vertical
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(2, 40));
        add(sep);

        add(new PurchaseBtn("Charbon", "400$", e -> {
            controller.buyPowerPlant(EnergyType.COAL);
            onUpdate.run();
        }));
        add(new PurchaseBtn("Solaire", "200$", e -> {
            controller.buyPowerPlant(EnergyType.SOLAR);
            onUpdate.run();
        }));
        add(new PurchaseBtn("Eolienne", "150$", e -> {
            controller.buyPowerPlant(EnergyType.WIND);
            onUpdate.run();
        }));
        add(new PurchaseBtn("Nuclaire", "2000$", e -> {
            controller.buyPowerPlant(EnergyType.NUCLEAR);
            onUpdate.run();
        }));
    }

    // Méthode pour mettre à jour les boutons
    public void update() {
        boolean enabled = !controller.isPaused();
        for (Component c : getComponents()) {
            if (c instanceof JButton) {
                c.setEnabled(enabled);
                c.setBackground(enabled ? new Color(40, 44, 52) : new Color(30, 30, 30));
            }
        }
    }
}

// Bouton d'achat
class PurchaseBtn extends JButton {
    public PurchaseBtn(String name, String price, java.awt.event.ActionListener l) {
        super("<html><center><b>" + name + "</b><br>" + price + "</center></html>");

        // Configuration du bouton
        setPreferredSize(new Dimension(105, 55));
        // Ajout de l'action
        addActionListener(l);
        // Configuration visuelle
        setBackground(new Color(40, 44, 52));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setFont(new Font("Segoe UI", Font.PLAIN, 10));
        setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
