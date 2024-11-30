package src.com.biocollapse.service;

import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.util.GlobalConfig;
import src.com.biocollapse.util.GlobalRandom;
import src.com.biocollapse.model.Age;

import java.util.List;

public class InfectionService {

    /**
     * updates infected, healed and dead humans in the humans list
     * 
     * @param humans
     * @param config
     * @param currentTick
     */
    public void updateHumansStatus(List<Human> humans, int currentTick) {
        boolean[][] infected = new boolean[Map.MAP_HEIGHT][Map.MAP_WIDTH];

        updateInfectedPositions(humans, infected);
        infectNearbyHumans(humans, infected, currentTick);
        updateDeadOrHealed(humans, currentTick);
    }

    /**
     * collects the positions where there are infected people
     * 
     * @param humans
     * @param infectedPos
     */
    private void updateInfectedPositions(List<Human> humans, boolean infected[][]) {
        int infectionRadius = GlobalConfig.config.getInfectionRadius();

        // iterate over the humans list to find infected humans
        for (Human currentHuman : humans) {
            MapPosition pos = currentHuman.getPos();
            if (currentHuman.isInfected() && currentHuman.isAlive() && !currentHuman.isHospitalized()) {
                // Check if we have to infect only one field or a radius
                if (infectionRadius > 1) {
                    infectRadius(pos, infected, infectionRadius);
                } else {
                    infected[pos.getRow()][pos.getCol()] = true;
                }
            }
        }
    }

    /**
     * calculate the positions located in a specific radius
     * 
     * @param pos
     * @param radius
     * @return a list of MapPositions in a specific radius
     */
    private void infectRadius(MapPosition pos, boolean[][] infected, int radius) {
        int row = pos.getRow();
        int col = pos.getCol();
        // Loop through the square grid around the human, bounded by the infection
        // radius and mark every visited field as infected.
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                if (Map.isValidPosition(row + i, col + j)) {
                    infected[row + i][col + j] = true;
                }
            }
        }
    }

    /**
     * infects healthy humans which are in the infection radius of another infected
     * human according to a probability
     * 
     * @param config
     * @param humans
     * @param infectedPos
     * @param currentTick
     */
    private void infectNearbyHumans(List<Human> humans, boolean[][] infected, int currentTick) {
        int infectionProbability = GlobalConfig.config.getInfectionProbability();
        int effectiveInfectionProbability = GlobalConfig.config.getMaskMandate()
                ? infectionProbability / GlobalConfig.config.getmaskEffect()
                : infectionProbability;

        // Check surrounding positions within the infection radius
        for (Human currentHuman : humans) {
            MapPosition pos = currentHuman.getPos();
            if (infected[pos.getRow()][pos.getCol()]) {
                if (currentHuman.isAlive() && !currentHuman.isInfected() && !currentHuman.isImmune()) {
                    if (GlobalRandom.checkProbability(effectiveInfectionProbability)) {
                        currentHuman.setInfected(true);
                        currentHuman.setInfectedAt(currentTick);
                    }
                }
            }
        }
    }

    /**
     * updates the human list with new healed or dead humans according to the
     * parameters
     * 
     * @param humans
     * @param config
     * @param currentTick
     */
    private void updateDeadOrHealed(List<Human> humans, int currentTick) {
        int mortalityRisk = GlobalConfig.config.getMortalityRisk();
        int infectionTime = GlobalConfig.config.getInfectionTime();
        int immunityChance = GlobalConfig.config.getImmunityChance();

        for (Human human : humans) {
            if (human.isInfected()) {
                Age humanAge = human.getAge();

                // being an elder or a child increases mortality risk and infection time
                int effectiveMortalityRisk = humanAge == Age.Adult ? mortalityRisk
                        : mortalityRisk + mortalityRisk / GlobalConfig.config.getAgeEffect();
                int effectiveinfectionTime = humanAge == Age.Adult ? infectionTime
                        : infectionTime + infectionTime / GlobalConfig.config.getAgeEffect();

                // hospitalization decreases mortality risk and infection time
                effectiveMortalityRisk = human.isHospitalized() ? effectiveMortalityRisk / 4 : effectiveMortalityRisk;
                effectiveinfectionTime = human.isHospitalized() ? effectiveinfectionTime / 2 : effectiveinfectionTime;

                // if still within infectionTime a human has a probability to die
                if (currentTick - human.getInfectedAt() <= effectiveinfectionTime) {
                    if (GlobalRandom.checkProbability(effectiveMortalityRisk)) {
                        human.setAlive(false);
                    }
                } else { // otherwise they are healed and have a chance to become immune
                    human.setInfected(false);

                    // Adults have a better chance at becoming immune than children and the elderly
                    int effectiveImmunityChance = humanAge == Age.Adult ? immunityChance + immunityChance / 2
                            : immunityChance;

                    if (GlobalRandom.checkProbability(effectiveImmunityChance)) {
                        human.setImmune(true);
                    }
                }
            }
        }
    }
}
