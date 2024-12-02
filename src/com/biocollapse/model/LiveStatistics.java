package src.com.biocollapse.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class LiveStatistics {

    private static final String STAT_ALIVE = "Lebendig";
    private static final String STAT_INFECTED = "Infiziert";
    private static final String STAT_RECOVERED = "Erholt";
    private static final String STAT_HEALTHY = "Gesund";
    private static final String STAT_IMMUNE = "Immun";
    private static final String STAT_DEATHS = "Tod";
    private static final String STAT_HOSPITAL_CAPACITY_RATIO = "Krankenhausauslastung";

    private static final Color COLOR_ALIVE = Color.BLUE;
    private static final Color COLOR_INFECTED = Color.RED;
    private static final Color COLOR_RECOVERED = Color.YELLOW;
    private static final Color COLOR_HEALTHY = Color.GREEN;
    private static final Color COLOR_IMMUNE = Color.ORANGE;
    private static final Color COLOR_DEATHS = Color.BLACK;
    private static final Color COLOR_HOSPITAL_CAPACITY_RATIO = Color.DARK_GRAY;

    private int alive;
    private int infected;
    private int healthy;
    private int immune;
    private int deaths;
    private Integer hospitalCapacityRatio;
    private long timestamp;

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
        this.timestamp = System.currentTimeMillis();
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

    public List<GraphItem> toGraph(){
        List<GraphItem> items = new ArrayList<>();
        items.add(new GraphItem(STAT_ALIVE, alive, true, false, COLOR_ALIVE));
        items.add(new GraphItem(STAT_INFECTED, infected, true, false, COLOR_INFECTED));
        items.add(new GraphItem(STAT_HEALTHY, healthy, true, false, COLOR_HEALTHY));
        items.add(new GraphItem(STAT_IMMUNE, immune, true, false, COLOR_IMMUNE));
        items.add(new GraphItem(STAT_DEATHS, deaths, true, false, COLOR_DEATHS));
        items.add(new GraphItem(STAT_HOSPITAL_CAPACITY_RATIO, hospitalCapacityRatio, true, false, COLOR_HOSPITAL_CAPACITY_RATIO));
        return items;
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

    public long getTimestamp() {
        return timestamp;
    }
}
