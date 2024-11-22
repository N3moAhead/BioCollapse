package src.com.biocollapse.model;

public class LiveStatistics {

    private int alive;
    private int infected;
    private int healthy;
    private int immune;
    private int deaths;
    private Integer hospitalCapacityRatio;

    /**
     * Live statistics store the current simulation state instead of the overall
     * history.
     */
    public LiveStatistics(int alive, int infected, int healthy, int immune, int deaths, Integer hospitalCapacityRatio) {
        this.alive = alive;
        this.infected = infected;
        this.healthy = healthy;
        this.immune = immune;
        this.deaths = deaths;
        this.hospitalCapacityRatio = hospitalCapacityRatio;
    }

    public String toJSON() {
        return "{ \"alive\": " + alive +
                ", \"infected\": " + infected +
                ", \"healthy\": " + healthy +
                ", \"immune\": " + immune +
                ", \"deaths\": " + deaths +
                ", \"hospitalCapacityRatio\": " + hospitalCapacityRatio +
                " }";
    }

    public int getAlive() {
        return alive;
    }

    public int getInfected() {
        return infected;
    }

    public int getHealthy() {
        return healthy;
    }

    public int getImmune() {
        return immune;
    }

    public int getDeaths() {
        return deaths;
    }

    public double getHospitalCapacityRatio() {
        return hospitalCapacityRatio;
    }
}
