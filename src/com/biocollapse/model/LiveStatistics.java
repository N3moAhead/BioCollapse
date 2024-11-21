package src.com.biocollapse.model;

public class LiveStatistics {

    public static final String STAT_ALIVE = "Lebendig";
    public static final String STAT_INFECTED = "Infiziert";
    public static final String STAT_RECOVERED = "Erholt";
    public static final String STAT_HEALTHY = "Gesung";
    public static final String STAT_IMMUNE = "Imumn";
    public static final String STAT_DEATHS = "Tod";
    public static final String STAT_HOSPITAL_CAPACITY_RATIO = "Krankenhausauslastung";

    private int infected;
    private int recovered;
    private int immune;
    private int deaths;

    /**
     * Live statistics store the current simulation state instead of the overall
     * history.
     */
    public LiveStatistics(int infected, int recovered, int immune, int deaths) {
        this.infected = infected;
        this.recovered = recovered;
        this.immune = immune;
        this.deaths = deaths;
    }

    /**
     * Live statistics store the current simulation state instead of the overall
     * history.
     */
    public LiveStatistics() {
         // TODO: Decide on whether to use setters or the constructor. Decision by back end.
    }

    public void setInfected(int infected) {
        this.infected = infected;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public void setImmune(int immune) {
        this.immune = immune;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getInfected() {
        return infected;
    }

    public int getRecovered() {
        return recovered;
    }

    public int getImmune() {
        return immune;
    }

    public int getDeaths() {
        return deaths;
    }
}
