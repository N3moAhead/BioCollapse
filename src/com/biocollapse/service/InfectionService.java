package src.com.biocollapse.service;

import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.util.GlobalRandom;
import src.com.biocollapse.model.Age;
import src.com.biocollapse.model.Config;


import java.util.ArrayList;
import java.util.List;


public class InfectionService {

    /**
     * updates infected, healed and dead humans in the humans list
     * @param humans
     * @param config
     * @param currentTick
     */
    public void updateHumansStatus(Human[] humans, Config config,  int currentTick) {

        ArrayList<MapPosition> infectedPos = new ArrayList<MapPosition>();

        updateInfectedPositions(humans, infectedPos);
        infectNearbyHumans(config, humans, infectedPos, currentTick);
        updateDeadOrHealed(humans, config, currentTick);
    } 

    /**
     * collects the positions where there are infected people
     * @param humans
     * @param infectedPos
     */
    private void updateInfectedPositions(Human[] humans, ArrayList<MapPosition> infectedPos) {

        //iterate over the humans list to find infected humans
        for (Human currentHuman : humans) {
            if (currentHuman.isInfected() && currentHuman.isAlive() && !currentHuman.isHospitalized()) {
                infectedPos.add(currentHuman.getPos());
            }
        }
    }

    /**
     * infects healthy humans which are in the infection radius of another infected human according to a probability
     * @param config
     * @param humans
     * @param infectedPos
     * @param currentTick
     */
    private void infectNearbyHumans(Config config, Human[] humans, ArrayList<MapPosition> infectedPos, int currentTick) {
        int infectionRadius = config.getInfectionRadius();
        int infectionProbability = config.getInfectionProbability();
        int effectiveInfectionProbability = config.getMaskMandate() ? infectionProbability / 2 : infectionProbability;

        // Check surrounding positions within the infection radius
        for (MapPosition pos : infectedPos){
            for (MapPosition neighbor : getPositionsWithinRadius(pos, infectionRadius)) {
                Human neighborHuman = findHumanAtPosition(neighbor, humans);
                if (neighborHuman != null && neighborHuman.isAlive() && !neighborHuman.isInfected() && !neighborHuman.isImmune()) {
                    if (GlobalRandom.checkProbability(effectiveInfectionProbability)) {
                        neighborHuman.setInfected(true);
                        neighborHuman.setInfectedAt(currentTick);
                    }
                }
            }
        }
    }

    /**
     * calculate the positions located in a specific radius
     * @param pos
     * @param radius
     * @return a list of MapPositions in a specific radius
     */
    private List<MapPosition> getPositionsWithinRadius(MapPosition pos, int radius) {
        List<MapPosition> neighbors = new ArrayList<>();

        // Loop through the square grid around the human, bounded by the infection radius
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                MapPosition neighborPos = new MapPosition(pos.getRow() + i, pos.getCol() + j);
                if (isWithinMapBounds(neighborPos)) {
                    neighbors.add(neighborPos);
                }
            }
        }

        return neighbors;
    }

    /**
     * checks of a calculated position is within the bounds of the Map
     * @param pos
     * @return boolean
     */
    private boolean isWithinMapBounds(MapPosition pos) {
        return pos.getRow() >= 0 && pos.getRow() < Map.MAP_HEIGHT && pos.getCol() >= 0 && pos.getCol() < Map.MAP_WIDTH;
    }

    /**
     * finds the human located in  specific MapPosition 
     * @param position
     * @param humans
     * @return Human
     */
    private Human findHumanAtPosition(MapPosition position, Human[] humans) {
        for (Human human : humans) {
            if (human.getPos().equals(position)) {
                return human;
            }
        }
        return null;
    }

    /**
     * updates the human list with new healed of dead humans according to the parameters
     * @param humans
     * @param config
     * @param currentTick
     */
    private void updateDeadOrHealed(Human[] humans, Config config, int currentTick) {
        int mortalityRisk = config.getMortalityRisk();
        int infectionTime = config.getInfectionTime();
        int immunityChance = config.getImmunityChance();

        for (Human human : humans) {
            if (human.isInfected()) {
                Age humanAge = human.getAge();

                //being an elder or a child increases mortality risk and infection time
                int effectiveMortalityRisk = humanAge == Age.Adult ? mortalityRisk : mortalityRisk + mortalityRisk / 3;
                int effectiveinfectionTime = humanAge == Age.Adult ? infectionTime : infectionTime + infectionTime / 3;

                //hospitalization decreases mortality risk and infection time
                effectiveMortalityRisk = human.isHospitalized() ? effectiveMortalityRisk / 4 : effectiveMortalityRisk;
                effectiveinfectionTime = human.isHospitalized() ? effectiveinfectionTime / 2 : effectiveinfectionTime;

                //if still within infectionTime a human has a probability to die
                if(currentTick - human.getInfectedAt() <= effectiveinfectionTime) {
                    if (GlobalRandom.checkProbability(effectiveMortalityRisk)) {
                        human.setAlive(false);
                    }
                } else { //otherwise they are healed and have a chance to become immune
                    human.setInfected(false);

                    //Adults have a better chance at becoming immune than children and the elderly
                    int effectiveImmunityChance = humanAge == Age.Adult ? immunityChance + immunityChance / 2 : immunityChance;
                    
                    if (GlobalRandom.checkProbability(effectiveImmunityChance)) {
                        human.setImmune(true);
                    }
                }
            }
        }
    }
}
