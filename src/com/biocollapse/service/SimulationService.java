package src.com.biocollapse.service;

import src.com.biocollapse.model.Hospital;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.GoalState;
import src.com.biocollapse.model.LiveStatistics;
import src.com.biocollapse.model.MapPosition;

public class SimulationService {

    public LiveStatistics calculateLiveStatistics(Human[] humans, Hospital[] hospitals) {
        int infectedCounter = 0;
        int healthyCounter = humans.length;
        int immuneCounter = 0;
        int aliveCounter = humans.length;
        int deathCounter = 0;
        int hospitalCapacity = 0;
        int usedHospitalCapacity = 0;
        Integer hospitalCapacityRatio = 0;

        for (Human human : humans) {
            if (human.isInfected()) {
                infectedCounter++;
                healthyCounter = (healthyCounter > 0) ? healthyCounter -= 1 : healthyCounter;
            }
            if (!human.isAlive()) {
                deathCounter++;
                aliveCounter = (aliveCounter > 0) ? aliveCounter -= 1 : aliveCounter;
                infectedCounter = (infectedCounter > 0) ? infectedCounter -= 1 : infectedCounter;
            }
            // This case can only occur after someone was infected
            if (human.isImmune()) {
                immuneCounter++;
            }
        }

        for (Hospital hospital : hospitals) {
            hospitalCapacity += hospital.getCapacity();
            usedHospitalCapacity += hospital.getUsedCapacity();
        }

        if (hospitalCapacity > 0) {
            hospitalCapacityRatio = usedHospitalCapacity / hospitalCapacity;
        } else {
            System.err.println("There was an Error in Calculating the hospital capacity: " + hospitalCapacity);
            hospitalCapacityRatio = -1;
        }

        LiveStatistics current = new LiveStatistics(aliveCounter, infectedCounter, aliveCounter, immuneCounter,
                deathCounter, hospitalCapacityRatio);

        return current;
    }

    /**
     * This method updates the hospitals and the humans. It uses checkPatient() and checkHospitalization for updating humans and hospitals accordingly.
     * @param hospitals
     * @param humans
     */
    public void updateHospitals(Hospital[] hospitals, Human[] humans){
        for (Hospital hospital : hospitals){
            for (Human human : humans){
                if (human.isHospitalized()) {
                    checkPatient(human, hospital);
                }
                else {
                    checkHospitalization(human, hospital);
                }
            }
        }
    }

    /**
     * This method checks if a human is already in a hospital. If he is healthy again, then he can go back home. If he is dead but still in hospital,
     * the capacity of the hospital will change
     * @param human
     * @param hospital
     */
    private void checkPatient(Human human, Hospital hospital){
        // Have to check which hospital the human is in to update the usedCapacity of this hospital when human goes home
        if (isHumanAtHospitalPos(human.getPos(), hospital.getPosition())) {
            // Only if human is alive and healthy he can go home
            if (human.isAlive() && !human.isInfected()) {
                human.setGoalState(GoalState.to_home);
                human.setHospitalized(false);
                hospital.decreaseUsedCapacity();
            }
            // If human is dead but still hospitalized then he died in this tick and the hospital capacity needs to be changed
            else if (!human.isAlive() && human.isHospitalized()) {
                human.setHospitalized(false);
                hospital.decreaseUsedCapacity();
            } 
        }
    }

    /**
     * This method checks if a human has the destination Hospital and is at it. If he is infected then he will go into hospital if there is capacity
     * If the human is already in the hospital, then nothing happens
     * @param human
     * @param hospital
     */
    private void checkHospitalization(Human human, Hospital hospital){
        MapPosition humanPos = human.getPos();
        MapPosition hospitalPos = hospital.getPosition();
        // Check if human is at the hospital and has hospital as goal
        if (isHumanAtHospitalPos(humanPos, hospitalPos) && human.getGoalState() == GoalState.to_hospital) {
            if (hasHospitalCapacity(hospital) && human.isInfected()) {
                human.setHospitalized(true);
                hospital.incrementUsedCapacity();
            }
        }

    }

    /**
     * This method checks if a human is at the same position as the hospital
     * @param humanPos
     * @param hospitalPos
     * @return boolean
     */
    private boolean isHumanAtHospitalPos(MapPosition humanPos, MapPosition hospitalPos){
        boolean isAtHospitalPos = false;
        if (humanPos == hospitalPos) {
            isAtHospitalPos = true;
        }

        return isAtHospitalPos;
    }

    /**
     * This method checks if a hospital has capacity left
     * @param hospital
     * @return boolean
     */
    private boolean hasHospitalCapacity(Hospital hospital){
        boolean hasCapacityLeft = false;

        if (hospital.getCapacity() - hospital.getUsedCapacity() > 0) {
            hasCapacityLeft = true;
        }

        return hasCapacityLeft;
    }
}
