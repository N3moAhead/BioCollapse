package src.com.biocollapse.service;

import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.GoalState;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.model.MovementAction;
import src.com.biocollapse.util.GlobalConfig;
import src.com.biocollapse.util.GlobalRandom;

public class MovementService {
    private final Map map;
    private static final int MOVE_TO_PREVIOUS_PENALTY = 2;

    public MovementService(Map map) {
        this.map = map;
    }

    // A person is always travelling back and forth between home and work.
    // If the person is infected, there is a probability that the person will go to
    // hospital.
    // If the person arrives at home, there is a probability that
    // he can continue to stay at home instead of going to work.
    public void updateHumanGoal(Human human) {
        GoalState humanGoalState = human.getGoalState();
        if (human.isInfected()) {
            // If the person is infected, they have a chance to change their destination to
            // a hospital.
            if (humanGoalState != GoalState.to_hospital
                    && GlobalRandom.checkProbability(GlobalConfig.config.getHospitalProbability())) {
                human.setGoalState(GoalState.to_hospital);
                MapPosition nearestHospital = map.findNearest(Block.Hospital, human.getPos().copy());
                if (nearestHospital != null) {
                    human.setGoalPos(nearestHospital);
                }
            }
        } else {
            // When a person reaches their destination, they return home or go to work.
            if (human.getPos().equals(human.getGoalPos())) {
                if (humanGoalState == GoalState.to_work) {
                    human.setGoalState(GoalState.to_home);
                    human.setGoalPos(human.getHomePos());
                } else {
                    // There is a chance that a person will stay at home
                    if (!GlobalRandom.checkProbability(GlobalConfig.config.getIsolationProbability())) {
                        human.setGoalState(GoalState.to_work);
                        human.setGoalPos(human.getWorkPos());
                    }
                }
            }
        }
    }

    /**
     * Calculates the Manhattan distance between two positions.
     *
     * @param p1 the first position
     * @param p2 the second position
     * @return the Manhattan distance
     */
    private int distance(MapPosition p1, MapPosition p2) {
        return Math.abs(p2.getRow() - p1.getRow()) + Math.abs(p2.getCol() - p1.getCol());
    }

    /**
     * Moves the human towards the goal position by evaluating the shortest valid
     * path.
     *
     * @param human the human to move
     */
    public void move(Human human) {
        MovementAction bestDirection = MovementAction.NONE;
        int shortestDistance = Integer.MAX_VALUE;

        MapPosition humanPos = human.getPos();
        MapPosition humanGoalPos = human.getGoalPos();
        MapPosition previouPosition = human.getPreviouPosition();

        if (humanPos.equals(humanGoalPos)) {
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
                    int newDistance = distance(newPos, humanGoalPos);
                    if (newPos.equals(previouPosition)) {
                        newDistance += MOVE_TO_PREVIOUS_PENALTY;
                    }
                    if (newDistance < shortestDistance) {
                        shortestDistance = newDistance;
                        bestDirection = direction;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                // Ignore positions outside the map
            }
        }

        // Move the human in the best direction found
        human.setPreviouPosition(humanPos.copy());
        human.moveIntoDirection(bestDirection);
    }
}
