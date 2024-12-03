// Authors: Inaas, Lars, Sebastian, Lukas
package src.com.biocollapse.model;

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

    // On init a human spwans at an unknown
    // location and his first action will be to walk home.
    this.goalPos = homePos;
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
