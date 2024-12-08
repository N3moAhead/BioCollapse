// Authors: Inaas, Lars, Sebastian, Lukas
package src.com.biocollapse.model;

import src.com.biocollapse.util.GlobalConfig;
import src.com.biocollapse.util.GlobalRandom;

public class Human {
	private Age age;
	private boolean infected;
	private int infectedAt;
	private boolean hospitalized = false;
	private boolean alive = true;
	private boolean immune;
	private MapPosition pos;
	private MapPosition goalPos;
	private MapPosition workPos;
	private MapPosition homePos;
	private Integer reachedLocationAt = null;
	// Represents whether a person has already made a decision whether to go home or
	// go to hospital once they have been infected
	private boolean infectedDecisionMade = false;
	private boolean pretendsToBeHealthy = false;
	// Marks that a person had been infected in the past but has been healed at
	// least once
	private boolean recoveredFromVirus = false;

	public Human(boolean infected, boolean immune, MapPosition pos, MapPosition workPos, MapPosition homePos) {
		this.infected = infected;
		this.immune = immune;
		this.pos = pos;
		this.workPos = workPos;
		this.homePos = homePos;
		this.age = Age.Adult;

		// On init a human spwans at an unknown
		// location and his first action will be to walk home.
		this.goalPos = homePos;
	}

	/**
	 * Updates the destination of the human according to different probabilites
	 * 
	 * @param map  the map where a human walks
	 * @param tick current tick
	 */
	public void updateHumanGoal(Map map, int tick) {
		Block blockGoal = map.getBlock(this.goalPos);
		if (isContagious(tick)) {
			setGoalWhenInfected(blockGoal, map, tick);
		} else {
			// When we reach this code, the person is truly healthy, and the person can stop
			// pretending to be healthy if they were. See the setGoalWhenInfected function
			// for more information.
			pretendsToBeHealthy = false;
			setGoalWhenHealthy(blockGoal, map, tick);
		}
	}

	/**
	 * Once a person is infected, three things can happen. Either the person goes to
	 * hospital. Or they isolate themselves at home. Or, finally, the person
	 * pretends to be healthy and goes on living as before.
	 * 
	 * @param blockGoal
	 * @param map
	 */
	private void setGoalWhenInfected(Block blockGoal, Map map, int tick) {
		int hospitalProbability = GlobalConfig.config.getHospitalProbability();

		// If human is not on its way to a hospital and the hospitalProbability checks
		// the human will go to the hospital
		if (!infectedDecisionMade) {
			if (GlobalRandom.checkProbability(hospitalProbability)) {
				setGoalToNearestHospital(map);
			} else if (GlobalConfig.config.getIsolationMandate()
					|| GlobalRandom.checkProbability(GlobalConfig.config.getIsolationProbability())) {
				// Theres a chance a human isolates himself at home
				setGoalPos(homePos);
			} else {
				pretendsToBeHealthy = true;
				// If a human does not go into the hospital or isolates himself at home he just
				// goes on living life as usual
				setGoalWhenHealthy(blockGoal, map, tick);
			}
			infectedDecisionMade = true;
		}

		if (pretendsToBeHealthy) {
			setGoalWhenHealthy(blockGoal, map, tick);
		}
	}

	/**
	 * Sets the goal to the nearest Hospital. If there is no hospital in reach the
	 * function has a fallback to the home position
	 * 
	 * @param map
	 */
	private void setGoalToNearestHospital(Map map) {
		MapPosition nearestHospital = map.findNearest(Block.Hospital, pos.copy());
		if (nearestHospital != null) {
			setGoalPos(nearestHospital);
		} else {
			// It should not be the case that there is no hospital within reach, but in the
			// event that it happens, the person will simply go home.
			setGoalPos(homePos);
		}
	}

	private void setGoalWhenHealthy(Block blockGoal, Map map, int tick) {
		// When the lockdown mandate is active healty humans want to stay at home
		if (blockGoal != Block.House && (GlobalConfig.config.getLockdown() || GlobalConfig.config.getSchoolClosure())) {
			setGoalPos(homePos);
		}

		// When a person reaches their destination, they return home or go to work.
		if (pos.equals(goalPos)) {
			// let them stay at home for a while
			if (reachedLocationAt == null) {
				setReachedLocationAt(tick);
			} else {
				// If the human has not stayed long enough at one specific location he will stay
				// there for a while
				if (tick - reachedLocationAt > GlobalConfig.config.getTicksAtLocation()) {
					if (blockGoal == Block.Workplace) {
						// set it to null because we left the current location
						setReachedLocationAt(null);
						setGoalPos(homePos);
					} else {
						// Set it to null because we left the current location
						setReachedLocationAt(null);
						setGoalPos(workPos);
					}
				}
			}
		}
	}

