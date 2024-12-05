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
	private MapPosition previouPosition;
	private Integer reachedLocationAt = null;

	public MapPosition getPreviouPosition() {
		return previouPosition;
	}

	public void setPreviouPosition(MapPosition previouPosition) {
		this.previouPosition = previouPosition;
	}

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
	 * updates the destination of the human according to different probabilites
	 * 
	 * @param map  the map where a human walks
	 * @param tick current tick
	 */
	public void updateHumanGoal(Map map, int tick) {
		Block blockGoal = map.getBlock(this.goalPos);
		if (this.infected) {
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
				MapPosition nearestHospital = map.findNearest(Block.Hospital, this.pos.copy());
				if (nearestHospital != null) {
					this.setGoalPos(nearestHospital);
				}
			} else if (blockGoal != Block.Hospital && GlobalRandom.checkProbability(isolationProbability)) {
				// Todo ensure that the isolation does not get revoked in the next
				this.setGoalPos(homePos);
			}
		} else {
			// When a person reaches their destination, they return home or go to work.
			if (this.pos.equals(this.goalPos)) {
				// let them stay at home for a while
				if (this.reachedLocationAt == null) {
					this.setReachedLocationAt(tick);
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
						this.setReachedLocationAt(null);
						this.setGoalPos(this.homePos);
					} else {
						// There is a chance that a person will stay at home
						if (GlobalRandom.checkProbability(effectiveLockdownProbability)) {
							this.setReachedLocationAt(tick);
						} else {
							// Set it to null because we left the current location
							this.setReachedLocationAt(null);
							this.setGoalPos(workPos);
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
	 * @param map the map where a human moves
	 */
	public void move(Map map) {
		MovementAction bestDirection = MovementAction.NONE;
		int smallestSteps = Integer.MAX_VALUE;

		if (this.pos.equals(this.goalPos)) {
			return;
		}

		Integer[][] stepMatrix = map.getStepMatrix(this.goalPos, this.pos);
		if (stepMatrix == null) {
			return;
		}

		// Check all four directions
		for (MovementAction direction : MovementAction.values()) {
			if (direction != MovementAction.NONE) { // Skip "NONE" direction
				try {
					MapPosition newPos = this.pos.getByMove(direction);
					Block blockAtNewPos = map.getBlock(newPos);

					// Only consider positions that are walkable
					if (blockAtNewPos == Block.Path || newPos.equals(this.goalPos)) {
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

	public Integer getReachedLocationAt() {
		return reachedLocationAt;
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
