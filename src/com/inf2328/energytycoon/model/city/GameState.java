package com.inf2328.energytycoon.model.city;

import com.inf2328.energytycoon.model.player.Player;

/*
 * Classe représentant le jeu
 * 
 * @param player : le joueur
 * @param city : la ville
 * @param timeCycle : le cycle de temps
 * @param daysHighUnsatisfacton : nombre de jours où le niveau de satisfaction des résidences est inférieur à 50%
 * @param daysEnergyShortage : nombre de jours où la production d'énergie est inférieure au besoin
 */
public class GameState {

    private final Player player;
    private final City city;
    private final TimeCycle timeCycle;
    private int daysHighUnsatisfaction = 0;
    private int consecutiveShortageTicks = 0;

    private static final double UNSATISFACTION_THRESHOLD = 40.0;
    private static final double UNSATISFIED_RATIO = 0.4;
    private static final int DAYS_UNSATISFIED_LIMIT = 2;

    private static final int SHORTAGE_TICKS_LIMIT = 12;
    private static final double COLLAPSED_RATIO = 0.1;

    public GameState(Player player, City city) {
        this.player = player;
        this.city = city;
        this.timeCycle = new TimeCycle();
    }

    // Avancement du temps
    public void advanceTime() {
        timeCycle.nextTimeSlot();
        city.update(timeCycle);
    }

    // Récupération du joueur
    public Player getPlayer() {
        return player;
    }

    // Récupération de la ville
    public City getCity() {
        return city;
    }

    // Récupération du cycle de temps
    public TimeCycle getTimeCycle() {
        return timeCycle;
    }

    // Règle 1 : 40 % des résidences ont une satisfaction < 40 %
    private void checkUnsatisfaction() {
        double ratio = city.getUnsatisfiedRatio(UNSATISFACTION_THRESHOLD);
        if (ratio > UNSATISFIED_RATIO) {
            daysHighUnsatisfaction++;
        } else {
            daysHighUnsatisfaction = 0;
        }
    }

    // Règle 2 : production < demande pendant 12 créneaux (3 jours)
    private void checkEnergyShortage() {
        double demand = city.getTotalEnergyDemand();
        double production = city.getTotalEnergyProduction(timeCycle);

        if (production < demand) {
            consecutiveShortageTicks++;
        } else {
            consecutiveShortageTicks = 0;
        }
    }

    // Règle 3 : 10 % des résidences ont satisfaction == 0 → Game Over immédiat
    private boolean hasCollapsedResidences() {
        return city.getZeroSatisfactionRatio() > COLLAPSED_RATIO;
    }

    // Règle 4 : le joueur n'a plus d'argent → Game Over immédiat
    private boolean isGameOver() {
        return daysHighUnsatisfaction >= DAYS_UNSATISFIED_LIMIT
                || consecutiveShortageTicks >= SHORTAGE_TICKS_LIMIT
                || hasCollapsedResidences()
                || player.getMoney() < 0;
    }

    // Vérification des règles
    public boolean updateAndCheckGameOver() {
        checkUnsatisfaction();
        checkEnergyShortage();

        return isGameOver();
    }

    // Représentation du jeu
    @Override
    public String toString() {
        return timeCycle.toString() + "\n"
                + city.toString() + "\n"
                + player.toString();
    }
}
