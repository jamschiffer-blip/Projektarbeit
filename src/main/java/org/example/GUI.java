package org.example;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame {

    private JButton Undo, Redo, Speichern;
    private JPanel leiste;
    private JToolBar symbolleiste;
    private JMenuBar menueleiste;
    private JMenu datei;
    private JMenuItem Speichernmenueleiste, Laden, NeueDatei;
    private boolean mausgedrueckt = false;
    private String Modus = "Polygon";
    public Zeichenflaeche zeichenflaeche;
    private Color aktuelleFarbe = Color.BLACK;
    private float aktuelleDicke = 2.0f;
    private final int Zeichenflaeche_HOEHE = 2000;
    private final int Zeichenflaeche_BREITE = 1500;
    private File aktuelleDatei;
    private Color hellblau = new Color(200,235,245);
    private Color textfarbe = new Color(40,40,40);
    private ArrayList<Linie> aktuelleLinie = new ArrayList<>();

    public GUI() {
        super("Graphic Editor");
        setSize(2000, 2500);
        zeichenflaeche = new Zeichenflaeche();
        zeichenflaeche.setPreferredSize(new Dimension(Zeichenflaeche_BREITE, Zeichenflaeche_HOEHE));

        MeinListener listener = new MeinListener();
        zeichenflaeche.addMouseListener(listener);
        zeichenflaeche.addMouseMotionListener(listener);


        setLayout(new BorderLayout());

        symbolleiste = new JToolBar();
        symbolleiste.setFloatable(false); //Kann man mit der Maus nicht mehr wegziehen

        Undo = new JButton();
        Undo.setIcon(new ImageIcon("icons/undo.png"));
        Redo = new JButton();
        Redo.setIcon(new ImageIcon("icons/redo.png"));
        Speichern = new JButton();
        Speichern.setIcon(new ImageIcon("icons/save.png"));

        Undo.setActionCommand("undo");
        Redo.setActionCommand("redo");  //Icons Plagiat!!!
        Speichern.setActionCommand("speichern");
        Undo.addActionListener(e -> zeichenflaeche.undo());
        Redo.addActionListener(e -> zeichenflaeche.redo());
        Speichern.addActionListener(e -> { zwischenspeichern();});

        symbolleiste.add(Undo);
        symbolleiste.add(Redo);
        symbolleiste.add(Speichern);
        symbolleiste.setLayout(new FlowLayout(FlowLayout.LEFT));
        symbolleiste.setOpaque(true);
        symbolleiste.setBackground(hellblau);

        leiste = new JPanel();
        leiste.setLayout(new BoxLayout(leiste, BoxLayout.Y_AXIS));
        leiste.add(symbolleiste, BorderLayout.WEST); //hiermit bleibt die leiste immer links in der ecke,auch wenn das fenster vergroessert oder verkleinert wird
        leiste.setOpaque(true);
        leiste.setBackground(hellblau);

        menueleiste = new JMenuBar();
        datei = new JMenu("Datei");
        Speichernmenueleiste = new JMenuItem("Speichern Unter");
        Speichernmenueleiste.addActionListener(e -> SpeichernUnter());
        Laden = new JMenuItem("Laden");
        Laden.addActionListener(e -> ladeBild());
        NeueDatei = new JMenuItem("Neue Datei");
        Laden.setActionCommand("laden");
        Speichernmenueleiste.setActionCommand("Speichern Unter");
        NeueDatei.setActionCommand("neue datei");//hier abfrage ob korrekt
        NeueDatei.addActionListener(e -> zeichenflaeche.reset());
        menueleiste.setOpaque(true);
        menueleiste.setBackground(hellblau);

        datei.add(Speichernmenueleiste);
        datei.add(Laden);
        datei.add(NeueDatei);

        menueleiste.setLayout(new FlowLayout(FlowLayout.LEFT));
        menueleiste.add(datei);

        leiste.add(menueleiste);

        datei.setForeground(textfarbe);
        Speichernmenueleiste.setForeground(textfarbe);
        Laden.setForeground(textfarbe);
        NeueDatei.setForeground(textfarbe);


        add(leiste, BorderLayout.NORTH);
        add(zeichenflaeche, BorderLayout.CENTER);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    class MeinListener implements ActionListener, MouseListener, MouseMotionListener {


        int counter = 0;
        int StartxKoordinate;
        int StartyKoordinate;
        int EndXKoordinate;
        int EndYKoordinate;
        int counterPolygon = 0;
        int[] xKoordinatenPolygon = new int[100]; //Das Polygon darf maximal 100 Punkte haben
        int[] yKoordinatenPolygon = new int[100];

        @Override
        public void actionPerformed(ActionEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            double distanz;

            if (Modus.equals("Frei")) {
                StartxKoordinate = e.getX();
                StartyKoordinate = e.getY();
            }
            if (Modus.equals("Polygon")) {

                if (counterPolygon < 100) {
                    xKoordinatenPolygon[counterPolygon] = e.getX();
                    yKoordinatenPolygon[counterPolygon] = e.getY();
                    counterPolygon++;
                }
                //Hier Preview
                if(counterPolygon>=2){
                    int[] xKoordinatenPreview = Arrays.copyOf(xKoordinatenPolygon,counterPolygon);
                    int[] yKoordinatenPreview = Arrays.copyOf(yKoordinatenPolygon,counterPolygon);
                    Polygon previewPolygon = new Polygon(xKoordinatenPreview,yKoordinatenPreview);
                    previewPolygon.setDicke(aktuelleDicke);
                    previewPolygon.setFarbe(aktuelleFarbe);
                    zeichenflaeche.setPreviewPolygon(previewPolygon);
                }
                if (counterPolygon >= 3 && e.getClickCount() == 2) //Clickcount==2 da man dann mit einem Doppelklick das Polygon abschliessen kann
                {
                    zeichenflaeche.clearPreviewPolygon();
                    int[] xKoordinaten = Arrays.copyOf(xKoordinatenPolygon, counterPolygon);
                    int[] yKoordinaten = Arrays.copyOf(yKoordinatenPolygon, counterPolygon);
                    Polygon polygon = new Polygon(xKoordinaten, yKoordinaten);
                    polygon.setFarbe(aktuelleFarbe);
                    polygon.setDicke(aktuelleDicke);
                    zeichenflaeche.zeichnePolygon(polygon);//nur das benutzte Array wird verwendetet
                    counterPolygon = 0; //für nächstes Polygon zurücksetzen
                }

            }
            if (Modus.equals("Fuellen") && e.getClickCount() == 2) {
                //hier kommt der Füllmodus hin
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (Modus.equals("Frei")) {
                aktuelleLinie.clear(); //Liste der ganz vielen kleinen Striche die ein grossen Strich bilden wird leer gemacht
                StartxKoordinate = e.getX();
                StartyKoordinate = e.getY();
            }

            if (Modus.equals("Gerade")) {
                switch (counter) {
                    case 0:
                        counter++;
                        StartyKoordinate = e.getY();
                        StartxKoordinate = e.getX();
                        break;


                    case 1:
                        counter = 0;
                        EndXKoordinate = e.getX();
                        EndYKoordinate = e.getY();
                        Linie linie = new Linie(StartxKoordinate, StartyKoordinate, EndXKoordinate, EndYKoordinate);
                        linie.setFarbe(aktuelleFarbe);
                        linie.setDicke(aktuelleDicke);
                        zeichenflaeche.zeichneLinie(linie);
                        break;


                }
            }
            if (Modus.equals("Ellipse") || Modus.equals("Kreis") || Modus.equals("Rechteck")) {
                StartxKoordinate = e.getX();
                StartyKoordinate = e.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(Modus.equals("Frei")){
                if(!aktuelleLinie.isEmpty()){
                    zeichenflaeche.Linien.removeAll(aktuelleLinie); //damit werden alle Linien die als Preview dienen gelöscht um Un und redo möglichzumache
                    zeichenflaeche.zeichneLinie(new ArrayList<>(aktuelleLinie)); // es wird die methode zeichnelinie für eine edngültige linie aufgerufen
                    aktuelleLinie.clear();
                }
            }
            if (Modus.equals("Ellipse")) {
                zeichenflaeche.clearPreviewEllipse();
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();

                int xKoordinate = Math.min(StartxKoordinate, EndXKoordinate); //Kleinere Koordinaten nehmen da man in der "linken Oberen" Ecke hier die Koordinaten für die Ellipsen sind
                int yKoordinate = Math.min(StartyKoordinate, EndYKoordinate);
                int breite = Math.abs(EndXKoordinate - StartxKoordinate); //Abstand wird hier berechnet dieser ist durch abs immer positiv
                int hoehe = Math.abs(EndYKoordinate - StartyKoordinate); // hier dasselbe nur für die hoehe statt breite
                Ellipse ellipse = new Ellipse(xKoordinate, yKoordinate, breite, hoehe);
                ellipse.setFarbe(aktuelleFarbe);
                ellipse.setDicke(aktuelleDicke);
                zeichenflaeche.zeichneEllipse(ellipse);
            }
            if (Modus.equals("Kreis")) {
                zeichenflaeche.clearPreviewKreis();
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();
                double radiusinDouble = Math.sqrt((StartxKoordinate - EndXKoordinate) * (StartxKoordinate - EndXKoordinate)
                        + (StartyKoordinate - EndYKoordinate) * (StartyKoordinate - EndYKoordinate)); // Berechnung radius mithilfe Pythagoras
                int radius = (int) radiusinDouble;
                int xKoordinate = StartxKoordinate - radius; // Berechnung der Koordinaten des Mittelpunktes
                int yKoordinate = StartyKoordinate - radius;
                Kreis kreis = new Kreis(radius * 2, xKoordinate, yKoordinate);
                kreis.setFarbe(aktuelleFarbe);
                kreis.setDicke(aktuelleDicke);
                zeichenflaeche.zeichneKreis(kreis);
            }
            if (Modus.equals("Rechteck")) {
                zeichenflaeche.clearPreviewRechteck();
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();

                int xKoordinate = Math.min(StartxKoordinate, EndXKoordinate); //Kleinere Koordinaten nehmen da man in der "linken Oberen" Ecke hier die Koordinaten für die Ellipsen sind
                int yKoordinate = Math.min(StartyKoordinate, EndYKoordinate);
                int breite = Math.abs(EndXKoordinate - StartxKoordinate); //Abstand wird hier berechnet dieser ist durch abs immer positiv
                int hoehe = Math.abs(EndYKoordinate - StartyKoordinate); // hier dasselbe nur für die hoehe statt breite
                Rechteck rechteck = new Rechteck(xKoordinate, yKoordinate, breite, hoehe);
                rechteck.setFarbe(aktuelleFarbe);
                rechteck.setDicke(aktuelleDicke);
                zeichenflaeche.zeichneRechteck(rechteck);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {

            if (Modus.equals("Frei")) {
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();
                Linie linie = new Linie(StartxKoordinate, StartyKoordinate, EndXKoordinate, EndYKoordinate);
                linie.setFarbe(aktuelleFarbe);
                linie.setDicke(aktuelleDicke);
                //das dient als Preview für das freie zeichnen
                aktuelleLinie.add(linie);
                zeichenflaeche.Linien.add(linie);
                repaint();

                StartxKoordinate = EndXKoordinate; //Damit werden ganz viele kleine Linien verbunden zu einer großen
                StartyKoordinate = EndYKoordinate;
            }
            if (Modus.equals("Radieren")) {

            }
            //vorschau
            if(Modus.equals("Kreis")){
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();
                double radiusinDouble = Math.sqrt((StartxKoordinate - EndXKoordinate) * (StartxKoordinate - EndXKoordinate)
                        + (StartyKoordinate - EndYKoordinate) * (StartyKoordinate - EndYKoordinate)); // Berechnung radius mithilfe Pythagoras
                int radius = (int) radiusinDouble;
                int xKoordinate = StartxKoordinate - radius; // Berechnung der Koordinaten des Mittelpunktes
                int yKoordinate = StartyKoordinate - radius;
                Kreis previewkreis = new Kreis(radius * 2, xKoordinate, yKoordinate);
                previewkreis.setFarbe(aktuelleFarbe);
                previewkreis.setDicke(aktuelleDicke);
                zeichenflaeche.setPreviewKreis(previewkreis);

            }
            if(Modus.equals("Rechteck")){
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();

                int xKoordinate = Math.min(StartxKoordinate, EndXKoordinate); //Kleinere Koordinaten nehmen da man in der "linken Oberen" Ecke hier die Koordinaten für die Ellipsen sind
                int yKoordinate = Math.min(StartyKoordinate, EndYKoordinate);
                int breite = Math.abs(EndXKoordinate - StartxKoordinate); //Abstand wird hier berechnet dieser ist durch abs immer positiv
                int hoehe = Math.abs(EndYKoordinate - StartyKoordinate); // hier dasselbe nur für die hoehe statt breite
                Rechteck previewrechteck = new Rechteck(xKoordinate, yKoordinate, breite, hoehe);
                previewrechteck.setFarbe(aktuelleFarbe);
                previewrechteck.setDicke(aktuelleDicke);
                zeichenflaeche.setPreviewRechteck(previewrechteck);
            }
            if(Modus.equals("Ellipse")){
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();

                int xKoordinate = Math.min(StartxKoordinate, EndXKoordinate); //Kleinere Koordinaten nehmen da man in der "linken Oberen" Ecke hier die Koordinaten für die Ellipsen sind
                int yKoordinate = Math.min(StartyKoordinate, EndYKoordinate);
                int breite = Math.abs(EndXKoordinate - StartxKoordinate); //Abstand wird hier berechnet dieser ist durch abs immer positiv
                int hoehe = Math.abs(EndYKoordinate - StartyKoordinate); // hier dasselbe nur für die hoehe statt breite
                Ellipse ellipse = new Ellipse(xKoordinate, yKoordinate, breite, hoehe);
                ellipse.setFarbe(aktuelleFarbe);
                ellipse.setDicke(aktuelleDicke);
                zeichenflaeche.setPreviewEllipse(ellipse);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    public void SpeichernUnter() {
        JFileChooser filter = new JFileChooser();
        filter.setFileFilter(new FileNameExtensionFilter("JPG (*.jpg)","jpg")); //es werden nur jpg dateien angezeigt
        if (filter.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { //Wenn Nutzer speichern will, kann immer noch abbrechen dabei
            File datei = filter.getSelectedFile(); // die datei der der benutzer eingegeben hat
            if (!datei.getName().toLowerCase().endsWith(".jpg"))
                datei = new File(datei.getAbsoluteFile() + ".jpg"); //hiermit wird geprüft ob der Nutzer schon jpg am ende hat wenn nicht wird es angehangen
            BufferedImage Bild = zeichenflaeche.wandleBildinBufferdImage();
            try {
                ImageIO.write(Bild, "jpg", datei); //damit wird datei erzeugt
                aktuelleDatei = datei; //damit ist wenn ich schon enmal richtig gespeiert habe die Datei ungleichnull -Für die Methode zwischen speichern ist das wichtig
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "es gab einen Fehler beim Speichern!");
            }
        }
    }

    public void zwischenspeichern() { //Wie Speichernunter ohne festlegen dateinamen
        if (aktuelleDatei == null) { //Abfrage ob schonmal Speicherortfestgelegt worden ist
            SpeichernUnter();
            return;
        }
        BufferedImage Bild = zeichenflaeche.wandleBildinBufferdImage();
        try {
            ImageIO.write(Bild, "jpg", aktuelleDatei);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "es gab einen Fehler beim Speichern der Datei!");
        }
    }

    public void ladeBild() {
        JFileChooser sucher = new JFileChooser();
        sucher.setFileFilter(new FileNameExtensionFilter("JPG (*.jpg)","jpg")); // es werden nur jpgs angezeigt
        if (sucher.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { //Wenn nutzer wirklich laden will und nicht zwischen durch abbricht
            BufferedImage zuladenesBild;
            try {
                zuladenesBild = ImageIO.read(sucher.getSelectedFile());//Nutzer sucht Bild aus welches geladden werden soll
                zeichenflaeche.reset();
                zeichenflaeche.setBild(zuladenesBild);
                zeichenflaeche.repaint();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Es gab einen Fehler beim Laden des Bildes!");

            }

        }
    }
}