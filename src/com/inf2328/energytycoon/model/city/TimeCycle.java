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

    private int hour;
    private int day;
    private int week;
    private int month;

    // Constructeur
    public TimeCycle() {
        hour = 8; // On commence à 8h le matin
        hour = 8; // On commence à 8h le matin
        day = 1;
        week = 1;
        month = 1;
    }

    // Méthode de passage à la prochaine horraire
    public void nextTimeSlot() {
        hour++;
        if (hour >= 24) {
            hour = 0;
            nextDay();
        }
    }

    public int getHour() {
        return hour;
    }

    public String getFormattedTime() {
        return String.format("%02dh00", hour);
    }

    // Récupération de l'heure de la journée basée sur l'heure numérique
    public TimeOfDay getTimeOfDay() {
        if (hour >= 6 && hour < 12)
            return TimeOfDay.MORNING;
        if (hour >= 12 && hour < 18)
            return TimeOfDay.DAY;
        if (hour >= 18 && hour < 24)
            return TimeOfDay.EVENING;
        return TimeOfDay.NIGHT;
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

    public int getTotalDays() {
        return (month - 1) * 28 + (week - 1) * 7 + (day - 1);
    }

    public void restart() {
        hour = 8;
        day = 1;
        week = 1;
        month = 1;
    }

    // Représentation du cycle de temps
    @Override
    public String toString() {
        return String.format("Month %d, Week %d, Day %d, Time: %s (%s)",
                month, week, day, getFormattedTime(), getTimeOfDay());
    }
}
