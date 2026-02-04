package org.example;

public class Rechteck {
    private int xKoordinate,yKoordinate,Breite,Hoehe;

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
}
