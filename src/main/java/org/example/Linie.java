package org.example;

import java.awt.*;

public class Linie {
    /*
    Initiliasierung mit standardmäßiger Werte
     */
    private Color farbe = Color.BLACK;
    private float dicke = 2.0f;

    private int StartX,EndX,StartY,EndY;

    public Linie(int StartX,int StartY,int EndX, int EndY) {
        this.StartX = StartX;
        this.StartY = StartY;
        this.EndX = EndX;
        this.EndY = EndY;
    }
    /*
     Getter und Setter für die jeweiligen Instanzvariablen
     */
    public int getStartX() {
        return StartX;
    }

    public int getEndX() {
        return EndX;
    }

    public int getStartY() {
        return StartY;
    }

    public int getEndY() {
        return EndY;
    }

    public Color getFarbe() {
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
}
