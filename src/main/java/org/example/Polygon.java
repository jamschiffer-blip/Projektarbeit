package org.example;

public class Polygon {
    private int[] xKoordinaten,yKoordinaten;
    protected boolean ausgefuellt = false;
    public Polygon(int[] xKoordinaten,int[] yKoordinaten) {
        this.xKoordinaten = xKoordinaten;
        this.yKoordinaten = yKoordinaten;
    }

    public int[] getxKoordinaten() {
        return xKoordinaten;
    }

    public int[] getyKoordinaten() {
        return yKoordinaten;
    }
    public int getAnzahlPunkte(){
        return xKoordinaten.length;
    }

    public boolean isAusgefuellt() {
        return ausgefuellt;
    }

    public void setAusgefuellt(boolean ausgefuellt) {
        this.ausgefuellt = ausgefuellt;
    }
}
