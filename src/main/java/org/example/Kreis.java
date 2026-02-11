package org.example;

import java.awt.*;

public class Kreis {
    /*
    Initiliasierung mit standardmäßiger Werte
     */
    private Color fuellfarbe = null;
    protected boolean ausgefuellt = false;
    private Color farbe = Color.BLACK;
    private float dicke = 2.0f;

    private int Durchmesser,xKoordinate,yKoordinate;

    public Kreis(int Durchmesser,int xKoordinate,int yKoordinate) {
        this.xKoordinate = xKoordinate;
        this.yKoordinate = yKoordinate;
        this.Durchmesser = Durchmesser;
    }
    /*
     Getter und Setter für die jeweiligen Instanzvariablen
     */
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

