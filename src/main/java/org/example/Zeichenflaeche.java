package org.example;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Zeichenflaeche extends JPanel {
    public List<Linie> Linien;
    public List<Ellipse> Ellipsen;

    public Zeichenflaeche() {
        Linien = new ArrayList<>();//hier werden die Linien die gezeichnet werden erstgespeichert
        Ellipsen = new ArrayList<>();
    }
    public void zeichneLinie(Linie linie){
        Linien.add(linie);
        repaint();
    }
    public void zeichneEllipse(Ellipse ellipse){
        Ellipsen.add(ellipse);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) { //überschreiben von paintcomponent anstatt graphics2d wäre sonst zzu gross
        super.paintComponent(g);
        Graphics2D grafik = (Graphics2D) g ;

        for(Linie linie : Linien)
            grafik.drawLine(linie.getStartX(),linie.getStartY(),linie.getEndX(), linie.getEndY());
        for(Ellipse ellipse : Ellipsen)
            grafik.drawOval(ellipse.getxKoordinate(),ellipse.getyKoordinate(),ellipse.getBreite(),ellipse.getHoehe());
    }
}
