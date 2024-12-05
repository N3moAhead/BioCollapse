// Authors: Inaas, Lars, Johann, Lukas
package src.com.biocollapse.model;

public class Config {
  private int infectionRadius;
  private int infectionProbability;
  private int incubationTime;
  private int mortalityRisk;
  private int infectionTime;
  private int immunityChance;

  private int hospitalCapacity;
  private int isolationProbability;
  private int hospitalProbability;
  private int childrenRatio;
  private int adultRatio;
  private int elderlyRatio;

  private boolean lockdown;
  private boolean isolationMandate;
  private boolean maskMandate;
  private boolean schoolClosure;
  private int maskEffect = 2; // how much the maskMandate decreases the infectionProbability
  private int ageEffect = 3; // how much the age of a human increases mortalityRisk
  private int isolationEffect = 2; // how much the isolationMandate increases the isolationProbability
  private int lockdownEffect = 75;
  private int ticksAtLocation = 50; // how many ticks does a person stay at home or at work after reaching the goal
  private int populationSize = 500;
  public static final  int SIMULATION_ONE_DAY_TICKS = 250;
  public static final int SIMULATION_MAX_DAYS = 14;

  /**
   * @param infectionRadius      The radius in which an infected person can infect
   *                             another person
   * @param infectionProbability The probability of an infected person to infect
   *                             another person
   * @param incubationTime       The amount of ticks until symptoms show and a
   *                             person chooses to go to the hospital or not
   * @param mortalityRisk        The probability of the virus killing a person
   * @param infectionTime        The amount of ticks after infection in which a
   *                             person can die
   * @param immunityChance       The probability of getting immune after surviving
   *                             an infection
   * @param hospitalCapacity     The capacity each hospital provides
   * @param isolationProbability The probabilty for people to isolate themselves
   * @param hospitalProbability  The probability for people to go into the
   *                             hospital when infected
   * @param childrenRatio        The ratio of children inside of the simulation
   * @param adultRatio           The ratio of adult people inside of the
   *                             simulation
   * @param elderlyRatio         The ratio of elderly people inside of the
   *                             simulation
   * @param lockdown
   * @param isolationMandate
   * @param maskMandate
   * @param schoolClosure
   */
  public Config(int infectionRadius, int infectionProbability, int incubationTime, int mortalityRisk, int infectionTime,
      int immunityChance, int hospitalCapacity, int isolationProbability, int hospitalProbability,
      int childrenRatio, int adultRatio, int elderlyRatio, boolean lockdown, boolean isolationMandate,
      boolean maskMandate, boolean schoolClosure) {
    setConfig(infectionRadius, infectionProbability, incubationTime, mortalityRisk, infectionTime, immunityChance,
        hospitalCapacity, isolationProbability, hospitalProbability, childrenRatio, adultRatio, elderlyRatio, lockdown,
        isolationMandate, maskMandate, schoolClosure);
  }

  public Config() {
  }

  /**
   * Allows us to initialize the Config class lazily
   * 
   * @param infectionRadius      The radius in which an infected person can infect
   *                             another person
   * @param infectionProbability The probability of an infected person to infect
   *                             another person
   * @param incubationTime       The amount of ticks it needs until the virus is
   *                             deadly
   * @param mortalityRisk        The probability of the virus killing a person
   * @param infectionTime        The amount of ticks after the incubation time in
   *                             which a person can die
   * @param immunityChance       The probability of getting immune after surviving
   *                             an infection
   * @param hospitalCapacity     The capacity each hospital provides
   * @param isolationProbability The probabilty for people to isolate themselves
   * @param hospitalProbability  The probability for people to go into the
   *                             hospital when infected
   * @param childrenRatio        The ratio of children inside of the simulation
   * @param adultRatio           The ratio of adult people inside of the
   *                             simulation
   * @param elderlyRatio         The ratio of elderly people inside of the
   *                             simulation
   * @param lockdown
   * @param isolationMandate
   * @param maskMandate
   * @param schoolClosure
   */
  public void setConfig(int infectionRadius, int infectionProbability, int incubationTime, int mortalityRisk,
      int infectionTime,
      int immunityChance, int hospitalCapacity, int isolationProbability, int hospitalProbability,
      int childrenRatio, int adultRatio, int elderlyRatio, boolean lockdown, boolean isolationMandate,
      boolean maskMandate, boolean schoolClosure) {
    this.infectionRadius = infectionRadius;
    this.infectionProbability = infectionProbability;
    this.incubationTime = incubationTime;
    this.mortalityRisk = mortalityRisk;
    this.infectionTime = infectionTime;
    this.immunityChance = immunityChance;
    this.hospitalCapacity = hospitalCapacity;
    this.isolationProbability = isolationProbability;
    this.hospitalProbability = hospitalProbability;
    this.childrenRatio = childrenRatio;
    this.adultRatio = adultRatio;
    this.elderlyRatio = elderlyRatio;
    this.lockdown = lockdown;
    this.isolationMandate = isolationMandate;
    this.maskMandate = maskMandate;
    this.schoolClosure = schoolClosure;
  }

