package com.inf2328.energytycoon.model.city;
import com.inf2328.energytycoon.model.player.Player;

/*
 * Classe représentant le jeu
 * 
 * @param player : le joueur
 * @param city : la ville
 * @param timeCycle : le cycle de temps
 */
public class GameState {
    
    private final Player player;
    private final City city;
    private final TimeCycle timeCycle;

    public GameState(Player player, City city) {
        this.player = player;
        this.city = city;
        this.timeCycle = new TimeCycle();
    }

    // Passage à la journée suivante
    public void nextDay() {
        timeCycle.nextDay();
        city.distributeEnergy();
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

    // Représentation du jeu
    @Override
    public String toString() {
         return timeCycle.toString() + "\n" + city.toString() + "\n" + player.toString();
    }
}
