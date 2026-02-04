package org.example;

import java.awt.*;

public class Polygon {
    private int[] xKoordinaten,yKoordinaten;
    protected boolean ausgefuellt = false;
    private Color farbe = Color.BLACK;
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
    public Color getFarbe(){
        return farbe;
    }

    public void setFarbe(Color farbe) {
        this.farbe = farbe;
    }
}