  public int getPopulationSize() {
    return populationSize;
  }

  public void setPopulationSize(int populationSize) {
    this.populationSize = populationSize;
  }

  public int getTicksAtLocation() {
    return ticksAtLocation;
  }

  public void setTicksAtLocation(int ticksAtLocation) {
    this.ticksAtLocation = ticksAtLocation;
  }

  public int getInfectionRadius() {
    return infectionRadius;
  }

  public void setInfectionRadius(int infectionRadius) {
    this.infectionRadius = infectionRadius;
  }

  public int getInfectionProbability() {
    return infectionProbability;
  }

  public void setInfectionProbability(int infectionProbability) {
    this.infectionProbability = infectionProbability;
  }

  public int getIncubationTime() {
    return incubationTime;
  }

  public void setIncubationTime(int incubationTime) {
    this.incubationTime = incubationTime;
  }

  public int getMortalityRisk() {
    return mortalityRisk;
  }

  public void setMortalityRisk(int mortalityRisk) {
    this.mortalityRisk = mortalityRisk;
  }

  public int getInfectionTime() {
    return infectionTime;
  }

  public void setInfectionTime(int infectionTime) {
    this.infectionTime = infectionTime;
  }

  public int getImmunityChance() {
    return immunityChance;
  }

  public void setImmunityChance(int immunityChance) {
    this.immunityChance = immunityChance;
  }

  public int getHospitalCapacity() {
    return hospitalCapacity;
  }

  public void setHospitalCapacity(int hospitalCapacity) {
    this.hospitalCapacity = hospitalCapacity;
  }

  public int getIsolationProbability() {
    return isolationProbability;
  }

  public void setIsolationProbability(int isolationProbability) {
    this.isolationProbability = isolationProbability;
  }

  public int getHospitalProbability() {
    return hospitalProbability;
  }

  public void setHospitalProbability(int hospitalProbability) {
    this.hospitalProbability = hospitalProbability;
  }

  public int getChildrenRatio() {
    return childrenRatio;
  }

  public void setChildrenRatio(int childrenRatio) {
    this.childrenRatio = childrenRatio;
  }

  public int getAdultRatio() {
    return adultRatio;
  }

  public void setAdultRatio(int adultRatio) {
    this.adultRatio = adultRatio;
  }

  public int getElderlyRatio() {
    return elderlyRatio;
  }

  public void setElderlyRatio(int elderlyRatio) {
    this.elderlyRatio = elderlyRatio;
  }

  public boolean getLockdown() {
    return lockdown;
  }

  public void setLockdown(boolean lockdown) {
    this.lockdown = lockdown;
  }

  public boolean getIsolationMandate() {
    return isolationMandate;
  }

  public void setIsolationMandate(boolean isolateMandate) {
    this.isolationMandate = isolateMandate;
  }

  public boolean getMaskMandate() {
    return maskMandate;
  }

  public void setMaskMandate(boolean maskMandate) {
    this.maskMandate = maskMandate;
  }

  public boolean getSchoolClosure() {
    return schoolClosure;
  }

  public void setSchoolClosure(boolean schoolClosure) {
    this.schoolClosure = schoolClosure;
  }

  public int getmaskEffect() {
    return this.maskEffect;
  }

  public void setMaskEffect(int maskEffect) {
    this.maskEffect = maskEffect;
  }

  public int getAgeEffect() {
    return ageEffect;
  }

  public void setAgeEffect(int ageEffect) {
    this.ageEffect = ageEffect;
  }

  public int getIsolationEffect() {
    return isolationEffect;
  }

  public void setIsolationEffect(int isolationEffect) {
    this.isolationEffect = isolationEffect;
  }

  public int getLockdownEffect() {
    return lockdownEffect;
  }

  public void setLockdownEffect(int lockdownEffect) {
    this.lockdownEffect = lockdownEffect;
  }

  @Override
  public String toString() {
    return "Config{" +
        "infectionRadius=" + infectionRadius +
        ", infectionProbability=" + infectionProbability +
        ", incubationTime=" + incubationTime +
        ", mortalityRisk=" + mortalityRisk +
        ", infectionTime=" + infectionTime +
        ", immunityChance=" + immunityChance +
        ", hospitalCapacity=" + hospitalCapacity +
        ", isolationProbability=" + isolationProbability +
        ", hospitalProbability=" + hospitalProbability +
        ", childrenRatio=" + childrenRatio +
        ", adultRatio=" + adultRatio +
        ", elderlyRatio=" + elderlyRatio +
        '}';
  }
}
