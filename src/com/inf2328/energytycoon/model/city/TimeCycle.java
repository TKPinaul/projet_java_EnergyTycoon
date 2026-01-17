package com.inf2328.energytycoon.model.city;


/*
 * Classe représentant le cycle de temps
 * 
 * @param day : le jour actuel
 * @param week : la semaine actuelle
 * @param month : le mois actuel
 * @param timeOfDay : moment de la journée
 */
public class TimeCycle {

    public enum TimeOfDay {
        MORNING,
        DAY,
        EVENING,
        NIGHT,
    }
    
    private int day;
    private int week;
    private int month;
    private TimeOfDay timeOfDay;

    // Constructeur
    public TimeCycle() {
        day = 1;
        week = 1;
        month = 1;
        timeOfDay = TimeOfDay.DAY;
    }

    // Méthode de passage à la prochaine horraire
    public void nextTimeSlot() {
        switch (timeOfDay) {
            case MORNING -> timeOfDay = TimeOfDay.DAY;
            case DAY -> timeOfDay = TimeOfDay.EVENING;
            case EVENING -> timeOfDay = TimeOfDay.NIGHT;
            case NIGHT -> {
                timeOfDay = TimeOfDay.MORNING;
                nextDay();
            }
        }
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

    // Récupération de l'heure de la journée
    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
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
        return String.format("Day %d, Week %d, Month %d, Time: %s", day, week, month, timeOfDay);
    }
}
