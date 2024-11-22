package src.com.biocollapse.model;

import src.com.biocollapse.util.GlobalConfig;
import src.com.biocollapse.util.GlobalRandom;

enum GoalState {
  to_home,
  to_work,
  to_hospital,
}

public class Human {
  private boolean infected;
  private int infectedAt;
  private boolean alive = true;
  private boolean immune;
  private MapPosition pos;
  private MapPosition goalPos;
  private GoalState goalState = GoalState.to_home;
  private MapPosition workPos;
  private MapPosition homePos;

  public Human(boolean infected, boolean immune, MapPosition pos, MapPosition workPos, MapPosition homePos) {
    this.infected = infected;
    this.immune = immune;
    this.pos = pos;
    this.workPos = workPos;
    this.homePos = homePos;

    // On init a human spwans at an unknown
    // location and his first action will be to walk home.
    this.goalPos = homePos;
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

  public GoalState getGoalState() {
    return goalState;
  }

  public void setGoalState(GoalState goalState) {
    this.goalState = goalState;
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

  // A person is always travelling back and forth between home and work.
  // If the person is infected, there is a probability that the person will go to hospital. 
  // If the person arrives at home, there is a probability that
  // he can continue to stay at home instead of going to work.
  public void updateHumanGoal() {
    if (infected) {
      // If the person is infected, they have a chance to change their destination to a hospital.
      if (goalState != GoalState.to_hospital && GlobalRandom.checkProbability(GlobalConfig.config.getHospitalProbability())) {
        goalState = GoalState.to_hospital;
        // TODO Implement a function to get a Hospital position
        goalPos = new MapPosition(0, 0);
      }
    } else {
      // When a person reaches their destination, they return home or go to work.
      if (pos.equals(goalPos)) {
        if (goalState == GoalState.to_work) {
          goalState = GoalState.to_home;
          goalPos = homePos;
        } else {
          // If the person arrived at home there is a chance that the person will stay at home
          if (!GlobalRandom.checkProbability(GlobalConfig.config.getIsolationProbability())) {
            goalState = GoalState.to_work;
            goalPos = workPos;
          }
        }
      }
    }
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
        ", goalState=" + goalState.toString() +
        ", workPos=" + (workPos != null ? workPos.toString() : "null") +
        ", homePos=" + (homePos != null ? homePos.toString() : "null") +
        '}';
  }
}
