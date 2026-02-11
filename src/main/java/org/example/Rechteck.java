package org.example;

import java.awt.*;

public class Rechteck {
    /*
    Initiliasierung mit standardmäßiger Werte
     */
    private Color fuellfarbe = null;
    protected boolean ausgefuellt = false;
    private Color farbe = Color.BLACK;
    private float dicke = 2.0f;

    private int xKoordinate,yKoordinate,Breite,Hoehe;

    public Rechteck(int StartxKoordinate,int StartyKoordinate,int Breite,int Hoehe) {
        this.xKoordinate = StartxKoordinate;
        this.yKoordinate = StartyKoordinate;
        this.Breite = Breite;
        this.Hoehe = Hoehe;
    }
    /*
     Getter und Setter für die jeweiligen Instanzvariablen
     */
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
