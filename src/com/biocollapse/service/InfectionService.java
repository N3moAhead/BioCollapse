// Authors: Inaas, Lukas
package src.com.biocollapse.service;

import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.util.GlobalConfig;
import src.com.biocollapse.util.GlobalRandom;
import src.com.biocollapse.model.Age;


import java.util.ArrayList;
import java.util.List;


public class InfectionService {

    /**
     * updates infected, healed and dead humans in the humans list
     * @param humans
     * @param config
     * @param currentTick
     */
    public void updateHumansStatus(List<Human> humans,  int currentTick) {

        ArrayList<MapPosition> infectedPos = new ArrayList<MapPosition>();

        updateInfectedPositions(humans, infectedPos);
        infectNearbyHumans(humans, infectedPos, currentTick);
        updateDeadOrHealed(humans, currentTick);
    } 

    /**
     * collects the positions where there are infected people
     * @param humans
     * @param infectedPos
     */
    private void updateInfectedPositions(List<Human> humans, ArrayList<MapPosition> infectedPos) {

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
    private void infectNearbyHumans(List<Human> humans, ArrayList<MapPosition> infectedPos, int currentTick) {
        int infectionRadius = GlobalConfig.config.getInfectionRadius();
        int infectionProbability = GlobalConfig.config.getInfectionProbability();
        int effectiveInfectionProbability = GlobalConfig.config.getMaskMandate() ? infectionProbability / GlobalConfig.config.getmaskEffect() : infectionProbability;

        // Check surrounding positions within the infection radius
        for (MapPosition pos : infectedPos){
            for (MapPosition neighbor : getPositionsWithinRadius(pos, infectionRadius)) {
                ArrayList<Human> neighborHumans = findHumansAtPos(neighbor, humans);
                for (Human neighborHuman: neighborHumans) {
                    if (neighborHuman.isAlive() && !neighborHuman.isInfected() && !neighborHuman.isImmune()) {
                        if (GlobalRandom.checkProbability(effectiveInfectionProbability)) {
                            neighborHuman.setInfected(true);
                            neighborHuman.setInfectedAt(currentTick);
                        }
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
     * finds the humans located in a specific MapPosition 
     * @param position
     * @param humans
     * @return Human
     */
    private ArrayList<Human> findHumansAtPos(MapPosition position, List<Human> humans) {
        ArrayList<Human> humansAtPos = new ArrayList<Human>();
        for (Human human : humans) {
            if (human.getPos().equals(position)) {
                humansAtPos.add(human);
            }
        }
        return humansAtPos;
    }

    /**
     * updates the human list with new healed or dead humans according to the parameters
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

                //being an elder or a child increases mortality risk and infection time
                int effectiveMortalityRisk = humanAge == Age.Adult ? mortalityRisk : mortalityRisk + mortalityRisk / GlobalConfig.config.getAgeEffect();
                int effectiveinfectionTime = humanAge == Age.Adult ? infectionTime : infectionTime + infectionTime / GlobalConfig.config.getAgeEffect();

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
