package model;

public class Auto {

    private String marke;
    private String model;
    private int baujahr;
    private double preis;
    private boolean elektro;

    public Auto(String marke, String model, int baujahr, double preis, boolean elektro) {
        this.marke = marke;
        this.model = model;
        this.baujahr = baujahr;
        this.preis = preis;
        this.elektro = elektro;
    }

    public String getMarke() {
        return marke;
    }

    public String getModel() {
        return model;
    }

    public int getBaujahr() {
        return baujahr;
    }

    public double getPreis() {
        return preis;
    }

    public boolean isElektro() {
        return elektro;
    }

    public boolean istOldtimer() {
        return baujahr <= 1995;
    }
}
