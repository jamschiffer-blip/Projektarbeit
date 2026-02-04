package org.example;

public class Kreis {
    private int Durchmesser,xKoordinate,yKoordinate;
    protected boolean ausgefuellt = false;

    public Kreis(int Durchmesser,int xKoordinate,int yKoordinate) {
        this.xKoordinate = xKoordinate;
        this.yKoordinate = yKoordinate;
        this.Durchmesser = Durchmesser;
    }

    public int getDurchmesser() {
        return Durchmesser;
    }

    public int getxKoordinate() {
        return xKoordinate;
    }

    public int getyKoordinate() {
        return yKoordinate;
    }

    public boolean isAusgefuellt() {
        return ausgefuellt;
    }

    public void setAusgefuellt(boolean ausgefuellt) {
        this.ausgefuellt = ausgefuellt;
    }
}
