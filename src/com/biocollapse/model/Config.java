package src.com.biocollapse.model;

public class Config {
  private int infectionRadius;
  private int infectionProbability;
  private int incubationTime;
  private int mortalityRate;
  private int timeToDeath;
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

  /**
   * @param infectionRadius The radius in which an infected person can infect another person
   * @param infectionProbability The probability of an infected person to infect another person
   * @param incubationTime The amount of ticks it needs until the virus is deadly
   * @param mortalityRate The probability of the virus killing a person
   * @param timeToDeath The amount of ticks after the incubation time in which a person can die
   * @param immunityChance The probability of getting immune after surviving an infection
   * @param hospitalCapacity The capacity each hospital provides
   * @param isolationProbability The probabilty for people to isolate themselves
   * @param hospitalProbability The probability for people to go into the hospital when infected
   * @param childrenRatio The ratio of children inside of the simulation
   * @param adultRatio The ratio of adult people inside of the simulation
   * @param elderlyRatio The ratio of elderly people inside of the simulation
   * @param lockdown 
   * @param isolationMandate
   * @param maskMandate
   * @param schoolClosure
   */
  public Config(int infectionRadius, int infectionProbability, int incubationTime, int mortalityRate, int timeToDeath,
      int immunityChance, int hospitalCapacity, int isolationProbability, int hospitalProbability,
      int childrenRatio, int adultRatio, int elderlyRatio, boolean lockdown, boolean isolationMandate,
      boolean maskMandate, boolean schoolClosure) {
    setConfig(infectionRadius, infectionProbability, incubationTime, mortalityRate, timeToDeath, immunityChance,
        hospitalCapacity, isolationProbability, hospitalProbability, childrenRatio, adultRatio, elderlyRatio, lockdown,
        isolationMandate, maskMandate, schoolClosure);
  }

  public Config() {
  }

  /**
   * Allows us to initialize the Config class lazily
   * @param infectionRadius The radius in which an infected person can infect another person
   * @param infectionProbability The probability of an infected person to infect another person
   * @param incubationTime The amount of ticks it needs until the virus is deadly
   * @param mortalityRate The probability of the virus killing a person
   * @param timeToDeath The amount of ticks after the incubation time in which a person can die
   * @param immunityChance The probability of getting immune after surviving an infection
   * @param hospitalCapacity The capacity each hospital provides
   * @param isolationProbability The probabilty for people to isolate themselves
   * @param hospitalProbability The probability for people to go into the hospital when infected
   * @param childrenRatio The ratio of children inside of the simulation
   * @param adultRatio The ratio of adult people inside of the simulation
   * @param elderlyRatio The ratio of elderly people inside of the simulation
   * @param lockdown 
   * @param isolationMandate
   * @param maskMandate
   * @param schoolClosure
   */
  public void setConfig(int infectionRadius, int infectionProbability, int incubationTime, int mortalityRate,
      int timeToDeath,
      int immunityChance, int hospitalCapacity, int isolationProbability, int hospitalProbability,
      int childrenRatio, int adultRatio, int elderlyRatio, boolean lockdown, boolean isolationMandate,
      boolean maskMandate, boolean schoolClosure) {
    this.infectionRadius = infectionRadius;
    this.infectionProbability = infectionProbability;
    this.incubationTime = incubationTime;
    this.mortalityRate = mortalityRate;
    this.timeToDeath = timeToDeath;
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

  public int getMortalityRate() {
    return mortalityRate;
  }

  public void setMortalityRate(int mortalityRate) {
    this.mortalityRate = mortalityRate;
  }

  public int getTimeToDeath() {
    return timeToDeath;
  }

  public void setTimeToDeath(int timeToDeath) {
    this.timeToDeath = timeToDeath;
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

  @Override
  public String toString() {
    return "Config{" +
        "infectionRadius=" + infectionRadius +
        ", infectionProbability=" + infectionProbability +
        ", incubationTime=" + incubationTime +
        ", mortalityRate=" + mortalityRate +
        ", timeToDeath=" + timeToDeath +
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
