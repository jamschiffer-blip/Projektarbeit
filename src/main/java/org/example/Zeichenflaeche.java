package org.example;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Zeichenflaeche extends JPanel {
    private List<Linie> Linien;

    public Zeichenflaeche() {
        Linien = new ArrayList<>(); //hier werden die Linien die gezeichnet werden erstgespeichert
    }
    public void zeichneLinie(Linie linie){
        Linien.add(linie);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) { //überschreiben von paintcomponent anstatt graphics2d wäre sonst zzu gross
        super.paintComponent(g);
        Graphics2D grafik = (Graphics2D) g ;

        for(Linie linie : Linien)
            grafik.drawLine(linie.getStartX(),linie.getStartY(),linie.getEndX(), linie.getEndY());
    }
}
