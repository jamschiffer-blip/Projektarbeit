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
    /*
    Getter und Setter für die Instanzvariablen
     */

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getxKoordinate() {
        return xKoordinate;
    }

    public void setxKoordinate(int xKoordinate) {
        this.xKoordinate = xKoordinate;
    }

    public int getyKoordinate() {
        return yKoordinate;
    }

    public void setyKoordinate(int yKoordinate) {
        this.yKoordinate = yKoordinate;
    }

    public Color getTextfarbe() {
        return textfarbe;
    }

    public void setTextfarbe(Color textfarbe) {
        this.textfarbe = textfarbe;
    }

    public Font getSchriftart() {
        return schriftart;
    }

    public void setSchriftart(Font schriftart) {
        this.schriftart = schriftart;
    }
}
