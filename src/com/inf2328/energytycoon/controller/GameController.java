package com.inf2328.energytycoon.controller;

import com.inf2328.energytycoon.model.building.PowerPlant;
import com.inf2328.energytycoon.model.building.Residence;
import com.inf2328.energytycoon.model.city.City;
import com.inf2328.energytycoon.model.city.GameState;
import com.inf2328.energytycoon.model.city.TimeCycle;
import com.inf2328.energytycoon.model.energy.EnergyType;
import javax.swing.JOptionPane;

/*
 * Contrôleur du jeu
 */
public class GameController {
    private final GameState gameState;
    private boolean gameOver = false;
    private boolean paused = false;
    private int totalTimeSlots = 0;

    public GameController(GameState gameState) {
        this.gameState = gameState;
    }

    public void advanceTime() {
        if (gameOver || paused)
            return;
        totalTimeSlots++;
        TimeCycle time = gameState.getTimeCycle();
        int currentDay = time.getDay();

        gameState.advanceTime();
        int newHour = time.getHour();

        // Paiement des impôts toutes les 6 heures (6h, 12h, 18h, 0h)
        if (newHour % 6 == 0) {
            double periodicRevenue = calculateDailyRevenue() / 4.0;
            gameState.getPlayer().addMoney(periodicRevenue);
        }

        // Augmentation des besoins énergétiques toutes les 12h (0h et 12h)
        if (newHour % 12 == 0) {
            for (Residence r : gameState.getCity().getResidences()) {
                r.increaseEnergyNeedOverTime();
            }
        }

        if (time.getDay() != currentDay) {
            // Bonus nocturne : nb bâtiments * 10
            int count = gameState.getCity().getTotalBuildingsCount();
            gameState.getPlayer().addMoney(count * 10.0);

            gameState.getCity().performDailyUpdates();
        }
        gameOver = gameState.updateAndCheckGameOver();
    }

    private double calculateDailyRevenue() {
        double revenue = 0;
        City city = gameState.getCity();
        for (Residence r : city.getResidences()) {
            double baseIncome = (20 * Math.pow(r.getLevel(), 2)) + 230;
            double s = r.getSatisfaction();
            if (s < 25)
                revenue += baseIncome * 0.25;
            else if (s < 50)
                revenue += baseIncome * 0.5;
            else
                revenue += baseIncome;
        }
        return revenue;
    }

    public boolean repairPowerPlant(PowerPlant p) {
        if (paused)
            return false;
        double cost = p.getRepairCost();
        if (gameState.getPlayer().getMoney() >= cost) {
            gameState.getPlayer().addMoney(-cost);
            p.performMaintenance();
            return true;
        } else {
            showFundsWarning(cost);
            return false;
        }
    }

    public void repairAllPowerPlants() {
        if (paused)
            return;
        double totalCost = 0;
        java.util.List<PowerPlant> toRepair = new java.util.ArrayList<>();

        for (PowerPlant p : gameState.getCity().getPowerPlants()) {
            if (p.isUnderMaintenance()) {
                totalCost += p.getRepairCost();
                toRepair.add(p);
            }
        }

        if (toRepair.isEmpty())
            return;

        if (gameState.getPlayer().getMoney() >= totalCost) {
            gameState.getPlayer().addMoney(-totalCost);
            for (PowerPlant p : toRepair) {
                p.performMaintenance();
            }
        } else {
            showFundsWarning(totalCost);
        }
    }

    public boolean buyResidence(int targetLevel) {
        if (paused)
            return false;
        Residence temp = new Residence(targetLevel);
        double cost = temp.getPriceForLevel(targetLevel);
        if (gameState.getPlayer().getMoney() >= cost) {
            if (autoPlaceBuilding(temp)) {
                gameState.getPlayer().addMoney(-cost);
                gameState.getCity().addResidence(temp);
                gameState.getPlayer().addResidence(temp);
                return true;
            }
        } else {
            showFundsWarning(cost);
        }
        return false;
    }

    public boolean buyPowerPlant(EnergyType type) {
        if (paused)
            return false;
        PowerPlant newPP = new PowerPlant(type);
        double cost = newPP.getBaseCost();
        if (gameState.getPlayer().getMoney() >= cost) {
            if (autoPlaceBuilding(newPP)) {
                gameState.getPlayer().addMoney(-cost);
                gameState.getCity().addPowerPlant(newPP);
                gameState.getPlayer().addPowerPlant(newPP);
                return true;
            }
        } else {
            showFundsWarning(cost);
        }
        return false;
    }

    private boolean autoPlaceBuilding(com.inf2328.energytycoon.model.building.Building b) {
        City city = gameState.getCity();
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 16; x++) {
                boolean occupied = false;
                for (com.inf2328.energytycoon.model.building.Building existing : city.getResidences()) {
                    if (existing.getX() == x && existing.getY() == y) {
                        occupied = true;
                        break;
                    }
                }
                if (!occupied) {
                    for (com.inf2328.energytycoon.model.building.Building existing : city.getPowerPlants()) {
                        if (existing.getX() == x && existing.getY() == y) {
                            occupied = true;
                            break;
                        }
                    }
                }
                if (!occupied) {
                    b.setX(x);
                    b.setY(y);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean upgradeBuilding(com.inf2328.energytycoon.model.building.Building b) {
        if (paused)
            return false;
        double cost = b.getUpgradeCost();
        if (b.canUpgrade()) {
            if (gameState.getPlayer().getMoney() >= cost) {
                gameState.getPlayer().addMoney(-cost);
                b.upgrade();
                return true;
            } else {
                showFundsWarning(cost);
            }
        }
        return false;
    }

    private void showFundsWarning(double required) {
        JOptionPane.showMessageDialog(null,
                "Fonds insuffisants ! Requiert : " + String.format("%.2f", required) + "$",
                "ERREUR TRESORERIE",
                JOptionPane.WARNING_MESSAGE);
    }

    public void restartGame() {
        gameState.getPlayer().setMoney(1200);
        gameState.getPlayer().clearBuildings();
        gameState.getCity().clearBuildings();
        gameState.getTimeCycle().restart();

        // Setup initial identique au lancement
        Residence r1 = new Residence(1);
        r1.setX(2);
        r1.setY(2);
        gameState.getCity().addResidence(r1);
        gameState.getPlayer().addResidence(r1);

        Residence r2 = new Residence(1);
        r2.setX(4);
        r2.setY(2);
        gameState.getCity().addResidence(r2);
        gameState.getPlayer().addResidence(r2);

        PowerPlant p1 = new PowerPlant(EnergyType.SOLAR);
        p1.setX(3);
        p1.setY(5);
        gameState.getCity().addPowerPlant(p1);
        gameState.getPlayer().addPowerPlant(p1);

        PowerPlant p2 = new PowerPlant(EnergyType.WIND);
        p2.setX(5);
        p2.setY(5);
        gameState.getCity().addPowerPlant(p2);
        gameState.getPlayer().addPowerPlant(p2);

        gameOver = false;
        totalTimeSlots = 0;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getTotalTimeSlots() {
        return totalTimeSlots;
    }
}