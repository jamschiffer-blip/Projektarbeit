package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Zeichenflaeche extends JPanel {
    private List<Linie> Linien;
    private List<Ellipse> Ellipsen;
    private List<Kreis> Kreise;
    private List<Rechteck> Rechtecke;
    private List<Polygon> Polygons;
    public List<Point> Radiererpunkte;
    private List<Text> Texte;
    private BufferedImage Bild;
    private Zustandsspeicher zustandsspeicher = new Zustandsspeicher();
    private Kreis previewKreis;
    private Linie previewLinie;
    private Ellipse previewEllipse;
    private Rechteck previewRechteck;
    private Polygon previewPolygon;
    private Color Hintergrundfarbe = Color.WHITE; //Weiss als Standardfarbe
    private int Radierradius = 20;
    private boolean fuellFarbebenutzen;
    private Color aktuelleFuellfarbe;
    private List<Object> Reihenfolge;//Für die richtige Reihenfolge damit Radieren funktioniert
    private List<Linie> previewFreihand = null;
    private List<Point> previewRadierer = null;

    public Zeichenflaeche() {
        //hier werden die Shapes die gezeichnet werden erst gespeichert
        Linien = new ArrayList<>();
        Ellipsen = new ArrayList<>();
        Kreise = new ArrayList<>();
        Rechtecke = new ArrayList<>();
        Polygons = new ArrayList<>();
        Radiererpunkte = new ArrayList<>();
        Texte = new ArrayList<>();
        Reihenfolge = new ArrayList<>();
    }
    /*
    Hier folgen die zeichne Methoden, diese übergeben die jeweiligen Objekte an ihre Objektlisten und an Liste Reihenfolge
    Alle verwenden aus Zustandsspeicher die Methode fuehreaus um Un- und Redo zu ermöglichen
     */
    public void zeichneLinie(Linie linie){ //nur für gerade Linie
        zustandsspeicher.fuehreaus(
                () -> {
                    Linien.add(linie); //Hier wird Linie hinzugefügt dieser Teil, wird benötigt zum erstmaligen Zeichen eines Shapes oder für Redo
                    Reihenfolge.add(linie); //Wird der Reihenfolge hinzugefügt
                    repaint();
                },
                ()-> {
                    Linien.remove(linie); //Hier wird die Linie wieder entfernt, wird benötigt für Redo
                    Reihenfolge.remove(linie); //Wird bei Undo aus REihenfolge entfernt
                    repaint();
                }
                );
    }
    public void zeichneLinie(List<Linie> Linien){ //fuer den Modus Frei dann wird ein kompletter strich entfernt,
        List<Linie> Kopie = new ArrayList<>(Linien); //Kopie anlegen da die Liste Linien später geleert wird
        zustandsspeicher.fuehreaus(
                () -> {
                    this.Linien.addAll(Linien);
                    Reihenfolge.add(Kopie);
                    repaint();
                },
                () -> {
                    this.Linien.removeAll(Linien);
                    Reihenfolge.remove(Kopie);
                    repaint();

                }
        );
    }
    public void zeichneEllipse(Ellipse ellipse){
        zustandsspeicher.fuehreaus(
                () -> {
                    Ellipsen.add(ellipse);
                    Reihenfolge.add(ellipse);
                    repaint();
                },
                ()-> {
                    Ellipsen.remove(ellipse);
                    Reihenfolge.remove(ellipse);
                    repaint();
                }
        );
    }
    public void zeichneKreis(Kreis kreis){
        zustandsspeicher.fuehreaus(
                () -> {
                    Kreise.add(kreis);
                    Reihenfolge.add(kreis);
                    repaint();
                },
                ()-> {
                    Kreise.remove(kreis);
                    Reihenfolge.remove(kreis);
                    repaint();
                }
        );
    }
    public void zeichneRechteck(Rechteck rechteck){
        zustandsspeicher.fuehreaus(
                () -> {
                    Rechtecke.add(rechteck);
                    Reihenfolge.add(rechteck);
                    repaint();
                },
                ()-> {
                    Rechtecke.remove(rechteck);
                    Reihenfolge.remove(rechteck);
                    repaint();
                }
        );
    }
    public void zeichnePolygon(Polygon polygon){
        zustandsspeicher.fuehreaus(
                () -> {
                    Polygons.add(polygon);
                    Reihenfolge.add(polygon);
                    repaint();
                },
                ()-> {
                    Polygons.remove(polygon);
                    Reihenfolge.remove(polygon);
                    repaint();
                }
        );
    }

    /*
    Erstellung statischer inneren Klaase Radierstrich um gleichzeitig mehrerere
    Radierstriche in unterschiedlichen Größen speichern zu können
     */
    static class Radierstrich{
        List<Point> punkte;
        int radius;
        Radierstrich(List<Point> punkte, int radius){
            this.punkte = punkte;
            this.radius = radius;
        }
    }
    /*
    Neue Radiere Methode, es wird wie bei zeichneLinie eine Liste erstellt der jeweiligen Radierpunkte, damit
    ein gesamter radierstrich daraus entsteht und dieser auch mit Redo und Undo rückgängig gemacht werden kann
     */
    public void radiere(List<Point> Punkte){
        List<Point> Kopie = new ArrayList<>(Punkte); //Auch hier wie beim freien Zeichnen Kopie anlegen, da die Liste Punkte geleert wird, nach einem Radiervorgang
        int aktuellerRadius = getRadierradius();
        Radierstrich radierstrich = new Radierstrich(Kopie,aktuellerRadius); //neuer Radierstrich wird mit aktuellen Werten erstellt

        zustandsspeicher.fuehreaus(
                () ->{
                    Reihenfolge.add(radierstrich);
                    repaint();
                },
                () -> {
                    Reihenfolge.remove(radierstrich);
                    repaint();
                }

        );
    }
    public void zeichneText(Text text){
        zustandsspeicher.fuehreaus(
                () -> {
                    Texte.add(text);
                    Reihenfolge.add(text);
                    repaint();
                },
                () -> {
                    Texte.remove(text);
                    Reihenfolge.remove(text);
                    repaint();
                }

        );
    }
    /*
    Diese Methoden werden in GUI aufgerufen, für Un- und Redo
     */
    public void undo(){
        zustandsspeicher.undo();
    }
    public void redo(){
        zustandsspeicher.redo();
    }
    /*
    Hier wird das eigentliche Bild mit gemalt
    paintComponent wird bei Repaint immer aufgerufen
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D grafik = (Graphics2D) g ;
        grafik.setColor(getHintergrundfarbe());
        grafik.fillRect(0,0,getWidth(),getHeight());

        //Wenn es Bild gibt das geladen wurde wird es gezeichnet
        if(Bild != null){
            grafik.drawImage(Bild,0,0,null);
        }
        /*
        Hier kommen die finalen Zeichnungen in Richtiger Reihenfolge
         */
        for(Object o : Reihenfolge) {

            //Gerade Linie
            if(o instanceof Linie linie)
             {
                grafik.setStroke(new BasicStroke(linie.getDicke()));
                grafik.setColor(linie.getFarbe());
                grafik.drawLine(linie.getStartX(), linie.getStartY(), linie.getEndX(), linie.getEndY());
                }
            //Freihand Linie
            /*
            Wenn also das aktuelle Objekt in der Reihenfolge eine Liste ist,diese nicht leer ist und diese vom Typ Linie ist,
            wird jeder einzelne kleine Linie gezeichnet, diese Typabfrage ist wichtig da es zwei Arten von Linie gibt
             */
            else if(o instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof Linie){
                List<Linie> liste = (List<Linie>) o ; //Das aktuelle Objekt also die jeweilige Liste wird übergeben
                for(Linie linie : liste) {
                    grafik.setStroke(new BasicStroke(linie.getDicke()));
                    grafik.setColor(linie.getFarbe());
                    grafik.drawLine(linie.getStartX(), linie.getStartY(), linie.getEndX(), linie.getEndY());
                }
            }
            //Ellipse
            else if (o instanceof Ellipse ellipse) {
                if (ellipse.getFuellfarbe() != null) { //Zuerst füllen damit Randlinien sichtbar werden durchübermalen
                    grafik.setColor(ellipse.getFuellfarbe());
                    grafik.fillOval(ellipse.getxKoordinate(), ellipse.getyKoordinate(), ellipse.getBreite(), ellipse.getHoehe());
                }
                grafik.setStroke(new BasicStroke(ellipse.getDicke())); //jetzt umranden
                grafik.setColor(ellipse.getFarbe());
                grafik.drawOval(ellipse.getxKoordinate(), ellipse.getyKoordinate(), ellipse.getBreite(), ellipse.getHoehe());
            }
            //Kreis
            else if (o instanceof Kreis kreis) {
                if (kreis.getFuellfarbe() != null) {
                    grafik.setColor(kreis.getFuellfarbe());
                    grafik.fillOval(kreis.getxKoordinate(), kreis.getyKoordinate(), kreis.getDurchmesser(), kreis.getDurchmesser());
                }
                grafik.setStroke(new BasicStroke(kreis.getDicke()));
                grafik.setColor(kreis.getFarbe());
                grafik.drawOval(kreis.getxKoordinate(), kreis.getyKoordinate(), kreis.getDurchmesser(), kreis.getDurchmesser()); //hoehe und breite sind bei kreis gleich
            }
            //Rechteck
            else if (o instanceof Rechteck rechteck) {
                if (rechteck.getFuellfarbe() != null) {
                    grafik.setColor(rechteck.getFuellfarbe());
                    grafik.fillRect(rechteck.getxKoordinate(), rechteck.getyKoordinate(), rechteck.getBreite(), rechteck.getHoehe());
                }
                grafik.setStroke(new BasicStroke(rechteck.getDicke()));
                grafik.setColor(rechteck.getFarbe());
                grafik.drawRect(rechteck.getxKoordinate(), rechteck.getyKoordinate(), rechteck.getBreite(), rechteck.getHoehe());
            }
            //Polygon
            else if (o instanceof Polygon polygon) {
                if (polygon.getFuellfarbe() != null) {
                    grafik.setColor(polygon.getFuellfarbe());
                    grafik.fillPolygon(polygon.getxKoordinaten(), polygon.getyKoordinaten(), polygon.getAnzahlPunkte());
                }
                grafik.setStroke(new BasicStroke(polygon.getDicke()));
                grafik.setColor(polygon.getFarbe());
                grafik.drawPolygon(polygon.getxKoordinaten(), polygon.getyKoordinaten(), polygon.getAnzahlPunkte());
            }
            //Text
            else if (o instanceof Text text) {
                grafik.setColor(text.getTextfarbe());
                grafik.setFont(text.getSchriftart());
                grafik.drawString(text.getText(), text.getxKoordinate(), text.getyKoordinate());
            }
            //Auch ein Radierstrich zählt zu den Shapes
            else if(o instanceof Radierstrich radierstrich) {
                grafik.setColor(getHintergrundfarbe());
                int radius = radierstrich.radius;
                int durchmesser = 2 * radius;
                for (Point radierpunkt : radierstrich.punkte)
                    grafik.fillOval(radierpunkt.x - radius, radierpunkt.y - radius, durchmesser, durchmesser);
            }
        }
        /*
        Hier werden die Previewshapes gezeichnet
         */
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
        if(previewEllipse != null) {
            if (previewEllipse.getFuellfarbe() != null) {
                grafik.setColor(getAktuelleFuellfarbe());
                grafik.fillOval(previewEllipse.getxKoordinate(), previewEllipse.getyKoordinate(), previewEllipse.getBreite(), previewEllipse.getHoehe());
            }
            grafik.setStroke(new BasicStroke(previewEllipse.getDicke()));
            grafik.setColor(previewEllipse.getFarbe());
            grafik.drawOval(previewEllipse.getxKoordinate(), previewEllipse.getyKoordinate(), previewEllipse.getBreite(), previewEllipse.getHoehe());
        }
            if(previewFreihand != null && !previewFreihand.isEmpty()){
            for (Linie linie : previewFreihand){
                grafik.setStroke(new BasicStroke(linie.getDicke()));
                grafik.setColor(linie.getFarbe());
                grafik.drawLine(linie.getStartX(), linie.getStartY(), linie.getEndX(), linie.getEndY());
            }
        }
        if(previewRadierer != null && !previewRadierer.isEmpty()){
            grafik.setColor(getHintergrundfarbe());
            int radius = getRadierradius();
            int durchmesser = 2 * radius;
            for (Point radierpunkt : previewRadierer)
                grafik.fillOval(radierpunkt.x - radius, radierpunkt.y - radius, durchmesser, durchmesser);
        }

    }
    /*
    Diese methode resetet das Bild, sie wird unter Anderem beim erstellen einer neuen Datei genutzt
    Sie leert alle Listen der jeweiligen Shapes und setzt die Hintergrundfarbe, welche aktuell gefragt ist
    Die Hintergrundfarbe ist standardmäßig weiß
     */
    public void reset(){
        Linien.clear();
        Ellipsen.clear();
        Kreise.clear();
        Rechtecke.clear();
        Polygons.clear();
        Radiererpunkte.clear();
        Texte.clear();
        Reihenfolge.clear();
        zustandsspeicher.clear(); //Undo und Redo nach reset nicht mehr möglich
        Bild = null;
        revalidate();
        repaint();
    }
    /*
    Methode, die die gemalte Zeichenfläche als BufferedImage speichert, dieses kann später als JPG Datei gespeichert werden
    Hierbei wird das Bild eins zu eins nachgemalt und anschließend gespeichert
     */
    public BufferedImage wandleBildinBufferdImage() {
        BufferedImage Bild = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB); //Um als JPG zu zeichen es gibt RGB Farben
        Graphics2D grafik = Bild.createGraphics(); //Hiermit kann man im BufferedImage zeichnen

        //Jetzt wird das Bild nachgemalt und anschließend gespüeichert
        grafik.setColor(getHintergrundfarbe());
        grafik.fillRect(0,0,getWidth(),getHeight()); //Zeichenfläche wird dabei mit Hintergrundfarbe übermalt
        for(Object o : Reihenfolge) {

            //Gerade Linie
            if(o instanceof Linie linie)
            {
                grafik.setStroke(new BasicStroke(linie.getDicke()));
                grafik.setColor(linie.getFarbe());
                grafik.drawLine(linie.getStartX(), linie.getStartY(), linie.getEndX(), linie.getEndY());
            }
            //Freihand Linie
            else if(o instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof Linie){
                List<Linie> liste = (List<Linie>) o ; //Das aktuelle Objekt also die jeweilige Liste wird übergeben
                for(Linie linie : liste) {
                    grafik.setStroke(new BasicStroke(linie.getDicke()));
                    grafik.setColor(linie.getFarbe());
                    grafik.drawLine(linie.getStartX(), linie.getStartY(), linie.getEndX(), linie.getEndY());
                }
            }

            //Ellipse
            else if (o instanceof Ellipse ellipse) {
                if (ellipse.getFuellfarbe() != null) { //Zuerst füllen damit Randlinien sichtbar werden durchübermalen
                    grafik.setColor(ellipse.getFuellfarbe());
                    grafik.fillOval(ellipse.getxKoordinate(), ellipse.getyKoordinate(), ellipse.getBreite(), ellipse.getHoehe());
                }
                grafik.setStroke(new BasicStroke(ellipse.getDicke())); //jetzt umranden
                grafik.setColor(ellipse.getFarbe());
                grafik.drawOval(ellipse.getxKoordinate(), ellipse.getyKoordinate(), ellipse.getBreite(), ellipse.getHoehe());

            }
            //Kreis
            else if (o instanceof Kreis kreis) {
                if (kreis.getFuellfarbe() != null) {
                    grafik.setColor(kreis.getFuellfarbe());
                    grafik.fillOval(kreis.getxKoordinate(), kreis.getyKoordinate(), kreis.getDurchmesser(), kreis.getDurchmesser());
                }
                grafik.setStroke(new BasicStroke(kreis.getDicke()));
                grafik.setColor(kreis.getFarbe());
                grafik.drawOval(kreis.getxKoordinate(), kreis.getyKoordinate(), kreis.getDurchmesser(), kreis.getDurchmesser()); //hoehe und breite sind bei kreis gleich

            }
            //Rechteck
            else if (o instanceof Rechteck rechteck) {
                if (rechteck.getFuellfarbe() != null) {
                    grafik.setColor(rechteck.getFuellfarbe());
                    grafik.fillRect(rechteck.getxKoordinate(), rechteck.getyKoordinate(), rechteck.getBreite(), rechteck.getHoehe());
                }
                grafik.setStroke(new BasicStroke(rechteck.getDicke()));
                grafik.setColor(rechteck.getFarbe());
                grafik.drawRect(rechteck.getxKoordinate(), rechteck.getyKoordinate(), rechteck.getBreite(), rechteck.getHoehe());

            }
            //Polygon
            else if (o instanceof Polygon polygon) {
                if (polygon.getFuellfarbe() != null) {
                    grafik.setColor(polygon.getFuellfarbe());
                    grafik.fillPolygon(polygon.getxKoordinaten(), polygon.getyKoordinaten(), polygon.getAnzahlPunkte());
                }
                grafik.setStroke(new BasicStroke(polygon.getDicke()));
                grafik.setColor(polygon.getFarbe());
                grafik.drawPolygon(polygon.getxKoordinaten(), polygon.getyKoordinaten(), polygon.getAnzahlPunkte());
            }
            //Text
            else if (o instanceof Text text) {
                grafik.setColor(text.getTextfarbe());
                grafik.setFont(text.getSchriftart());
                grafik.drawString(text.getText(), text.getxKoordinate(), text.getyKoordinate());
            }
            //Radierstrich
            else if(o instanceof Radierstrich radierstrich) {
                grafik.setColor(getHintergrundfarbe());
                int radius = radierstrich.radius;
                int durchmesser = 2 * radius;
                for (Point radierpunkt : radierstrich.punkte)
                    grafik.fillOval(radierpunkt.x - radius, radierpunkt.y - radius, durchmesser, durchmesser);
            }
        }
        grafik.dispose();


        return Bild;
    }
    /*
    Clear und set Methoden für die previews
    Clear um Preview zu entfernen
    Set um Preview zu setzen
     */
    public void setPreviewKreis(Kreis kreis){
        previewKreis = kreis;
        repaint();
    }
    public void clearPreviewKreis(){
        previewKreis = null;
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
    public void setBild(BufferedImage bild) {
        Bild = bild;
        setPreferredSize(new Dimension(Bild.getWidth(),Bild.getHeight())); //Die Zeichenfläche wird damit so groß wie das gespeierte Bild,damit wird es 1:1 wenn es geladen wird
        revalidate(); //Neuberechnung für Layout
        repaint();
    }

    public void setPreviewFreihand(List<Linie> linien) {
        this.previewFreihand = linien;
        repaint();
    }

    public void clearPreviewFreihand () {
        this.previewFreihand = null;
        repaint();
    }

    public void setPreviewRadierer(List<Point> punkte) {
        this.previewRadierer = punkte;
        repaint();
    }

    public void clearPreviewRadierer() {
        this.previewRadierer = null;
        repaint();
    }
}
