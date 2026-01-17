package com.inf2328.energytycoon.model.city;

import com.inf2328.energytycoon.model.building.PowerPlant;
import com.inf2328.energytycoon.model.building.Residence;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Classe représentant la ville
 * 
 * @param residences : la liste des résidences
 * @param powerPlants : la liste des centrales
 */
public class City {

    // Liste des résidences
    private final List<Residence> residences;
    // Liste des centrales
    private final List<PowerPlant> powerPlants;

    // Constructeur de la ville
    public City() {
        this.residences = new ArrayList<>();
        this.powerPlants = new ArrayList<>();
    }

    // Récupération des résidences
    public List<Residence> getResidences() {
        return Collections.unmodifiableList(residences);
    }

    // Récupération des centrales
    public List<PowerPlant> getPowerPlants() {
        return Collections.unmodifiableList(powerPlants);
    }

    // Ajout d'une résidence
    public void addResidence(Residence r) {
        residences.add(r);
    }

    // Ajout d'une centrale
    public void addPowerPlant(PowerPlant p) {
        powerPlants.add(p);
    }

    public void clearBuildings() {
        residences.clear();
        powerPlants.clear();
    }

    public int getTotalBuildingsCount() {
        return residences.size() + powerPlants.size();
    }

    // Mise à jour de la ville
    public void update(TimeCycle time) {
        // Mise à jour de l'état des centrales
        for (PowerPlant p : powerPlants) {
            p.updateStatus();
        }
        distributeEnergy(time);
    }

    // Actions journalières (appelé une fois par jour par le Controller)
    public void performDailyUpdates() {
        // Augmentation besoins résidences + Mise à jour compteur jours (pour upgrade)
        for (Residence r : residences) {
            r.increaseEnergyNeedOverTime();
            r.dailyUpdate();
        }

        // Mise à jour compteur jours centrales
        for (PowerPlant p : powerPlants) {
            p.dailyUpdate();
        }
    }

    // Récupération de la consommation totale d'énergie
    public double getTotalEnergyDemand() {
        double total = 0;

        for (Residence r : residences) {
            // Ajout de la demande de chaque résidence
            total += r.getEnergyNeed();
        }
        return total;
    }

    // Récupération de la production totale d'énergie (tout centrales confondues)
    public double getTotalEnergyProduction(TimeCycle time) {
        double total = 0;

        for (PowerPlant p : powerPlants) {
            // Ajout de la production de chaque centrale
            total += p.getEffectiveProduction(time);
        }
        return total;
    }

    // Distribution de l'énergie et mise à jour des satisfaction des résidents
    private void distributeEnergy(TimeCycle time) {
        double production = getTotalEnergyProduction(time);
        double demand = getTotalEnergyDemand();

        // Distribution de l'énergie
        for (Residence r : residences) {
            double received = 0;

            if (demand > 0 && production > 0) {
                // Calcul de la part d'énergie reçue par résidence
                received = (r.getEnergyNeed() / demand) * production;
            }

            r.updateSatisfaction(received);
        }
    }

    // Récupération du ratio de satisfaction des résidences
    public double getUnsatisfiedRatio(double threshold) {
        if (residences.isEmpty())
            return 0;

        int count = 0;

        // Comptage du nombre de résidences avec une satisfaction inférieure au seuil
        for (Residence r : residences) {
            if (r.getSatisfaction() < threshold) {
                count++;
            }
        }

        // Retourne le ratio de satisfaction des résidences
        return (double) count / residences.size();
    }

    // Récupération du ratio de satisfaction des résidences à 0
    public double getZeroSatisfactionRatio() {
        if (residences.isEmpty())
            return 0;

        int count = 0;

        // Comptage du nombre de résidences avec une satisfaction à 0
        for (Residence r : residences) {
            if (r.getSatisfaction() <= 0) {
                count++;
            }
        }

        // Retourne le ratio de satisfaction des résidences à 0
        return (double) count / residences.size();
    }

    // Indicateur de pénurie d'énergie
    public boolean isInEnergyCrisis(TimeCycle time) {
        return getTotalEnergyProduction(time) < getTotalEnergyDemand();
    }

    // Représentation de la ville
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("City Status:\n");

        sb.append("Residences:\n");
        for (Residence r : residences) {
            sb.append("  ").append(r).append("\n");
        }

        sb.append("PowerPlants:\n");
        for (PowerPlant p : powerPlants) {
            sb.append("  ").append(p).append("\n");
        }

        sb.append(String.format(
                "Total Demand: %.2f\n",
                getTotalEnergyDemand()));

        return sb.toString();
    }

}
