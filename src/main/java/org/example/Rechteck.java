package org.example;

import java.awt.*;

public class Rechteck {
    private int xKoordinate,yKoordinate,Breite,Hoehe;
    protected boolean ausgefuellt = false;
    private Color farbe = Color.BLACK;
    public Rechteck(int StartxKoordinate,int StartyKoordinate,int Breite,int Hoehe) {
        this.xKoordinate = StartxKoordinate;
        this.yKoordinate = StartyKoordinate;
        this.Breite = Breite;
        this.Hoehe = Hoehe;
    }

    public int getxKoordinate() {
        return xKoordinate;
    }

    public int getyKoordinate() {
        return yKoordinate;
    }

    public int getBreite() {
        return Breite;
    }

    public int getHoehe() {
        return Hoehe;
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
