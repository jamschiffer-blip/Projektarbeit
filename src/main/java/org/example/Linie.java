package org.example;

public class Linie {
    private int StartX,EndX,StartY,EndY;

    public Linie(int StartX,int StartY,int EndX, int EndY) {
        this.StartX = StartX;
        this.StartY = StartY;
        this.EndX = EndX;
        this.EndY = EndY;
    }

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
}
