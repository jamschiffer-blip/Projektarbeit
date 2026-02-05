package org.example;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Zeichenflaeche extends JPanel {
    public List<Linie> Linien;
    public List<Ellipse> Ellipsen;
    public List<Kreis> Kreise;
    public List<Rechteck> Rechtecke;
    public List<Polygon> Polygons;
    private BufferedImage Bild;


    public Zeichenflaeche() {
        Linien = new ArrayList<>();//hier werden die Linien die gezeichnet werden erstgespeichert
        Ellipsen = new ArrayList<>();
        Kreise = new ArrayList<>();
        Rechtecke = new ArrayList<>();
        Polygons = new ArrayList<>();
    }
    public void zeichneLinie(Linie linie){
        Linien.add(linie);
        repaint();
    }
    public void zeichneEllipse(Ellipse ellipse){
        Ellipsen.add(ellipse);
        repaint();
    }
    public void zeichneKreis(Kreis kreis){
        Kreise.add(kreis);
        repaint();
    }
    public void zeichneRechteck(Rechteck rechteck){
        Rechtecke.add(rechteck);
        repaint();
    }
    public void zeichnePolygon(Polygon polygon){
        Polygons.add(polygon);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) { //überschreiben von paintcomponent anstatt graphics2d wäre sonst zzu gross
        super.paintComponent(g);
        Graphics2D grafik = (Graphics2D) g ;

        for(Linie linie : Linien){
            grafik.setStroke(new BasicStroke(linie.getDicke())); //als Methode schreiben evtl
            grafik.setColor(linie.getFarbe());
            grafik.drawLine(linie.getStartX(),linie.getStartY(),linie.getEndX(), linie.getEndY());
        }

        for(Ellipse ellipse : Ellipsen) {
            grafik.setStroke(new BasicStroke(ellipse.getDicke()));
            grafik.setColor(ellipse.getFarbe());
            grafik.drawOval(ellipse.getxKoordinate(),ellipse.getyKoordinate(),ellipse.getBreite(),ellipse.getHoehe());
        }
        for(Kreis kreis : Kreise) {
            grafik.setStroke(new BasicStroke(kreis.getDicke()));
            grafik.setColor(kreis.getFarbe());
            grafik.drawOval(kreis.getxKoordinate(),kreis.getyKoordinate(),kreis.getDurchmesser(), kreis.getDurchmesser() ); //hoehe und breite sind bei kreis gleich
        }
        for(Rechteck rechteck : Rechtecke) {
            grafik.setStroke(new BasicStroke(rechteck.getDicke()));
            grafik.setColor(rechteck.getFarbe());
            grafik.drawRect(rechteck.getxKoordinate(),rechteck.getyKoordinate(),rechteck.getBreite(),rechteck.getHoehe());
        }
        for(Polygon polygon : Polygons) {
            grafik.setStroke(new BasicStroke(polygon.getDicke()));
            grafik.setColor(polygon.getFarbe());
            grafik.drawPolygon(polygon.getxKoordinaten(),polygon.getyKoordinaten(), polygon.getAnzahlPunkte());
        }
    }
    public void reset(){
        Linien.clear();;
        Ellipsen.clear();
        Kreise.clear();
        Rechtecke.clear();;
        Polygons.clear();
        setBackground(Color.WHITE);
        repaint();
    }

    public BufferedImage getBild() {
        BufferedImage Bild = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB); //Um als JPG zu zeichen es gibt RGB Farben
        Graphics2D grafik = Bild.createGraphics(); //hiermit kann man im BufferedImage zeichnen

        //Jetzt wird das Bild nachgemalt und anschließend gespüeichert
        grafik.setColor(Color.WHITE);
        grafik.fillRect(0,0,getWidth(),getHeight()); //Hintergrundfrabe wird zu weiß

        for(Linie linie : Linien){
            grafik.setStroke(new BasicStroke(linie.getDicke()));
            grafik.setColor(linie.getFarbe());
            grafik.drawLine(linie.getStartX(),linie.getStartY(),linie.getEndX(), linie.getEndY());
        }

        for(Ellipse ellipse : Ellipsen) {
            grafik.setStroke(new BasicStroke(ellipse.getDicke()));
            grafik.setColor(ellipse.getFarbe());
            grafik.drawOval(ellipse.getxKoordinate(),ellipse.getyKoordinate(),ellipse.getBreite(),ellipse.getHoehe());
        }
        for(Kreis kreis : Kreise) {
            grafik.setStroke(new BasicStroke(kreis.getDicke()));
            grafik.setColor(kreis.getFarbe());
            grafik.drawOval(kreis.getxKoordinate(),kreis.getyKoordinate(),kreis.getDurchmesser(), kreis.getDurchmesser() ); //hoehe und breite sind bei kreis gleich
        }
        for(Rechteck rechteck : Rechtecke) {
            grafik.setStroke(new BasicStroke(rechteck.getDicke()));
            grafik.setColor(rechteck.getFarbe());
            grafik.drawRect(rechteck.getxKoordinate(),rechteck.getyKoordinate(),rechteck.getBreite(),rechteck.getHoehe());
        }
        for(Polygon polygon : Polygons) {
            grafik.setStroke(new BasicStroke(polygon.getDicke()));
            grafik.setColor(polygon.getFarbe());
            grafik.drawPolygon(polygon.getxKoordinaten(),polygon.getyKoordinaten(), polygon.getAnzahlPunkte());
        }
        grafik.dispose();


        return Bild;
    }
}
