package org.example;

public class Ellipse {

    private int xKoordinate,yKoordinate,breite, hoehe;
    protected boolean ausgefuellt = false;
    public Ellipse(int xKoordinate,int yKoordinate,int breite,int hoehe) {
        this.xKoordinate = xKoordinate;
        this.yKoordinate = yKoordinate;
        this.breite = breite;
        this.hoehe = hoehe;
    }

    public int getxKoordinate() {
        return xKoordinate;
    }

    public int getyKoordinate() {
        return yKoordinate;
    }

    public int getBreite() {
        return breite;
    }

    public int getHoehe() {
        return hoehe;
    }

    public boolean isAusgefuellt() {
        return ausgefuellt;
    }

    public void setAusgefuellt(boolean ausgefuellt) {
        this.ausgefuellt = ausgefuellt;
    }
}
