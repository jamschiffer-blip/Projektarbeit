package org.example;

public class Polygon {
    int[] xKoordinaten,yKoordinaten;

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
}
