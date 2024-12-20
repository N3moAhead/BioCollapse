// Authors: Lars, Lukas, Johann
package src.com.biocollapse.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import src.com.biocollapse.model.GraphItem.GraphType;

public class LiveStatistics {

    public static final String STAT_ALIVE = "Lebendig";
    private static final String STAT_INFECTED = "Infiziert";
    private static final String STAT_HEALTHY = "Gesund";
    private static final String STAT_IMMUNE = "Immun";
    private static final String STAT_DEATHS = "Tod";
    private static final String STAT_RECOVERED = "Genesen";
    private static final String STAT_HOSPITAL_CAPACITY_RATIO = "Krankenhausauslastung";

    private static final Color COLOR_ALIVE = Color.BLUE;
    private static final Color COLOR_INFECTED = Color.RED;
    private static final Color COLOR_HEALTHY = Color.GREEN;
    private static final Color COLOR_IMMUNE = Color.ORANGE;
    private static final Color COLOR_DEATHS = Color.BLACK;
    private static final Color COLOR_RECOVERED = Color.CYAN;
    private static final Color COLOR_HOSPITAL_CAPACITY_RATIO = Color.MAGENTA;

    private final int alive;
    private final int infected;
    private final int healthy;
    private final int immune;
    private final int deaths;
    private final int day;
    private final int recovered;
    private final Integer hospitalCapacityRatio;
    private final long timestamp;

    /**
     * Live statistics store the current simulation state instead of the overall
     * history.
     */
    public LiveStatistics(int alive, int infected, int healthy, int immune, int deaths, Integer hospitalCapacityRatio,
            int day, int recovered) {
        this.alive = alive;
        this.infected = infected;
        this.healthy = healthy;
        this.immune = immune;
        this.deaths = deaths;
        this.hospitalCapacityRatio = hospitalCapacityRatio;
        this.day = day;
        this.timestamp = System.currentTimeMillis();
        this.recovered = recovered;
    }

    /**
     * Create a JSON entry of this object.
     * 
     * @return JSON entry String
     */
    public String toJSON() {
        return "{ \"alive\": " + alive +
                ", \"infected\": " + infected +
                ", \"healthy\": " + healthy +
                ", \"immune\": " + immune +
                ", \"deaths\": " + deaths +
                ", \"hospitalCapacityRatio\": " + hospitalCapacityRatio +
                ", \"recovered\": " + recovered +
                ", \"day\": " + day +
                " }";
    }

    /**
     * Add to graph and set visibility, color and name.
     */
    public List<GraphItem> toGraph() {
        List<GraphItem> items = new ArrayList<>();
        items.add(new GraphItem(STAT_INFECTED, infected, true, false, COLOR_INFECTED));
        items.add(new GraphItem(STAT_HEALTHY, healthy, true, false, COLOR_HEALTHY));
        items.add(new GraphItem(STAT_IMMUNE, immune, true, false, COLOR_IMMUNE));
        items.add(new GraphItem(STAT_DEATHS, deaths, true, false, COLOR_DEATHS));
        items.add(new GraphItem(STAT_ALIVE, alive, true, false, COLOR_ALIVE));
        items.add(new GraphItem(STAT_RECOVERED, recovered, true, false, COLOR_RECOVERED));
        items.add(new GraphItem(STAT_HOSPITAL_CAPACITY_RATIO, hospitalCapacityRatio, true, false,
                COLOR_HOSPITAL_CAPACITY_RATIO, GraphType.PERCENTAGE));
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

    public int getDay() {
        return day;
    }
}
