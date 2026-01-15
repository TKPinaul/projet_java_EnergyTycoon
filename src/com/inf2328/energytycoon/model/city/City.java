package com.inf2328.energytycoon.model.city;
import com.inf2328.energytycoon.model.building.PowerPlant;
import com.inf2328.energytycoon.model.building.Residence;
import com.inf2328.energytycoon.model.energy.EnergyType;

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

    // Ajout d'une résidence
    public void addResidence(Residence r) {
        residences.add(r);
    }

    // Ajout d'une centrale
    public void addPowerPlant(PowerPlant p) {
        powerPlants.add(p);
    }

    // Augmentation du besoin énergétique des résidences
    public void increaseEnergyNeedOverTime() {
        for (Residence r : residences) {
            r.increaseEnergyNeedOverTime();
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
    public double getTotalEnergyProduction() {
        double total = 0;

        for (PowerPlant p : powerPlants) {
            // Ajout de la production de chaque centrale
            total += p.getProduction().getAmount();
        }
        return total;
    }

    // Production totale par type d'énergie
    public double getTotalProductionByType(EnergyType type) {
        double total = 0;
        for (PowerPlant p : powerPlants) {
            if (p.getProduction().getType() == type) {
                total += p.getProduction().getAmount();
            }
        }
        return total;
    }

    // Distribution de l'énergie et mise à jour des satisfaction des résidents
    public void distributeEnergy() {
        double totalEnergy = getTotalEnergyProduction();
        
        List<Residence> shuffled = new ArrayList<>(residences);
        Collections.shuffle(shuffled);

        for (Residence r : shuffled) {
            double received = Math.min(r.getEnergyNeed(), totalEnergy);
            r.updateSatisfaction(received);

            // On soustrait ce qui a été distribué
            totalEnergy -= received;

            if (totalEnergy <= 0) break;
        }       
    }

    // Récupération des résidences
    public List<Residence> getResidences() {
        return residences;
    }

    // Récupération des centrales
    public List<PowerPlant> getPowerPlants() {
        return powerPlants;
    }

    // Représentation de la ville
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("City Status:\nResidences:\n");
        for (Residence r : residences) sb.append("  ").append(r).append("\n");

        sb.append("PowerPlants:\n");
        for (PowerPlant p : powerPlants) sb.append("  ").append(p).append("\n");

        sb.append(String.format("Total Demand: %.2f, Total Production: %.2f\n",
                getTotalEnergyDemand(), getTotalEnergyProduction()));
        return sb.toString();

    }
    
}
