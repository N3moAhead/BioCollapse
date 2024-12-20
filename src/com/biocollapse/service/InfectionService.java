// Authors: Inaas, Lukas
package src.com.biocollapse.service;

import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.util.GlobalConfig;
import src.com.biocollapse.util.GlobalRandom;
import src.com.biocollapse.model.Age;

import java.util.List;

public class InfectionService {
    private boolean[][] infected;
    private final int infectionProbability = GlobalConfig.config.getInfectionProbability();
    private final int effectiveInfectionProbability = GlobalConfig.config.getMaskMandate()
            ? infectionProbability / GlobalConfig.config.getmaskEffect()
            : infectionProbability;
    private final int mortalityRisk = GlobalConfig.config.getMortalityRisk();
    private final int infectionTime = GlobalConfig.config.getInfectionTime();
    private final int immunityChance = GlobalConfig.config.getImmunityChance();

    public void initInfectionUpdates() {
        this.infected = new boolean[Map.MAP_HEIGHT][Map.MAP_WIDTH];
    }

    /**
     * Marks infected fields in the infected matrix
     * Warning: initInfectionUdpates has to be called before this function!
     * 
     * @param humans
     * @param infectedPos
     */
    public void updateInfectedPositions(Human human, int currentTick) {
        int infectionRadius = GlobalConfig.config.getInfectionRadius();
        MapPosition pos = human.getPos();
        if (human.isContagious(currentTick) && human.isAlive() && !human.isHospitalized()) {
            // Check if we have to infect only one field or a radius
            if (infectionRadius > 1) {
                markInfectedRadius(pos, infectionRadius);
            } else {
                infected[pos.getRow()][pos.getCol()] = true;
            }
        }
    }

    /**
     * updates infected, healed and dead humans in the humans list
     * 
     * @param humans
     * @param config
     * @param currentTick
     */
    public void updateHumansStatus(List<Human> humans, int currentTick) {
        for (Human currentHuman : humans) {
            infectHumans(currentHuman, currentTick);
            updateDeadOrHealed(currentHuman, currentTick);
        }
    }

    /**
     * calculate the positions located in a specific radius
     * 
     * @param pos
     * @param radius
     * @return a list of MapPositions in a specific radius
     */
    private void markInfectedRadius(MapPosition pos, int radius) {
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
    private void infectHumans(Human human, int currentTick) {
        MapPosition pos = human.getPos();
        if (infected[pos.getRow()][pos.getCol()]) {
            if (human.isAlive() && !human.isInfected() && !human.isImmune()) {
                if (GlobalRandom.checkProbability(effectiveInfectionProbability)) {
                    human.setInfected(true);
                    human.setRecoveredFromVirus(false);
                    human.setInfectedAt(currentTick);
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
    private void updateDeadOrHealed(Human human, int currentTick) {
        if (human.isInfected()) {
            Age humanAge = human.getAge();

            // being an elder or a child increases mortality risk and infection time
            int effectiveMortalityRisk = humanAge == Age.Adult
                    ? mortalityRisk
                    : mortalityRisk + mortalityRisk / GlobalConfig.config.getAgeEffect();
            int effectiveinfectionTime = humanAge == Age.Adult
                    ? infectionTime
                    : infectionTime + infectionTime / GlobalConfig.config.getAgeEffect();

            // hospitalization decreases mortality risk and infection time
            effectiveMortalityRisk = human.isHospitalized() ? effectiveMortalityRisk / 4 : effectiveMortalityRisk;
            effectiveinfectionTime = human.isHospitalized() ? effectiveinfectionTime / 2 : effectiveinfectionTime;

            int ticksSinceInfection = currentTick - human.getInfectedAt();

            int incubationTime = GlobalConfig.config.getIncubationTime();
            int infectionTime = incubationTime + GlobalConfig.config.getInfectionTime();

            // After the infection time is over, the probability of the user dying will be
            // called once
            if (ticksSinceInfection > infectionTime) {
                if (GlobalRandom.checkProbability(effectiveMortalityRisk)) {
                    human.setAlive(false);
                    human.setInfected(false);
                } else {
                    human.setInfected(false);
                    human.setInfectedDecisionMade(false);
                    human.setRecoveredFromVirus(true);
                    // Adults have a better chance at becoming immune than children and the elderly
                    int effectiveImmunityChance = humanAge == Age.Adult
                            ? immunityChance + immunityChance / 2
                            : immunityChance;

                    if (GlobalRandom.checkProbability(effectiveImmunityChance)) {
                        human.setImmune(true);
                    }
                }
            }
        }
    }
}
