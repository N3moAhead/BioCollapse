// Authors: Lukas, Sebastian
package src.com.biocollapse.service;

import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.model.MovementAction;
import src.com.biocollapse.util.GlobalConfig;
import src.com.biocollapse.util.GlobalRandom;

public class MovementService {
    private final Map map;

    public MovementService(Map map) {
        this.map = map;
    }

    // A person is always travelling back and forth between home and work.
    // If the person is infected, there is a probability that the person will go to
    // hospital.
    // If the person arrives at home, there is a probability that
    // he can continue to stay at home instead of going to work.
    public void updateHumanGoal(Human human, int tick) {
        MapPosition humanGoalPos = human.getGoalPos();
        Block blockGoal = map.getBlock(humanGoalPos);
        if (human.isInfected()) {
            int hospitalProbability = GlobalConfig.config.getHospitalProbability();
            int isolationProbability = GlobalConfig.config.getIsolationProbability();
            // If the person is infected, they have a chance to change their destination to
            // a hospital or stay at home.
            if (GlobalConfig.config.getIsolationMandate()) {
                isolationProbability = isolationProbability * GlobalConfig.config.getIsolationEffect();
            }

            // If human is not on its way to a hospital and the hospitalProbability checks
            // the human will go to the hospital
            if (blockGoal != Block.Hospital && GlobalRandom.checkProbability(hospitalProbability)) {
                MapPosition nearestHospital = map.findNearest(Block.Hospital, human.getPos().copy());
                if (nearestHospital != null) {
                    human.setGoalPos(nearestHospital);
                }
            } else if (blockGoal != Block.Hospital && GlobalRandom.checkProbability(isolationProbability)) {
                // Todo ensure that the isolation does not get revoked in the next
                human.setGoalPos(human.getHomePos());
            }
        } else {
            // When a person reaches their destination, they return home or go to work.
            if (human.getPos().equals(human.getGoalPos())) {
                // let them stay at home for a while
                Integer reachedLocationAt = human.getReachedLocationAt();
                if (reachedLocationAt == null) {
                    human.setReachedLocationAt(tick);
                } else {
                    // If the human has not stayed long enough at one specific location he will stay
                    // there for a while
                    if (tick - reachedLocationAt < GlobalConfig.config.getTicksAtLocation())
                        return;
                    // There is a chance that a person will stay at home which can be increased when
                    // the lockdownMandate is true
                    int lockdownProbability = 0;
                    int effectiveLockdownProbability;
                    if (GlobalConfig.config.getLockdown() || GlobalConfig.config.getSchoolClosure()) {
                        effectiveLockdownProbability = GlobalConfig.config.getLockdownEffect();
                    } else {
                        effectiveLockdownProbability = lockdownProbability;
                    }
                    if (blockGoal == Block.Workplace) {
                        // set it to null because we left the current location
                        human.setReachedLocationAt(null);
                        human.setGoalPos(human.getHomePos());
                    } else {
                        // There is a chance that a person will stay at home
                        if (GlobalRandom.checkProbability(effectiveLockdownProbability)) {
                            human.setReachedLocationAt(tick);
                        } else {
                            // Set it to null because we left the current location
                            human.setReachedLocationAt(null);
                            human.setGoalPos(human.getWorkPos());
                        }
                    }
                }
            }
        }
    }

    /**
     * Moves the human towards the goal position by evaluating the least amount
     * of steps it would take to walk to the destination.
     *
     * @param human the human to move
     */
    public void move(Human human) {
        MovementAction bestDirection = MovementAction.NONE;
        int smallestSteps = Integer.MAX_VALUE;

        MapPosition humanGoalPos = human.getGoalPos();
        MapPosition humanPos = human.getPos();

        if (humanPos.equals(humanGoalPos)) {
            return;
        }

        Integer[][] stepMatrix = map.getStepMatrix(humanGoalPos, humanPos);
        if (stepMatrix == null) {
            return;
        }

        // Check all four directions
        for (MovementAction direction : MovementAction.values()) {
            if (direction == MovementAction.NONE)
                continue; // Skip "NONE" direction

            try {
                MapPosition newPos = humanPos.getByMove(direction);
                Block blockAtNewPos = map.getBlock(newPos);

                // Only consider positions that are walkable
                if (blockAtNewPos == Block.Path || newPos.equals(humanGoalPos)) {
                    Integer currentSteps = stepMatrix[newPos.getRow()][newPos.getCol()];
                    if (currentSteps != null && currentSteps < smallestSteps) {
                        smallestSteps = currentSteps;
                        bestDirection = direction;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                // Ignore positions outside the map
            }
        }

        human.moveIntoDirection(bestDirection);
    }

}
