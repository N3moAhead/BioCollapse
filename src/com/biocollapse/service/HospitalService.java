package src.com.biocollapse.service;

import java.util.List;

import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Hospital;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.MapPosition;

public class HospitalService {
    private Map map;

    public HospitalService(Map map) {
        this.map = map;
    }
    /**
     * This method updates the hospitals and the humans. It uses checkPatient() and
     * checkHospitalization for updating humans and hospitals accordingly.
     * 
     * @param hospitals
     * @param humans
     */
    public void updateHospitals(List<Hospital> hospitals, Human human) {
        for (Hospital hospital : hospitals) {
            if (human.isHospitalized()) {
                checkPatient(human, hospital);
            } else {
                checkHospitalization(human, hospital);
            }
        }
    }

    /**
     * This method checks if a human is already in a hospital. If he is healthy
     * again, then he can go back home. If he is dead but still in hospital,
     * the capacity of the hospital will change
     * 
     * @param human
     * @param hospital
     */
    private void checkPatient(Human human, Hospital hospital) {
        MapPosition humanPos = human.getPos();
        MapPosition hospitalPos = hospital.getPosition();
        // Have to check which hospital the human is in to update the usedCapacity of
        // this hospital when human goes home
        if (humanPos.equals(hospitalPos)) {
            // Only if human is alive and healthy he can go home
            if (human.isAlive() && !human.isInfected()) {
                human.setGoalPos(human.getHomePos());
                human.setHospitalized(false);
                hospital.decreaseUsedCapacity();
            }
            // If human is dead but still hospitalized then he died in this tick and the
            // hospital capacity needs to be changed
            else if (!human.isAlive() && human.isHospitalized()) {
                human.setHospitalized(false);
                hospital.decreaseUsedCapacity();
            }
        }
    }

    /**
     * This method checks if a human has the destination Hospital and is at it. If
     * he is infected then he will go into hospital if there is capacity
     * If the human is already in the hospital, then nothing happens
     * 
     * @param human
     * @param hospital
     */
    private void checkHospitalization(Human human, Hospital hospital) {
        MapPosition humanPos = human.getPos();
        MapPosition hospitalPos = hospital.getPosition();
        // Check if human is at the hospital and has hospital as goal
        if (humanPos.equals(hospitalPos) && map.getBlock(human.getGoalPos()) == Block.Hospital) {
            if (hasHospitalCapacity(hospital) && human.isInfected()) {
                human.setHospitalized(true);
                hospital.incrementUsedCapacity();
            }
        }

    }

    /**
     * This method checks if a hospital has capacity left
     * 
     * @param hospital
     * @return boolean
     */
    private boolean hasHospitalCapacity(Hospital hospital) {
        boolean hasCapacityLeft = false;

        if (hospital.getCapacity() - hospital.getUsedCapacity() > 0) {
            hasCapacityLeft = true;
        }

        return hasCapacityLeft;
    }
}
