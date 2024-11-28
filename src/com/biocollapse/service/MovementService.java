package src.com.biocollapse.service;

import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.model.MovementAction;

public class MovementService {
    private final Map map;
    private static final int MOVE_TO_PREVIOUS_PENALTY = 2;

    public MovementService(Map map) {
        this.map = map;
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
     * Moves the human towards the goal position by evaluating the shortest valid path.
     *
     * @param human the human to move
     */
    public void move(Human human) {
        MovementAction bestDirection = MovementAction.NONE;
        int shortestDistance = Integer.MAX_VALUE;

        MapPosition humanPos = human.getPos();
        MapPosition humanGoalPos = human.getGoalPos();
        MapPosition previouPosition = human.getPreviouPosition();

        // Check all four directions
        for (MovementAction direction : MovementAction.values()) {
            if (direction == MovementAction.NONE) continue; // Skip "NONE" direction

            try {
                MapPosition newPos = getNextPosition(humanPos, direction);
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

    /**
     * Returns the next position based on the current position and the given direction.
     *
     * @param currentPosition the current position
     * @param direction       the direction to move
     * @return the new position
     */
    private MapPosition getNextPosition(MapPosition currentPosition, MovementAction direction) {
        switch (direction) {
            case UP:
                return currentPosition.getTop();
            case RIGHT:
                return currentPosition.getRight();
            case DOWN:
                return currentPosition.getBot();
            case LEFT:
                return currentPosition.getLeft();
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }
}
