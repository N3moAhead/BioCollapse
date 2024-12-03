// Authors: Lars, Lukas, Johann
package src.com.biocollapse.service;

import java.util.ArrayList;
import java.util.List;
import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Hospital;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.LiveStatistics;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.util.GlobalRandom;

public class SimulationService {

    public void initializeSimulation(Block map[][], List<Human> humans, List<Hospital> hospitals) {
        final List<MapPosition> workplacePositions = extractPositions(map, Block.Workplace);
        final List<MapPosition> housePositions = extractPositions(map, Block.House);
        final List<MapPosition> hospitalPositions = extractPositions(map, Block.House);
        createEntities(map, workplacePositions, housePositions, hospitalPositions, humans, hospitals);
    }

    private List<MapPosition> extractPositions(Block[][] map, Block targetBlock) {
        List<MapPosition> positions = new ArrayList<>();
        for (int row = 0; row < Map.MAP_HEIGHT; row++) {
            for (int col = 0; col < Map.MAP_WIDTH; col++) {
                if (map[row][col] == targetBlock) {
                    positions.add(new MapPosition(row, col));
                }
            }
        }
        return positions;
    }

    private void createEntities(Block[][] map, List<MapPosition> workplacePositions, List<MapPosition> housePositions,
            List<MapPosition> hospitalPositions, List<Human> humans, List<Hospital> hospitals) {
        for (int i = 0; i < 100; i++) {
            humans.add(new Human(
                    i % 5 == 0 ? true : false,
                    false,
                    housePositions.get(i % housePositions.size()).copy(),
                    getRandomPosition(workplacePositions).copy(),
                    housePositions.get(i % housePositions.size()).copy()));
        }
        // Creating hospitals
        for (MapPosition hospitalPosition : hospitalPositions) {
            hospitals.add(new Hospital(hospitalPosition.copy()));
        }
    }

    private MapPosition getRandomPosition(List<MapPosition> positions) {
        return positions.get(GlobalRandom.getRandIntBetween(0, positions.size() - 1));
    }

    public LiveStatistics calculateLiveStatistics(List<Human> humans, List<Hospital> hospitals) {
        int infectedCounter = 0;
        int healthyCounter = humans.size();
        int immuneCounter = 0;
        int aliveCounter = humans.size();
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
                healthyCounter = (healthyCounter > 0) ? healthyCounter -= 1 : healthyCounter;
                infectedCounter = (infectedCounter > 0) ? infectedCounter -= 1 : infectedCounter;
            }
            // This case can only occur after someone was infected
            if (human.isImmune() && human.isAlive()) {
                immuneCounter++;
            }
        }

        for (Hospital hospital : hospitals) {
            hospitalCapacity += hospital.getCapacity();
            usedHospitalCapacity += hospital.getUsedCapacity();
        }

        if (hospitalCapacity > 0) {
            hospitalCapacityRatio = usedHospitalCapacity / hospitalCapacity;
        } else {
            // System.err.println("There was an Error in Calculating the hospital capacity: " + hospitalCapacity);
            hospitalCapacityRatio = -1;
        }

        LiveStatistics current = new LiveStatistics(aliveCounter, infectedCounter, healthyCounter, immuneCounter,
                deathCounter, hospitalCapacityRatio);

        return current;
    }
}
