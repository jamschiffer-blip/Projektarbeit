package org.example;

import java.awt.*;

public class Polygon {
    /*
    Initiliasierung mit standardmäßiger Werte
     */
    private Color fuellfarbe = null;
    protected boolean ausgefuellt = false;
    private Color farbe = Color.BLACK;
    private float dicke = 2.0f;

    private int[] xKoordinaten,yKoordinaten;

    public Polygon(int[] xKoordinaten,int[] yKoordinaten) {
        this.xKoordinaten = xKoordinaten;
        this.yKoordinaten = yKoordinaten;
    }
    /*
     Getter und Setter für die jeweiligen Instanzvariablen
     */
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

    public float getDicke() {
        return dicke;
    }

    public void setDicke(float dicke) {
        this.dicke = dicke;
    }

    public Color getFuellfarbe() {
        return fuellfarbe;
    }

    public void setFuellfarbe(Color fuellfarbe) {
        this.fuellfarbe = fuellfarbe;
    }
}

