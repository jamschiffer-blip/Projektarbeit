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
    private JMenu datei, stift, formen, radierer, text, hintergrund, Bedienungshilfe, ausfuellen;
    private JMenuItem Speichernmenueleiste, Laden, NeueDatei;
    private boolean mausgedrueckt = false;
    private String Modus = "Frei";
    public Zeichenflaeche zeichenflaeche;
    private Color aktuelleFarbe = Color.BLACK, aktuelleHintergrundfarbe = Color.WHITE;
    public Color aktuelleFuellfarbe = Color.WHITE;  //Public Damit Zeichenflaeche auch draufzugreifen kann
    private float aktuelleDicke = 2.0f;
    private final int Zeichenflaeche_HOEHE = 2000;
    private final int Zeichenflaeche_BREITE = 1500;
    private File aktuelleDatei;
    private Color hellblau = new Color(200, 235, 245);
    private Color textfarbe = new Color(40, 40, 40);
    private ArrayList<Linie> aktuelleLinie = new ArrayList<>();
    private ArrayList<Point> aktuelleRadierung = new ArrayList<>();
    private boolean Aenderungengespeichert = true, neuesFenstererstellen = false;
    public boolean fuelleimerbenutzen = false; //Public Damit Zeichenflaeche auch draufzugreifen kann
    private JPanel Aenderungenungespeichertfenster, neueDateierstellenfenster;
    private JDialog dialog;
    private ButtonGroup Auswahlmodi, Radierradius;

    public GUI() {
        super("Graphic Editor");
        setSize(2000, 2500);
        zeichenflaeche = new Zeichenflaeche();
        zeichenflaeche.setPreferredSize(new Dimension(Zeichenflaeche_BREITE, Zeichenflaeche_HOEHE));

        MeinListener listener = new MeinListener();
        zeichenflaeche.addMouseListener(listener);
        zeichenflaeche.addMouseMotionListener(listener);
        addWindowListener(listener);
        addKeyListener(listener);

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
        Speichern.addActionListener(e -> {
            zwischenspeichern();
            Aenderungengespeichert = true;
        });

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
        Speichernmenueleiste.addActionListener(e -> {
            SpeichernUnter();
            Aenderungengespeichert = true;
        });
        Laden = new JMenuItem("Laden");
        Laden.addActionListener(e -> ladeBild());
        NeueDatei = new JMenuItem("Neue Datei");
        Laden.setActionCommand("laden");
        Speichernmenueleiste.setActionCommand("Speichern Unter");
        NeueDatei.setActionCommand("neue datei");//hier abfrage ob korrekt
        NeueDatei.addActionListener(listener);
        menueleiste.setOpaque(true);
        menueleiste.setBackground(hellblau);

        stift = new JMenu("Stift");
        JMenuItem farbeStift = new JMenuItem("Farbe");
        farbeStift.addActionListener(e -> {
            Color farbe = JColorChooser.showDialog(this, "Stiftfarbe wählen", aktuelleFarbe); //Damit kann die Frabe ausgewähltwerden
            if (farbe != null) aktuelleFarbe = farbe;
        });
        JMenuItem dickeStift = new JMenuItem("Dicke");
        dickeStift.addActionListener(e -> {
            String Eingabe = JOptionPane.showInputDialog(       //Erstellen Eingabefeld um Dicke einzustellen
                    this, "Dicke eingeben: ",
                    aktuelleDicke
            );
            if (Eingabe == null) return;
            try {
                float dicke = Float.parseFloat(Eingabe);
                if (dicke > 0) aktuelleDicke = dicke;
            } catch (NumberFormatException exception) {
            }
        });

        formen = new JMenu("Formen");
        JRadioButtonMenuItem freiesZeichnen = new JRadioButtonMenuItem("Freies Zeichnen", true); //Startzustand
        JRadioButtonMenuItem geradesZeichnen = new JRadioButtonMenuItem("Gerades Zeichnen");
        JRadioButtonMenuItem Kreis = new JRadioButtonMenuItem("Kreis");
        JRadioButtonMenuItem Ellipse = new JRadioButtonMenuItem("Ellipse");
        JRadioButtonMenuItem Rechteck = new JRadioButtonMenuItem("Rechteck");
        JRadioButtonMenuItem Polygon = new JRadioButtonMenuItem("Polygon");

        freiesZeichnen.addActionListener(e -> Modus = "Frei");
        geradesZeichnen.addActionListener(e -> Modus = "Gerade");
        Kreis.addActionListener(e -> Modus = "Kreis");
        Ellipse.addActionListener(e -> Modus = "Ellipse");
        Rechteck.addActionListener(e -> Modus = "Rechteck");
        Polygon.addActionListener(e -> Modus = "Polygon");

        Auswahlmodi = new ButtonGroup();
        Auswahlmodi.add(freiesZeichnen);
        Auswahlmodi.add(geradesZeichnen);
        Auswahlmodi.add(Kreis);
        Auswahlmodi.add(Ellipse);
        Auswahlmodi.add(Rechteck);
        Auswahlmodi.add(Polygon);

        radierer = new JMenu("Radierer");
        JRadioButtonMenuItem kleinerRadierer = new JRadioButtonMenuItem("Kleiner Radierer");
        JRadioButtonMenuItem mittlererRadierer = new JRadioButtonMenuItem("Mittlerer Radierer");
        JRadioButtonMenuItem grosserRadierer = new JRadioButtonMenuItem("Großer Radierer");

        kleinerRadierer.addActionListener(e -> {
            Modus = "Radiere";
            zeichenflaeche.setRadierradius(10);
        });
        grosserRadierer.addActionListener(e -> {
            Modus = "Radiere";
            zeichenflaeche.setRadierradius(30);
        });
        mittlererRadierer.addActionListener(e -> {
            Modus = "Radiere";
            zeichenflaeche.setRadierradius(20);
        });

        hintergrund = new JMenu("Hintergrund");
        JMenuItem farbeHintergrund = new JMenuItem("Farbe");
        farbeHintergrund.addActionListener(e -> {
            Color farbe = JColorChooser.showDialog(this, "Hintergrundfarbe wählen", aktuelleHintergrundfarbe); //Damit kann die Frabe ausgewähltwerden
            if (farbe != null) {
                aktuelleHintergrundfarbe = farbe;
                zeichenflaeche.setHintergrundfarbe(aktuelleHintergrundfarbe);
                zeichenflaeche.repaint();
            }
        });

        ausfuellen = new JMenu("Fülleimer");
        JCheckBoxMenuItem ausfuellencheckbox = new JCheckBoxMenuItem("Ausfüllen?");
        ausfuellencheckbox.addActionListener(e -> {
            boolean aktiv = ausfuellencheckbox.isSelected(); //DAmit wenn sie nicht aktiv ist Fuellfarbebenutzen wieder auf false gesetzt wird nach nutzen
            zeichenflaeche.setFuellFarbebenutzen(aktiv);
        });
        JMenuItem farbeFuellEimer = new JMenuItem("Farbe");
        farbeFuellEimer.addActionListener(e -> {
            Color farbe = JColorChooser.showDialog(this, "Farbe zum Ausfüllen wählen", aktuelleFuellfarbe); //Damit kann die Frabe ausgewähltwerden
            if (farbe != null) {
                aktuelleFuellfarbe = farbe;
                zeichenflaeche.setAktuelleFuellfarbe(aktuelleFuellfarbe);
            }
        });

        Bedienungshilfe = new JMenu("Bedienungshilfe");
        JMenuItem Hilfe = new JMenuItem("Hilfe");
        Hilfe.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Willkomen bei dem Graphic Editor!\nHier ist die Bedienungsanleitung wie man diesen bedienen kann\n" +
                            "᛫Menüpunkt Datei:\n" +
                            " Erstellt,Lädt und Speichert neue Dateien\n" +
                            "\n" +
                            "᛫Menüpunkt Stift:\n" +
                            "Damit kann man Dicke und Farbe des Stiftes auswählen\n" +
                            "\n" +
                            "᛫Menüpunkt Formen:\n" +
                            "Hiermit kann man jeweilige Shapes auswählen,jedoch Achtung beim Polygon! Man wählt die einzelnen Punkte mit einem Click und" +
                            " wenn man den letzten Punkt auswählen will muss man doppelt klicken\n" +
                            "\n" +
                            "᛫Menüpunkt Radierer:\n" +
                            "Hier wählt man die größe des radierers aus\n" +
                            "\n" +
                            "᛫Menüpunkt Hintergrund:\n " +
                            "Hier wählt man die jeweilige Hintergrundfarbe aus\n" +
                            "\n" +
                            "᛫Menüpunkt Fülleimer:\n" +
                            "Hier wählt man aus ob und wie man seine Shapes ausfüllen will\n" +
                            "\n" +
                            "᛫Shortcuts:\n" +
                            "STRG + S zum Speichern\n" +
                            "STRG + Z für UNDO\n" +
                            "STRG + Y für REDO\n" +
                            "STRG + N für eine neue Datei zu erstellen\n" +
                            "STRG + O um ein Bild zu laden\n" +
                            "STRG + Q um Anwendung zu schließen\n" +
                            "+/- um Dicke zu verändern\n" +
                            "ACHTUNG! Tastenkombinationen funktionieren nur wenn man nicht gerdae auf die Menüleiste drückt!",
                            "Bedienungshilfe",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });


        Auswahlmodi.add(kleinerRadierer);       //Eine Buttongroup für alle Sachen, damit kann nur ein Eintrag ausgewählt werden
        Auswahlmodi.add(mittlererRadierer);
        Auswahlmodi.add(grosserRadierer);

        radierer.add(kleinerRadierer);
        radierer.add(mittlererRadierer);
        radierer.add(grosserRadierer);


        formen.add(freiesZeichnen);
        formen.add(geradesZeichnen);
        formen.add(Kreis);
        formen.add(Ellipse);
        formen.add(Rechteck);
        formen.add(Polygon);

        hintergrund.add(farbeHintergrund);

        ausfuellen.add(ausfuellencheckbox);
        ausfuellen.add(farbeFuellEimer);

        Bedienungshilfe.add(Hilfe);

        datei.add(Speichernmenueleiste);
        datei.add(Laden);
        datei.add(NeueDatei);

        stift.add(farbeStift);
        stift.add(dickeStift);

        menueleiste.setLayout(new GridLayout(1,0)); //Damit zwischen Menüpunkten gleichmäßig Platz ist
        menueleiste.add(datei);
        menueleiste.add(stift);
        menueleiste.add(formen);
        menueleiste.add(radierer);
        menueleiste.add(hintergrund);
        menueleiste.add(ausfuellen);
        menueleiste.add(Bedienungshilfe);

        leiste.add(menueleiste);

        datei.setForeground(textfarbe);
        Speichernmenueleiste.setForeground(textfarbe);
        Laden.setForeground(textfarbe);
        NeueDatei.setForeground(textfarbe);


        //Erstellen eines Fensters falls neue Datei erstellt werden soll
        neueDateierstellenfenster = new JPanel();
        JPanel text = new JPanel();
        JPanel buttons = new JPanel();
        JLabel neueDatei = new JLabel("Soll eine neue Datei erstellt werden?");
        JButton erstellen = new JButton("Ja");
        JButton nichterstellen = new JButton("Nein");
        erstellen.addActionListener(e -> {
            neuesFenstererstellen = true;
            dialog.dispose();
        });
        nichterstellen.addActionListener(e -> {
            neuesFenstererstellen = false;
            dialog.dispose();
        });
        neueDateierstellenfenster.setLayout(new GridLayout(2, 1, 50, 50));
        text.add(neueDatei);
        buttons.setLayout(new GridLayout(1, 2, 50, 50));
        buttons.add(erstellen);
        buttons.add(nichterstellen);
        neueDateierstellenfenster.add(text);
        neueDateierstellenfenster.add(buttons);

        //Erstellen eines Fensters falls Änderungen ungespeichert sind
        Aenderungenungespeichertfenster = new JPanel();
        JPanel textfenster2 = new JPanel();
        JPanel buttonsfenster2 = new JPanel();
        JLabel ungespeicherttext = new JLabel("Die Änderungen wurden nicht gespeichert");
        JButton speichern = new JButton("Speichern");
        JButton verwerfen = new JButton("Aenderungen verwerfen");
        speichern.addActionListener(e -> {
            Aenderungengespeichert = true;
            if (aktuelleDatei == null) {
                SpeichernUnter();
            } else zwischenspeichern();
            zeichenflaeche.reset();
            aktuelleDatei = null;
            Aenderungengespeichert = true; //Da noch keine Änderungen es in neue Datei gab
            dialog.dispose();
        });
        verwerfen.addActionListener(e -> {
            zeichenflaeche.reset();
            zeichenflaeche.reset();
            aktuelleDatei = null;
            Aenderungengespeichert = true; //Da noch keine Änderungen es in neue Datei gab
            dialog.dispose();
        });
        Aenderungenungespeichertfenster.setLayout(new GridLayout(2, 1, 50, 50));
        textfenster2.add(ungespeicherttext);
        buttonsfenster2.setLayout(new GridLayout(1, 2, 50, 50));
        buttonsfenster2.add(speichern);
        buttonsfenster2.add(verwerfen);
        Aenderungenungespeichertfenster.add(textfenster2);
        Aenderungenungespeichertfenster.add(buttonsfenster2);


        add(leiste, BorderLayout.NORTH);
        add(zeichenflaeche, BorderLayout.CENTER);
        pack();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //um Pop up zuerstellen falls es änderungen gibt
        //Um fokus nur auf zeichenfläche damit Tastenkombinationen funktionieren
        zeichenflaeche.setFocusable(true);
        zeichenflaeche.addKeyListener(listener);

        setVisible(true);
        zeichenflaeche.requestFocusInWindow();
    }

    class MeinListener implements ActionListener, MouseListener, MouseMotionListener, WindowListener, KeyListener {


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
            //ABfrage ob wirklich neue Datei erstellt werden soll
            if (e.getActionCommand().equals("neue datei")) {

                if (Aenderungengespeichert) {
                    JOptionPane PopupneuesFenstererstellen = new JOptionPane(

                            neueDateierstellenfenster,
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.DEFAULT_OPTION,
                            null,
                            new Object[]{} //wichtig da sonst OK Button gibt
                    );
                    dialog = PopupneuesFenstererstellen.createDialog(GUI.this, "Neues Fenster erstellen?");
                    dialog.setVisible(true);
                    if (neuesFenstererstellen) {
                        zeichenflaeche.reset();
                        aktuelleDatei = null;
                        Aenderungengespeichert = true; //Da noch keine Änderungen es in neue Datei gab
                    }
                }


                if (!Aenderungengespeichert) { //Wenn es ungespeicherte Änderungen gab kommt popuüp
                    JOptionPane PopupneuesFenstererstellen = new JOptionPane(

                            neueDateierstellenfenster,
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.DEFAULT_OPTION,
                            null,
                            new Object[]{} //wichtig da sonst OK Button gibt
                    );
                    dialog = PopupneuesFenstererstellen.createDialog(GUI.this, "Neues Fenster erstellen");
                    dialog.setVisible(true);
                    if (neuesFenstererstellen) { //nur wenn wirklich ein neues Fenster erstellt werden soll kommt die Abfrage ob man die Änderungen speichern will
                        JOptionPane ungespeicherteAenderungen = new JOptionPane(

                                Aenderungenungespeichertfenster,
                                JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.DEFAULT_OPTION,
                                null,
                                new Object[]{} //wichtig da sonst OK Button gibt
                        );
                        dialog = ungespeicherteAenderungen.createDialog(GUI.this, "Änderungen ungespeichert");
                        dialog.setVisible(true);
                    }
                }

            }
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
                if (counterPolygon >= 2) {
                    int[] xKoordinatenPreview = Arrays.copyOf(xKoordinatenPolygon, counterPolygon);
                    int[] yKoordinatenPreview = Arrays.copyOf(yKoordinatenPolygon, counterPolygon);
                    Polygon previewPolygon = new Polygon(xKoordinatenPreview, yKoordinatenPreview);
                    previewPolygon.setDicke(aktuelleDicke);
                    previewPolygon.setFarbe(aktuelleFarbe);
                    zeichenflaeche.setPreviewPolygon(previewPolygon);
                }
                if (counterPolygon >= 3 && e.getClickCount() == 2) //Clickcount==2 da man dann mit einem Doppelklick das Polygon abschliessen kann
                {
                    zeichenflaeche.clearPreviewPolygon();
                    Aenderungengespeichert = false;
                    int[] xKoordinaten = Arrays.copyOf(xKoordinatenPolygon, counterPolygon);
                    int[] yKoordinaten = Arrays.copyOf(yKoordinatenPolygon, counterPolygon);
                    Polygon polygon = new Polygon(xKoordinaten, yKoordinaten);
                    polygon.setFarbe(aktuelleFarbe);
                    polygon.setDicke(aktuelleDicke);
                    if(zeichenflaeche.isFuellFarbebenutzen()) polygon.setFuellfarbe(aktuelleFuellfarbe);
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
            zeichenflaeche.requestFocusInWindow(); //Damit focus bei jedem Click da bleibt und shortcuts funktionieren
            if (Modus.equals("Frei")) {
                aktuelleLinie.clear(); //Liste der ganz vielen kleinen Striche die ein grossen Strich bilden wird leer gemacht
                StartxKoordinate = e.getX();
                StartyKoordinate = e.getY();
                Aenderungengespeichert = false;
            }
            if (Modus.equals("Radiere")) {
                aktuelleRadierung.clear(); //Liste der ganz vielen Radierpunkten wird leer gemacht
                aktuelleRadierung.add(new Point(e.getX(), e.getY())); //Liste ganzvieler Radierpunkte wird erstmakls gefüllt
                zeichenflaeche.Radiererpunkte.add(new Point(e.getX(), e.getY())); //Preview wird sofort sichtbar
                Aenderungengespeichert = false;
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
                        Aenderungengespeichert = false;
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
            if (Modus.equals("Frei")) {
                if (!aktuelleLinie.isEmpty()) {
                    zeichenflaeche.Linien.removeAll(aktuelleLinie); //damit werden alle Linien die als Preview dienen gelöscht um Un und redo möglichzumache
                    zeichenflaeche.zeichneLinie(new ArrayList<>(aktuelleLinie));// es wird die methode zeichnelinie für eine edngültige linie aufgerufen
                    Aenderungengespeichert = false;
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
                if(zeichenflaeche.isFuellFarbebenutzen()) ellipse.setFuellfarbe(aktuelleFuellfarbe);
                zeichenflaeche.zeichneEllipse(ellipse);
            }
            if (Modus.equals("Kreis")) {
                zeichenflaeche.clearPreviewKreis();
                Aenderungengespeichert = false;
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
                if(zeichenflaeche.isFuellFarbebenutzen()) kreis.setFuellfarbe(aktuelleFuellfarbe);
                zeichenflaeche.zeichneKreis(kreis);
            }
            if (Modus.equals("Rechteck")) {
                zeichenflaeche.clearPreviewRechteck();
                Aenderungengespeichert = false;
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();

                int xKoordinate = Math.min(StartxKoordinate, EndXKoordinate); //Kleinere Koordinaten nehmen da man in der "linken Oberen" Ecke hier die Koordinaten für die Ellipsen sind
                int yKoordinate = Math.min(StartyKoordinate, EndYKoordinate);
                int breite = Math.abs(EndXKoordinate - StartxKoordinate); //Abstand wird hier berechnet dieser ist durch abs immer positiv
                int hoehe = Math.abs(EndYKoordinate - StartyKoordinate); // hier dasselbe nur für die hoehe statt breite
                Rechteck rechteck = new Rechteck(xKoordinate, yKoordinate, breite, hoehe);
                rechteck.setFarbe(aktuelleFarbe);
                rechteck.setDicke(aktuelleDicke);
                if(zeichenflaeche.isFuellFarbebenutzen()) rechteck.setFuellfarbe(aktuelleFuellfarbe);

                zeichenflaeche.zeichneRechteck(rechteck);
            }
            if (Modus.equals("Radiere")) {
                if (!aktuelleRadierung.isEmpty()) {
                    zeichenflaeche.Radiererpunkte.removeAll(aktuelleRadierung); //preview wird wieder entfernt
                    //Kopie nötig, da aktuelleRadierung bei Undo und Redo geleert wird
                    zeichenflaeche.radiere(new ArrayList<>(aktuelleRadierung)); //gesamter radiervorgang wird endgültig beendet, es wird der radierstrich hinzugefügt
                    aktuelleRadierung.clear();
                    Aenderungengespeichert = false;
                }
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
            if (Modus.equals("Radiere")) {
                Point aktuellerPunkt = new Point(e.getX(), e.getY());
                aktuelleRadierung.add(aktuellerPunkt); //Alle punkte über die radierer gezogen wird werden zum Aktuelle Liste hinzugefügt
                zeichenflaeche.Radiererpunkte.add(aktuellerPunkt); //für Preview
                repaint();
                Aenderungengespeichert = false;
            }
            //vorschau
            if (Modus.equals("Kreis")) {
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
            if (Modus.equals("Rechteck")) {
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
            if (Modus.equals("Ellipse")) {
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

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            if (!Aenderungengespeichert) {
                JOptionPane ungespeicherteAenderungen = new JOptionPane(

                        Aenderungenungespeichertfenster,
                        JOptionPane.PLAIN_MESSAGE,
                        JOptionPane.DEFAULT_OPTION,
                        null,
                        new Object[]{} //wichtig da sonst OK Button gibt
                );
                dialog = ungespeicherteAenderungen.createDialog(GUI.this, "Änderungen ungespeichert");
                dialog.setVisible(true);
            }
            GUI.this.dispose(); //Hauptfenster wird geschlossen
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {
            //Dicke erhöhen mit +
            if(e.getKeyChar() == '+') aktuelleDicke++;
            //Dicke reduzieren mit -
            if(e.getKeyChar() == '-') aktuelleDicke--;
        }

        //Tastenkombinationen
        @Override
        public void keyPressed(KeyEvent e) {
            //Speichern STRG + S
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) { //ConrolDown bedeutet STrG gedrückt und Keyevent.VK_[Buchstabe] jeweeilige Buchstabe
                zwischenspeichern();
                Aenderungengespeichert = true;
            }
            //Undo STRG + Z
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
                zeichenflaeche.undo();
            }
            //Redo STRG + Y
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y) {
                zeichenflaeche.redo();
            }
            //Neue Datei STRG + N
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_N) {
                zeichenflaeche.reset();
                aktuelleDatei = null;
            }
            //Datei laden STRG + O
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O) {
                ladeBild();
            }
            //Beenden STRG + Q
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Q) {
                dispose();
            }

        }
        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    public void SpeichernUnter() {
        JFileChooser filter = new JFileChooser();
        filter.setFileFilter(new FileNameExtensionFilter("JPG (*.jpg)", "jpg")); //es werden nur jpg dateien angezeigt
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
        sucher.setFileFilter(new FileNameExtensionFilter("JPG (*.jpg)", "jpg")); // es werden nur jpgs angezeigt
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