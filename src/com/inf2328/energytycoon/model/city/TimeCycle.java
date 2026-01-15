package com.inf2328.energytycoon.model.city;


/*
 * Classe représentant le cycle de temps
 * 
 * @param day : le jour actuel
 * @param week : la semaine actuelle
 * @param month : le mois actuel
 */
public class TimeCycle {
    
    private int day;
    private int week;
    private int month;

    // Constructeur
    public TimeCycle() {
        day = 1;
        week = 1;
        month = 1;
    }

    // Méthode de passage à la journée suivante
    public void nextDay() {
        day++;
        if (day > 7) { // 7 jours par semaine
            day = 1;
            week++;
        }
        if (week > 4) { // 4 semaines par mois
            week = 1;
            month++;
        }
    }

    // Récupération du jour
    public int getDay() {
        return day;
    }

    // Récupération de la semaine
    public int getWeek() {
        return week;
    }

    // Récupération du mois
    public int getMonth() {
        return month;
    }

    // Représentation du cycle de temps
    @Override
    public String toString() {
        return String.format("Day %d, Week %d, Month %d", day, week, month);
    }
}