	/**
	 * Moves the human towards the goal position by evaluating the least amount
	 * of steps it would take to walk to the destination.
	 *
	 * @param map the map where a human moves
	 */
	public void move(Map map) {
		MovementAction bestDirection = MovementAction.NONE;
		int smallestSteps = Integer.MAX_VALUE;

		if (pos.equals(goalPos)) {
			return;
		}

		Integer[][] stepMatrix = map.getStepMatrix(goalPos, pos);
		if (stepMatrix == null) {
			return;
		}

		// Check all four directions
		for (MovementAction direction : MovementAction.values()) {
			if (direction != MovementAction.NONE) { // Skip "NONE" direction
				try {
					MapPosition newPos = pos.getByMove(direction);
					Block blockAtNewPos = map.getBlock(newPos);

					// Only consider positions that are walkable
					if (blockAtNewPos == Block.Path || newPos.equals(goalPos)) {
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
		}
		moveIntoDirection(bestDirection);
	}

	public boolean getRecoveredFromVirus() {
		return this.recoveredFromVirus;
	}

	public void setRecoveredFromVirus(boolean recoveredFromVirus) {
		this.recoveredFromVirus = recoveredFromVirus;
	}

	public Integer getReachedLocationAt() {
		return reachedLocationAt;
	}

	public void setInfectedDecisionMade(boolean infectedDecisionMade) {
		this.infectedDecisionMade = infectedDecisionMade;
	}

	public boolean getInfectedDecisionMade() {
		return this.infectedDecisionMade;
	}

	public void setReachedLocationAt(Integer reachedLocationAt) {
		this.reachedLocationAt = reachedLocationAt;
	}

	public Age getAge() {
		return this.age;
	}

	public void setAge(Age age) {
		this.age = age;
	}

	public boolean isContagious(int currentTick) {
		if (!infected)
			return false;

		int ticksSinceInfection = currentTick - infectedAt;
		int incubationTime = GlobalConfig.config.getIncubationTime();

		if (ticksSinceInfection > incubationTime) {
			return true;
		}
		return false;
	}

	public boolean isInfected() {
		return infected;
	}

	public void setInfected(boolean infected) {
		this.infected = infected;
	}

	public int getInfectedAt() {
		return infectedAt;
	}

	public void setInfectedAt(int infectedAt) {
		this.infectedAt = infectedAt;
	}

	public boolean isHospitalized() {
		return hospitalized;
	}

	public void setHospitalized(boolean hosptialized) {
		this.hospitalized = hosptialized;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isImmune() {
		return immune;
	}

	public void setImmune(boolean immune) {
		this.immune = immune;
	}

	public MapPosition getPos() {
		return pos;
	}

	public void setPos(MapPosition pos) {
		this.pos = pos;
	}

	public void moveIntoDirection(MovementAction direction) {
		this.pos.moveIntoDirection(direction);
	}

	public MapPosition getGoalPos() {
		return goalPos;
	}

	public void setGoalPos(MapPosition goalPos) {
		this.goalPos = goalPos;
	}

	public MapPosition getWorkPos() {
		return workPos;
	}

	public void setWorkPos(MapPosition workPos) {
		this.workPos = workPos;
	}

	public MapPosition getHomePos() {
		return homePos;
	}

	public void setHomePos(MapPosition homePos) {
		this.homePos = homePos;
	}

	@Override
	public String toString() {
		return "Human{" +
				"infected=" + infected +
				", infectedAt=" + infectedAt +
				", alive=" + alive +
				", immune=" + immune +
				", pos=" + (pos != null ? pos.toString() : "null") +
				", goalPos=" + (goalPos != null ? goalPos.toString() : "null") +
				", workPos=" + (workPos != null ? workPos.toString() : "null") +
				", homePos=" + (homePos != null ? homePos.toString() : "null") +
				'}';
	}
}
