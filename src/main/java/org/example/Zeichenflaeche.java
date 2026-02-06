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
    private Zustandsspeicher zustandsspeicher = new Zustandsspeicher();


    public Zeichenflaeche() {
        Linien = new ArrayList<>();//hier werden die Linien die gezeichnet werden erstgespeichert
        Ellipsen = new ArrayList<>();
        Kreise = new ArrayList<>();
        Rechtecke = new ArrayList<>();
        Polygons = new ArrayList<>();
    }
    public void zeichneLinie(Linie linie){ //nur für gerade Linie
        zustandsspeicher.fuehreaus(
                () -> {
                    Linien.add(linie);
                    repaint();
                },
                ()-> {Linien.remove(linie);
                    repaint();
                }
                );
    }
    public void zeichneLinie(List<Linie> Linien){ //fuer den Modus Frei dann wird ein kompletter strcih entfernt,
        zustandsspeicher.fuehreaus(
                () -> {this.Linien.addAll(Linien);      //da es mehrere Listen mit dem namen Linien gibt
                repaint();
                },
                () -> { this.Linien.removeAll(Linien);
                    repaint();

                }
        );
    }
    public void zeichneEllipse(Ellipse ellipse){
        zustandsspeicher.fuehreaus(
                () -> {
                    Ellipsen.add(ellipse);
                    repaint();
                },
                ()-> {Ellipsen.remove(ellipse);
                    repaint();
                }
        );
    }
    public void zeichneKreis(Kreis kreis){
        zustandsspeicher.fuehreaus(
                () -> {
                    Kreise.add(kreis);
                    repaint();
                },
                ()-> {Kreise.remove(kreis);
                    repaint();
                }
        );
    }
    public void zeichneRechteck(Rechteck rechteck){
        zustandsspeicher.fuehreaus(
                () -> {
                    Rechtecke.add(rechteck);
                    repaint();
                },
                ()-> {
                    Rechtecke.remove(rechteck);
                    repaint();
                }
        );
    }
    public void zeichnePolygon(Polygon polygon){
        zustandsspeicher.fuehreaus(
                () -> {
                    Polygons.add(polygon);
                    repaint();
                },
                ()-> {
                    Polygons.remove(polygon);
                    repaint();
                }
        );
    }
    public void undo(){
        zustandsspeicher.undo();
    }
    public void redo(){
        zustandsspeicher.redo();
    }

    @Override
    protected void paintComponent(Graphics g) { //überschreiben von paintcomponent anstatt graphics2d wäre sonst zzu gross
        super.paintComponent(g);
        Graphics2D grafik = (Graphics2D) g ;
        if(Bild != null){
            grafik.drawImage(Bild,0,0,null); //Wenn es Bild gibt das geladen wurde wird es gezeichnet
        }
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
        repaint();
    }

    public BufferedImage wandleBildinBufferdImage() {
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

    public void setBild(BufferedImage bild) {
        Bild = bild;
        repaint();
    }
    public BufferedImage getBild(){
        return Bild;
    }
}
