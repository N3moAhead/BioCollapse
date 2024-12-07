// Authors: Lars, Lukas, Johann
package src.com.biocollapse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import src.com.biocollapse.model.Age;
import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Hospital;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.LiveStatistics;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.util.GlobalConfig;
import src.com.biocollapse.util.GlobalRandom;

public class SimulationService {

    public void initializeSimulation(Block map[][], List<Human> humans, List<Hospital> hospitals) {
        final List<MapPosition> workplacePositions = extractPositions(map, Block.Workplace);
        final List<MapPosition> housePositions = extractPositions(map, Block.House);
        final List<MapPosition> hospitalPositions = extractPositions(map, Block.Hospital);
        createEntities(workplacePositions, housePositions, hospitalPositions, humans, hospitals);
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

    private void createEntities(List<MapPosition> workplacePositions, List<MapPosition> housePositions,
            List<MapPosition> hospitalPositions, List<Human> humans, List<Hospital> hospitals) {
        int populationSize = GlobalConfig.config.getPopulationSize();
        for (int i = 0; i < populationSize; i++) {
            humans.add(new Human(
                    (i % 5 == 0),
                    false,
                    housePositions.get(i % housePositions.size()).copy(),
                    getRandomPosition(workplacePositions).copy(),
                    housePositions.get(i % housePositions.size()).copy()));
        }
        int totalChildren = GlobalConfig.config.getChildrenRatio() / 100 * populationSize;
        int childrenCreated = 0;
        int totalElderly = GlobalConfig.config.getElderlyRatio() / 100 * populationSize;

        // The size of this List is how many adults there are while its values are their
        // indeces in the humans List
        // When an item is removed from this List the other associated values inside
        // don't change.
        List<Integer> adultIndeces = IntStream.range(0, populationSize)
                .boxed()
                .collect(Collectors.toList());
        for (int i = 0; i < totalChildren + totalElderly; i++) {
            int randomIndex = GlobalRandom.getRandIntBetween(0, adultIndeces.size()-1);
            int associatedIndex = adultIndeces.get(randomIndex);

            if (childrenCreated <= totalChildren) {
                humans.get(associatedIndex).setAge(Age.Child);
                childrenCreated++;
            } else {
                humans.get(associatedIndex).setAge(Age.Elder);
            }
            adultIndeces.remove(randomIndex);
        }
        // Creating hospitals
        for (MapPosition hospitalPosition : hospitalPositions) {
            hospitals.add(new Hospital(hospitalPosition.copy()));
        }
    }

    private MapPosition getRandomPosition(List<MapPosition> positions) {
        return positions.get(GlobalRandom.getRandIntBetween(0, positions.size() - 1));
    }

    /**
     * Calulates the current simulation statistics based on the following
     * parameters:
     * 
     * @param humans
     * @param hospitals
     * @param day
     * @return a live statistics object.
     */
    public LiveStatistics calculateLiveStatistics(List<Human> humans, List<Hospital> hospitals, int day) {
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
        // float ratio = hospitalCapacity > 0 ? (usedHospitalCapacity /
        // hospitalCapacity) : -1;
        float ratio = (float) usedHospitalCapacity / hospitalCapacity;
        hospitalCapacityRatio = Math.round(ratio * 100);
        return new LiveStatistics(aliveCounter, infectedCounter, healthyCounter, immuneCounter, deathCounter,
                hospitalCapacityRatio, day);
    }
}
