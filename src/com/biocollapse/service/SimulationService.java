package src.com.biocollapse.service;

import src.com.biocollapse.model.Hospital;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.LiveStatistics;

public class SimulationService {

    public static LiveStatistics calculateLiveStatistics(Human[] humans, Hospital[] hospitals) {
        int infectedCounter = 0;
        int healthyCounter = humans.length;
        int immuneCounter = 0;
        int aliveCounter = humans.length;
        int deathCounter = 0;
        int hospitalCapacity = 0;
        int usedHospitalCapacity = 0;
        Integer hospitalCapacityRatio = 0;

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
            System.err.println("There was an Error in Calculating the hospital capacity: " + hospitalCapacity);
            hospitalCapacityRatio = -1;
        }

        LiveStatistics current = new LiveStatistics(aliveCounter, infectedCounter, aliveCounter, immuneCounter,
                deathCounter, hospitalCapacityRatio);

        return current;
    }
}
