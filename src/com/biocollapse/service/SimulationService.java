package src.com.biocollapse.service;

import src.com.biocollapse.model.Hospital;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.LiveStatistics;

public class SimulationService {
    // TODO Decide wether the SimulationController stores all Data or the service.
    // If the controller stores all Data we could make this class static
    private LiveStatistics currentLiveStatistics;

    public LiveStatistics calculateLiveStatistics(Human[] humans, Hospital[] hospitals) {
        int infectedCounter = 0;
        int healthyCounter = humans.length;
        int immuneCounter = 0;
        int aliveCounter = humans.length;
        int deathCounter = 0;
        int hospitalCapacity = 0;
        int usedHospitalCapacity = 0;
        double hospitalCapacityRatio = 0.0;

        for (Human human : humans) {
            if (human.isInfected()) {
                infectedCounter++;
                healthyCounter = (healthyCounter > 0) ? healthyCounter -= 1 : healthyCounter;
            }
            if (!human.isAlive()) {
                deathCounter++;
                aliveCounter = (aliveCounter > 0) ? aliveCounter -= 1 : aliveCounter;
                infectedCounter = (infectedCounter > 0) ? infectedCounter -= 1 : infectedCounter;
            }
            // This case can only occur when someone was infected is healthy again
            if (human.isImmune()) {
                immuneCounter++;
                healthyCounter++;
            }
        }

        for (Hospital hospital : hospitals) {
            hospitalCapacity += hospital.getCapacity();
            usedHospitalCapacity += hospital.getUsedCapacity();
        }

        if (hospitalCapacity > 0) {
            hospitalCapacityRatio = usedHospitalCapacity / hospitalCapacity;
        } else {
            // Idea: Instead of returning 0, when we got an Error, we could return Pi,
            // because Pi can never be our result in the calculation, we know that when it
            // is PI it has to be an error.
            hospitalCapacityRatio = Math.PI;
        }

        LiveStatistics current = new LiveStatistics(aliveCounter, infectedCounter, aliveCounter, immuneCounter,
                deathCounter, hospitalCapacityRatio);

        return current;
    }
}
