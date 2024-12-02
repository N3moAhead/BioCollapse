// Authors: Lars, Lukas
package src.com.biocollapse.model;

import src.com.biocollapse.util.GlobalConfig;

public class Hospital {
    private MapPosition pos;
    private int capacity;
    private int usedCapacity = 0;

    public Hospital(MapPosition hospitalPos) {
        this.pos = hospitalPos;
        this.capacity = GlobalConfig.config.getHospitalCapacity();
    }

    public MapPosition getPosition() {
        return this.pos;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getUsedCapacity() {
        return this.usedCapacity;
    }

    public void incrementUsedCapacity() {
        this.usedCapacity += 1;
    }

    public void decreaseUsedCapacity() {
        if (usedCapacity > 0) {
            usedCapacity -= 1;
        }
    }
}
