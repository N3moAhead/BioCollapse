package src.com.biocollapse.model;

public class Hospital {
    private MapPosition pos;
    private int capacity;
    private int usedCapacity;

    public MapPosition getPosition() {
        return this.pos;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getUsedCapacity() {
        return this.usedCapacity;
    }
}
