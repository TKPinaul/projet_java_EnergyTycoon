package com.inf2328.energytycoon.model.simulation;

import com.inf2328.energytycoon.model.city.GameState;
import com.inf2328.energytycoon.model.player.Player;
import com.inf2328.energytycoon.model.city.City;
import com.inf2328.energytycoon.model.building.Residence;
import com.inf2328.energytycoon.model.building.PowerPlant;
import com.inf2328.energytycoon.model.city.TimeCycle;
import com.inf2328.energytycoon.model.energy.EnergyType;

import java.util.Locale;
import java.util.Random;

/**
 * Simulator amélioré avec upgrades automatiques pour tester le modèle
 */
public class Simulator {

    private final GameState gameState;
    private final Random random = new Random();

    public Simulator() {

        Player player = new Player(1000); // argent initial
        City city = new City();

        // Ajouter résidences
        city.addResidence(new Residence(100, 50, 3));
        city.addResidence(new Residence(120, 60, 3));
        city.addResidence(new Residence(90, 45, 2));

        // Ajouter centrales
        city.addPowerPlant(new PowerPlant(500, 200, 5, EnergyType.SOLAR, 60));
        city.addPowerPlant(new PowerPlant(600, 250, 5, EnergyType.WIND, 30));

        gameState = new GameState(player, city);
    }

    public void runSimulation(int days) {
        for (int i = 0; i < days; i++) {
            nextDay();
            System.out.println("\n====================================\n");
        }
    }

    private void nextDay() {
        TimeCycle time = gameState.getTimeCycle();

        // Avancer le temps
        time.nextDay();

        // augmentation des besoins énergétiques des résidences
        for (Residence r : gameState.getCity().getResidences()) {
            r.increaseEnergyNeedOverTime(); // 1 % par jour
        }

        // Augmentations automatiques selon le jour
        upgradeBuildings(time.getDay());

        // Distribuer l'énergie
        gameState.getCity().distributeEnergy();

        // Calculer les revenus
        double revenue = calculateDailyRevenue();
        gameState.getPlayer().addMoney(revenue);

        // Affichage
        displayStatus(time, revenue);
    }

    private void displayStatus(TimeCycle time, double revenue) {
        System.out.println(String.format(
            "=== Day %d, Week %d, Month %d ===",
            time.getDay(), time.getWeek(), time.getMonth()));

        System.out.printf(Locale.FRANCE,
            "Revenue today: %.2f | Total money: %.2f%n",
            revenue, gameState.getPlayer().getMoney());

        System.out.println("\n--- City Status ---");

        System.out.println("Residences:");
        for (Residence r : gameState.getCity().getResidences()) {
            System.out.printf(Locale.FRANCE,
                "  Level %d | Satisfaction: %.1f%% | Energy Need: %.2f%n",
                r.getLevel(), r.getSatisfaction(), r.getEnergyNeed());
        }

        System.out.println("Power Plants:");
        for (PowerPlant p : gameState.getCity().getPowerPlants()) {
            System.out.printf(Locale.FRANCE,
                "  Level %d | Type: %s | Production: %.2f%n",
                p.getLevel(), p.getProduction().getType(),
                p.getProduction().getAmount());
        }

        System.out.printf(Locale.FRANCE,
            "Total Energy Demand: %.2f | Total Production: %.2f%n",
            gameState.getCity().getTotalEnergyDemand(),
            gameState.getCity().getTotalEnergyProduction());
    }

    /**
     * Augmente automatiquement certains niveaux pour tester l’évolution
     */
    private void upgradeBuildings(int day) {
        City city = gameState.getCity();

        switch (day) {
            case 2 -> {
                // Jour 2 : augmenter 1 ou 2 résidences existantes
                if (city.getResidences().size() > 0) city.getResidences().get(0).upgrade();
                if (city.getResidences().size() > 1) city.getResidences().get(1).upgrade();
                System.out.println(">>> Upgraded 1-2 residences on Day 2");

                // Ajouter 2 nouvelles résidences
                city.addResidence(new Residence(110, 55, 3));
                city.addResidence(new Residence(95, 45, 2));
                System.out.println(">>> Added 2 new residences on Day 2");
            }
            case 3 -> {
                // Jour 3 : augmenter une centrale aléatoire
                int index = random.nextInt(city.getPowerPlants().size());
                city.getPowerPlants().get(index).upgrade();
                System.out.println(">>> Upgraded PowerPlant #" + (index + 1) + " on Day 3");

                // Ajouter 2 nouvelles résidences
                city.addResidence(new Residence(105, 50, 3));
                city.addResidence(new Residence(100, 45, 2));
                System.out.println(">>> Added 2 new residences on Day 3");
            }
        }
    }



    private double calculateDailyRevenue() {
    double revenue = 0;

    for (Residence r : gameState.getCity().getResidences()) {
        double baseIncome = 50;
        double satisfaction = r.getSatisfaction();

        if (satisfaction < 25) {
            revenue += baseIncome * 0.25; // pénalité doublée
        } else if (satisfaction < 50) {
            revenue += baseIncome * 0.5;  // pénalité simple
        } else {
            revenue += baseIncome;
        }
    }
    return revenue;
}


    public static void main(String[] args) {
        Simulator sim = new Simulator();
        sim.runSimulation(5); // 5 jours
    }
}
