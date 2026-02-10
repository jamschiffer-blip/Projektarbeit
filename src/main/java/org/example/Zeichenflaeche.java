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
    public List<Point> Radiererpunkte;
    private BufferedImage Bild;
    private Zustandsspeicher zustandsspeicher = new Zustandsspeicher();
    private Kreis previewKreis;
    private Linie previewLinie;
    private Ellipse previewEllipse;
    private Rechteck previewRechteck;
    private Polygon previewPolygon;
    private Color Hintergrundfarbe = Color.WHITE; //Weiss als Standardfarbe
    private int Radierradius = 20;
    private Point Radierpunkt;
    private boolean fuellFarbebenutzen;
    private Color aktuelleFuellfarbe;

    public Zeichenflaeche() {
        Linien = new ArrayList<>();//hier werden die Linien die gezeichnet werden erstgespeichert
        Ellipsen = new ArrayList<>();
        Kreise = new ArrayList<>();
        Rechtecke = new ArrayList<>();
        Polygons = new ArrayList<>();
        Radiererpunkte = new ArrayList<Point>();
    }
    public void zeichneLinie(Linie linie){ //nur für gerade Linie
        zustandsspeicher.fuehreaus(
                () -> {
                    Linien.add(linie);
                    repaint();
                },
                ()-> {
                    Linien.remove(linie);
                    repaint();
                }
                );
    }
    public void zeichneLinie(List<Linie> Linien){ //fuer den Modus Frei dann wird ein kompletter strcih entfernt,
        zustandsspeicher.fuehreaus(
                () -> {
                    this.Linien.addAll(Linien);      //da es mehrere Listen mit dem namen Linien gibt
                    repaint();
                },
                () -> {
                    this.Linien.removeAll(Linien);
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
                ()-> {
                    Ellipsen.remove(ellipse);
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
                ()-> {
                    Kreise.remove(kreis);
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
        grafik.setColor(getHintergrundfarbe());  //hierbei wird die Zeichenfläche mit der jeweiligen Hintergrundfarbeübermalt
        grafik.fillRect(0,0,getWidth(),getHeight());

        if(Bild != null){
            grafik.drawImage(Bild,0,0,null); //Wenn es Bild gibt das geladen wurde wird es gezeichnet
        }
        for(Linie linie : Linien){
            grafik.setStroke(new BasicStroke(linie.getDicke())); //als Methode schreiben evtl
            grafik.setColor(linie.getFarbe());
            grafik.drawLine(linie.getStartX(),linie.getStartY(),linie.getEndX(), linie.getEndY());
        }

        for(Ellipse ellipse : Ellipsen) {
            if(ellipse.getFuellfarbe() != null){ //Zuerest füllen damit RAndlinien sichtbar werden durchübermalen
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillOval(ellipse.getxKoordinate(),ellipse.getyKoordinate(),ellipse.getBreite(),ellipse.getHoehe());
            }
            grafik.setStroke(new BasicStroke(ellipse.getDicke())); //jetzt umranden
            grafik.setColor(ellipse.getFarbe());
            grafik.drawOval(ellipse.getxKoordinate(),ellipse.getyKoordinate(),ellipse.getBreite(),ellipse.getHoehe());

        }
        for(Kreis kreis : Kreise) {
            if(kreis.getFuellfarbe()!=null){
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillOval(kreis.getxKoordinate(),kreis.getyKoordinate(),kreis.getDurchmesser(), kreis.getDurchmesser());
            }
            grafik.setStroke(new BasicStroke(kreis.getDicke()));
            grafik.setColor(kreis.getFarbe());
            grafik.drawOval(kreis.getxKoordinate(),kreis.getyKoordinate(),kreis.getDurchmesser(), kreis.getDurchmesser() ); //hoehe und breite sind bei kreis gleich

        }
        for(Rechteck rechteck : Rechtecke) {
            if(rechteck.getFuellfarbe()!=null){
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillRect(rechteck.getxKoordinate(),rechteck.getyKoordinate(),rechteck.getBreite(),rechteck.getHoehe());
            }
            grafik.setStroke(new BasicStroke(rechteck.getDicke()));
            grafik.setColor(rechteck.getFarbe());
            grafik.drawRect(rechteck.getxKoordinate(),rechteck.getyKoordinate(),rechteck.getBreite(),rechteck.getHoehe());

        }
        for(Polygon polygon : Polygons) {
            if(polygon.getFuellfarbe() != null){
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillPolygon(polygon.getxKoordinaten(),polygon.getyKoordinaten(), polygon.getAnzahlPunkte());
            }
            grafik.setStroke(new BasicStroke(polygon.getDicke()));
            grafik.setColor(polygon.getFarbe());
            grafik.drawPolygon(polygon.getxKoordinaten(),polygon.getyKoordinaten(), polygon.getAnzahlPunkte());

        }
        if(previewLinie!=null){
            grafik.setStroke(new BasicStroke(previewLinie.getDicke())); //als Methode schreiben evtl
            grafik.setColor(previewLinie.getFarbe());
            grafik.drawLine(previewLinie.getStartX(),previewLinie.getStartY(),previewLinie.getEndX(), previewLinie.getEndY());
        }
        if(previewKreis != null){
            if(previewKreis.getFuellfarbe()!=null){
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillOval(previewKreis.getxKoordinate(),previewKreis.getyKoordinate(),previewKreis.getDurchmesser(), previewKreis.getDurchmesser());
            }
            grafik.setStroke(new BasicStroke(previewKreis.getDicke()));
            grafik.setColor(previewKreis.getFarbe());
            grafik.drawOval(previewKreis.getxKoordinate(),previewKreis.getyKoordinate(),previewKreis.getDurchmesser(), previewKreis.getDurchmesser() );

        }
        if(previewPolygon != null){
            if(previewPolygon.getFuellfarbe()!=null){
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillPolygon(previewPolygon.getxKoordinaten(),previewPolygon.getyKoordinaten(), previewPolygon.getAnzahlPunkte());
            }
            grafik.setStroke(new BasicStroke(previewPolygon.getDicke()));
            grafik.setColor(previewPolygon.getFarbe());
            grafik.drawPolygon(previewPolygon.getxKoordinaten(),previewPolygon.getyKoordinaten(), previewPolygon.getAnzahlPunkte());

        }
        if(previewRechteck != null){
            if(previewRechteck.getFuellfarbe()!=null){
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillRect(previewRechteck.getxKoordinate(),previewRechteck.getyKoordinate(),previewRechteck.getBreite(),previewRechteck.getHoehe());
            }
            grafik.setStroke(new BasicStroke(previewRechteck.getDicke()));
            grafik.setColor(previewRechteck.getFarbe());
            grafik.drawRect(previewRechteck.getxKoordinate(),previewRechteck.getyKoordinate(),previewRechteck.getBreite(),previewRechteck.getHoehe());

        }
        if(previewEllipse != null){
            if(previewEllipse.getFuellfarbe()!=null){
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillOval(previewEllipse.getxKoordinate(),previewEllipse.getyKoordinate(),previewEllipse.getBreite(),previewEllipse.getHoehe());
            }
            grafik.setStroke(new BasicStroke(previewEllipse.getDicke()));
            grafik.setColor(previewEllipse.getFarbe());
            grafik.drawOval(previewEllipse.getxKoordinate(),previewEllipse.getyKoordinate(),previewEllipse.getBreite(),previewEllipse.getHoehe());


        }
        grafik.setColor(getHintergrundfarbe());
        int radius = getRadierradius();
        int durchmesser = 2*radius;
        for(Point radierpunkt : Radiererpunkte)
            grafik.fillOval(radierpunkt.x - radius,radierpunkt.y -radius,durchmesser,durchmesser);
    }
    public void reset(){
        Linien.clear();;
        Ellipsen.clear();
        Kreise.clear();
        Rechtecke.clear();;
        Polygons.clear();
        Radiererpunkte.clear();
        setHintergrundfarbe(getHintergrundfarbe());
        repaint();
    }

    public BufferedImage wandleBildinBufferdImage() {
        BufferedImage Bild = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB); //Um als JPG zu zeichen es gibt RGB Farben
        Graphics2D grafik = Bild.createGraphics(); //hiermit kann man im BufferedImage zeichnen

        //Jetzt wird das Bild nachgemalt und anschließend gespüeichert
        grafik.setColor(getHintergrundfarbe());
        grafik.fillRect(0,0,getWidth(),getHeight()); //Zeichenfläche wird dabei mit Hintergrundfarbe übermalt



        for(Linie linie : Linien){
            grafik.setStroke(new BasicStroke(linie.getDicke()));
            grafik.setColor(linie.getFarbe());
            grafik.drawLine(linie.getStartX(),linie.getStartY(),linie.getEndX(), linie.getEndY());
        }

        for(Ellipse ellipse : Ellipsen) {
            if(ellipse.getFuellfarbe()!=null){
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillOval(ellipse.getxKoordinate(),ellipse.getyKoordinate(),ellipse.getBreite(),ellipse.getHoehe());
            }
            grafik.setStroke(new BasicStroke(ellipse.getDicke()));
            grafik.setColor(ellipse.getFarbe());
            grafik.drawOval(ellipse.getxKoordinate(),ellipse.getyKoordinate(),ellipse.getBreite(),ellipse.getHoehe());

        }
        for(Kreis kreis : Kreise) {
            if(kreis.getFuellfarbe()!=null){
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillOval(kreis.getxKoordinate(),kreis.getyKoordinate(),kreis.getDurchmesser(), kreis.getDurchmesser());
            }
            grafik.setStroke(new BasicStroke(kreis.getDicke()));
            grafik.setColor(kreis.getFarbe());
            grafik.drawOval(kreis.getxKoordinate(),kreis.getyKoordinate(),kreis.getDurchmesser(), kreis.getDurchmesser() );//hoehe und breite sind bei kreis gleich

        }
        for(Rechteck rechteck : Rechtecke) {
            if(rechteck.getFuellfarbe()!=null){
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillRect(rechteck.getxKoordinate(),rechteck.getyKoordinate(),rechteck.getBreite(),rechteck.getHoehe());
            }
            grafik.setStroke(new BasicStroke(rechteck.getDicke()));
            grafik.setColor(rechteck.getFarbe());
            grafik.drawRect(rechteck.getxKoordinate(),rechteck.getyKoordinate(),rechteck.getBreite(),rechteck.getHoehe());

        }
        for(Polygon polygon : Polygons) {
            if(polygon.getFuellfarbe()!=null){
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillPolygon(polygon.getxKoordinaten(),polygon.getyKoordinaten(), polygon.getAnzahlPunkte());
            }
            grafik.setStroke(new BasicStroke(polygon.getDicke()));
            grafik.setColor(polygon.getFarbe());
            grafik.drawPolygon(polygon.getxKoordinaten(),polygon.getyKoordinaten(), polygon.getAnzahlPunkte());

        }
        grafik.setColor(getHintergrundfarbe());
        int radius = getRadierradius();
        int durchmesser = 2*radius;
        for(Point radierpunkt : Radiererpunkte)
            grafik.fillOval(radierpunkt.x - radius,radierpunkt.y -radius,durchmesser,durchmesser);
        grafik.dispose();


        return Bild;
    }
    public void radiere(int xKoordinate,int yKoordinate){
         Point Radierpunkt = new Point(xKoordinate,yKoordinate);
        zustandsspeicher.fuehreaus(
                () ->{
                    Radiererpunkte.add(Radierpunkt);
                    repaint();
                },
                () -> {
                    Radiererpunkte.remove(Radierpunkt);
                    repaint();
                }

        );
    }
    public void radiere(List<Point> Punkte){
        zustandsspeicher.fuehreaus(
                () ->{
                    this.Radiererpunkte.addAll(Punkte);
                    repaint();
                },
                () -> {
                    this.Radiererpunkte.removeAll(Punkte);
                    repaint();
                }

        );
    }

    public void setBild(BufferedImage bild) {
        Bild = bild;
        setPreferredSize(new Dimension(Bild.getWidth(),Bild.getHeight())); //Die zeichenflaeche wird damit so groß wie das gespeierte Bild,damit wird es 1:1 wenn es geladen wird
        revalidate(); //Neuberechnung für Layout
        repaint();
    }
    public BufferedImage getBild(){
        return Bild;
    }
    public void setPreviewKreis(Kreis kreis){
        previewKreis = kreis;
        repaint();
    }
    public void clearPreviewKreis(){
        previewKreis = null;
        repaint();
    }
    public void setPreviewLinie(Linie linie){
        previewLinie = linie;
        repaint();
    }
    public void clearPreviewLinie(){
        previewLinie = null;
        repaint();
    }
    public void setPreviewEllipse(Ellipse ellipse){
        previewEllipse = ellipse;
        repaint();
    }
    public void clearPreviewEllipse(){
        previewEllipse = null;
        repaint();
    }
    public void setPreviewRechteck(Rechteck rechteck){
        previewRechteck = rechteck;
        repaint();
    }
    public void clearPreviewRechteck(){
        previewRechteck = null;
        repaint();
    }
    public void setPreviewPolygon(Polygon polygon){
        previewPolygon = polygon;
        repaint();
    }
    public void clearPreviewPolygon(){
        previewPolygon = null;
        repaint();
    }

    public Color getHintergrundfarbe() {
        return Hintergrundfarbe;
    }

    public void setHintergrundfarbe(Color hintergrundfarbe) {
        Hintergrundfarbe = hintergrundfarbe;
    }

    public int getRadierradius() {
        return Radierradius;
    }

    public void setRadierradius(int radierradius) {
        Radierradius = radierradius;
    }

    public boolean isFuellFarbebenutzen() {
        return fuellFarbebenutzen;
    }

    public void setFuellFarbebenutzen(boolean fuellFarbebenutzen) {
        this.fuellFarbebenutzen = fuellFarbebenutzen;
    }

    public Color getAktuelleFuellfarbe() {
        return aktuelleFuellfarbe;
    }

    public void setAktuelleFuellfarbe(Color aktuelleFuellfarbe) {
        this.aktuelleFuellfarbe = aktuelleFuellfarbe;
    }
}
