package org.example;

import java.awt.*;

public class Text {

    private String text;
    private int xKoordinate,yKoordinate;
    private Color textfarbe = Color.WHITE;
    private Font schriftart;

    public Text(String text,int xKoordinate,int yKoordinate,Color textfarbe,Font schriftart) {
        this.text = text;
        this.schriftart = schriftart;
        this.xKoordinate = xKoordinate;
        this.yKoordinate = yKoordinate;
        this.textfarbe = textfarbe;
    }
    public String getText() {
        return text;
    }

    public int getxKoordinate() {
        return xKoordinate;
    }

    public int getyKoordinate() {
        return yKoordinate;
    }

    public Color getTextfarbe() {
        return textfarbe;
    }

    public Font getSchriftart() {
        return schriftart;
    }
}
