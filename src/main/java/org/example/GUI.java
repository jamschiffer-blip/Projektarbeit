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
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame {

    private JButton Undo, Redo, Speichern;
    private JPanel leiste;
    private JToolBar symbolleiste;
    private JMenuBar menueleiste;
    private JMenu datei, stift, formen, radierer, text, hintergrund, Bedienungshilfe, ausfuellen;
    private JMenuItem Speichernmenueleiste, Laden, NeueDatei;
    private String Modus = "Frei";
    public Zeichenflaeche zeichenflaeche;
    private Color aktuelleFarbe = Color.BLACK, aktuelleHintergrundfarbe = Color.WHITE;
    public Color aktuelleFuellfarbe = Color.WHITE;  //public damit Zeichenflaeche auch draufzugreifen kann
    private float aktuelleDicke = 2.0f;
    private final int Zeichenflaeche_HOEHE = 2000;
    private final int Zeichenflaeche_BREITE = 1500;
    private File aktuelleDatei;
    private Color hellblau = new Color(200, 235, 245);
    private Color textfarbe = new Color(40, 40, 40);
    private ArrayList<Linie> aktuelleLinie = new ArrayList<>();
    private ArrayList<Point> aktuelleRadierung = new ArrayList<>();
    private boolean Aenderungengespeichert = true, neuesFenstererstellen = false;
    private JPanel Aenderungenungespeichertfenster, neueDateierstellenfenster;
    private JDialog dialog;
    private ButtonGroup Auswahlmodi, Radierradius;
    private int schriftgroesse = 20;
    private Font schriftart = new Font(Font.SANS_SERIF,Font.PLAIN,schriftgroesse);
    private Color aktuelleTextFarbe = Color.BLACK;

    public GUI() {
        /*
        Erstellen des Fensters
         */
        super("Graphic Editor");
        setSize(2000, 2500);
        zeichenflaeche = new Zeichenflaeche();
        zeichenflaeche.setPreferredSize(new Dimension(Zeichenflaeche_BREITE, Zeichenflaeche_HOEHE));
        setLayout(new BorderLayout());

        /*
        Verbinden mit den Listenern
         */
        MeinListener listener = new MeinListener();
        zeichenflaeche.addMouseListener(listener.mouseAdapter);      //Diese beiden Listener braucht man nur zum Zeichnen, deswegen bekommt zeichenflaeche nur sie
        zeichenflaeche.addMouseMotionListener(listener.mouseMotionAdapter);
        addWindowListener(listener.windowAdapter);




        /*
        Erstellen der Symbolleiste
         */
        symbolleiste = new JToolBar();
        symbolleiste.setFloatable(false); //Dadurch kann man die symbolleistze mit der Maus nicht mehr wegziehen


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

        /*
        Erstellen der Menüleiste
         */

        menueleiste = new JMenuBar();

        //Erstellen des Menüpunktes Datei
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
        NeueDatei.setActionCommand("neue datei");
        NeueDatei.addActionListener(listener);

        datei.add(Speichernmenueleiste);
        datei.add(Laden);
        datei.add(NeueDatei);

        //Erstellen des Menüpunktes Stift
        stift = new JMenu("Stift");
        JMenuItem farbeStift = new JMenuItem("Farbe");
        farbeStift.addActionListener(e -> {
            Color farbe = JColorChooser.showDialog(this, "Stiftfarbe wählen", aktuelleFarbe); //Damit kann die Frabe ausgewählt werden
            if (farbe != null) aktuelleFarbe = farbe;
        });
        JMenuItem dickeStift = new JMenuItem("Dicke");
        dickeStift.addActionListener(e -> {
            String Eingabe = JOptionPane.showInputDialog(       //Erstellen eines Eingabefeldes um Dicke einzustellen
                    this, "Dicke eingeben: ",
                    aktuelleDicke
            );
            if (Eingabe == null) return;    //Wenn Eingabe abgebrochen wird, soll vorgang abgebrochen werden
            try {
                float dicke = Float.parseFloat(Eingabe);    //nur für gültige Eingaben der Dicke
                if (dicke > 0 && dicke <=50) aktuelleDicke = dicke;
                else if(dicke<=0) throw new NumberFormatException("Wert muss positiv sein!");
                else if(dicke>50) throw new NumberFormatException("Wert ist zu groß!");
            } catch (NumberFormatException exception) {
                //Erstellen eines Fensters bei falscher Eingabe
                JOptionPane.showMessageDialog(
                        this,
                        "Gültige positive Zahl unter 50 eingeben",
                        "Ungültige Eingabe",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        stift.add(farbeStift);
        stift.add(dickeStift);

        //Erstellen des Menüpunktes Formen für die Shapes
        formen = new JMenu("Formen");
        JRadioButtonMenuItem freiesZeichnen = new JRadioButtonMenuItem("Freies Zeichnen", true); //Startzustand ist Freihand Zeichnen
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

        Auswahlmodi = new ButtonGroup();    //Buttongroup damit nur ein Shape gleichzeitig ausgewählt werden kann
        Auswahlmodi.add(freiesZeichnen);
        Auswahlmodi.add(geradesZeichnen);
        Auswahlmodi.add(Kreis);
        Auswahlmodi.add(Ellipse);
        Auswahlmodi.add(Rechteck);
        Auswahlmodi.add(Polygon);


        formen.add(freiesZeichnen);
        formen.add(geradesZeichnen);
        formen.add(Kreis);
        formen.add(Ellipse);
        formen.add(Rechteck);
        formen.add(Polygon);


        //Erstellen des Menüpunktes Radierer
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
        Auswahlmodi.add(kleinerRadierer);       //Eine Buttongroup für Radierer und Shapes, damit nur ein Modus ausgewählt werden kann
        Auswahlmodi.add(mittlererRadierer);
        Auswahlmodi.add(grosserRadierer);

        radierer.add(kleinerRadierer);
        radierer.add(mittlererRadierer);
        radierer.add(grosserRadierer);

        //Erstellen des Menüpunktes Hintergrund, um die Hintergrundfarbe beliebig ändern zu können
        hintergrund = new JMenu("Hintergrund");
        JMenuItem farbeHintergrund = new JMenuItem("Farbe");
        farbeHintergrund.addActionListener(e -> {
            Color farbe = JColorChooser.showDialog(this, "Hintergrundfarbe wählen", aktuelleHintergrundfarbe); //Damit kann die Frabe ausgewählt werden
            if (farbe != null) {
                aktuelleHintergrundfarbe = farbe;
                zeichenflaeche.setHintergrundfarbe(aktuelleHintergrundfarbe);
                zeichenflaeche.repaint();
            }
        });

        hintergrund.add(farbeHintergrund);

        //Erstellen des Menüpunktes Fülleimer um gegebenenfalls Shapes mit ausgewählter Füllung zu zeichnen
        ausfuellen = new JMenu("Fülleimer");
        JCheckBoxMenuItem ausfuellencheckbox = new JCheckBoxMenuItem("Ausfüllen?");
        ausfuellencheckbox.addActionListener(e -> {
            boolean aktiv = ausfuellencheckbox.isSelected(); //Hiermit überprüft man ob bei der Checkbox der Haken gesetzt ist
            zeichenflaeche.setFuellFarbebenutzen(aktiv);
        });
        JMenuItem farbeFuellEimer = new JMenuItem("Farbe");
        farbeFuellEimer.addActionListener(e -> {
            Color farbe = JColorChooser.showDialog(this, "Farbe zum Ausfüllen wählen", aktuelleFuellfarbe);
            if (farbe != null) {
                aktuelleFuellfarbe = farbe;
                zeichenflaeche.setAktuelleFuellfarbe(aktuelleFuellfarbe);
            }
        });

        ausfuellen.add(ausfuellencheckbox);
        ausfuellen.add(farbeFuellEimer);

        //Erstellung des Menüpunktes Bedienunghsilfe, um dem Nutzer ein Hilfsfenster zu geben bei Bedarf
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
                            "\n" +
                            "ALT + D für Menüpunkt Datei\n" +
                            "ALT + S für Menüpunkt Stift\n" +
                            "ALT + F für Menüpunkt Formen\n" +
                            "ALT + B für Menüpunkt Bedienungshilfe\n" +
                            "ALT + E für Menüpunkt Radierer\n" +
                            "ALT + H für Menüpunkt Hintergrund\n" +
                            "ALT + T für Menüpunkt Text\n" +
                            "ALT + U für Menüpunkt Fülleimer\n" ,
                            "Bedienungshilfe",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        Bedienungshilfe.add(Hilfe);
        /*
        Erstellen des Menüpunktes Text
         */
        text = new JMenu("Text");
        JRadioButtonMenuItem Text = new JRadioButtonMenuItem("Text");
        Text.addActionListener(e -> Modus = "Text");
        JMenuItem farbeText = new JMenuItem("Farbe");
        farbeText.addActionListener(e -> {
            Color farbe = JColorChooser.showDialog(this, "Textfarbe wählen", aktuelleTextFarbe); //Damit kann die Frabe ausgewählt werden
            if (farbe != null) aktuelleTextFarbe = farbe;
        });
        JMenuItem dickeText = new JMenuItem("Dicke");
        dickeText.addActionListener(e -> {
            String Eingabe = JOptionPane.showInputDialog(       //Erstellen eines Eingabefeldes um Dicke einzustellen
                    this, "Dicke eingeben: ",
                    schriftgroesse
            );
            if (Eingabe == null) return;    //Wenn Eingabe abgebrochen wird, soll vorgang abgebrochen werden
            try {
                int dicke = Integer.parseInt(Eingabe);    //nur für gültige Eingaben der Dicke
                if (dicke > 0 && dicke <=50) schriftgroesse = dicke;
                else if(dicke<=0) throw new NumberFormatException("Wert muss positiv sein!");
                else if(dicke>30) throw new NumberFormatException("Wert ist zu groß!");
            } catch (NumberFormatException exception) {
                //Erstellen eines Fensters bei falscher Eingabe
                JOptionPane.showMessageDialog(
                        this,
                        "Gültige positive Zahl unter 50 eingeben",
                        "Ungültige Eingabe",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        Auswahlmodi.add(Text);     //Damit nur ein Modus ausgewählt sein kann
        text.add(Text);
        text.add(dickeText);
        text.add(farbeText);


        menueleiste.setOpaque(true);
        menueleiste.setBackground(hellblau);
        menueleiste.setLayout(new GridLayout(1,0)); //Gridlayout damit zwischen Menüpunkten gleichmäßig Platz ist
        menueleiste.add(datei);
        menueleiste.add(stift);
        menueleiste.add(formen);
        menueleiste.add(radierer);
        menueleiste.add(hintergrund);
        menueleiste.add(ausfuellen);
        menueleiste.add(text);
        menueleiste.add(Bedienungshilfe);

        /*
        Verbinden der Symbolleiste und Menüleiste zum Panel leiste für Ordnung
         */
        leiste = new JPanel();
        leiste.setLayout(new BoxLayout(leiste, BoxLayout.Y_AXIS));

        leiste.setOpaque(true);
        leiste.setBackground(hellblau);
        leiste.add(symbolleiste, BorderLayout.WEST); //Die Symbolleiste bleibt immer Links oben
        leiste.add(menueleiste);


        //Formatierung
        datei.setForeground(textfarbe);
        Speichernmenueleiste.setForeground(textfarbe);
        Laden.setForeground(textfarbe);
        NeueDatei.setForeground(textfarbe);


        /*
        Erstellen eines Fenster, das erscheint wenn eine neue Datei erstellt werden soll
         */
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

        /*
        Erstellen eines neuen Fensters, das erscheint bei ungespeicherten Änderungen
         */
        Aenderungenungespeichertfenster = new JPanel();
        JPanel textfenster2 = new JPanel();
        JPanel buttonsfenster2 = new JPanel();
        JLabel ungespeicherttext = new JLabel("Die Änderungen wurden nicht gespeichert");
        JButton speichern = new JButton("Speichern");
        JButton verwerfen = new JButton("Aenderungen verwerfen");
        speichern.addActionListener(e -> {
            Aenderungengespeichert = true;
            if (aktuelleDatei == null) {    //Wenn noch kein Speiocherortfestgelegt worden ist wird SpeichernUnter() aufgerufen
                SpeichernUnter();
            } else zwischenspeichern();
            zeichenflaeche.reset();
            aktuelleDatei = null;          //Dadurch muss der Nutzer neuen Speicherort festlegen
            Aenderungengespeichert = true; //Da es noch keine Änderungen in neuer Datei gab
            dialog.dispose();
        });
        verwerfen.addActionListener(e -> {
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

        /*
        Globaler KeyEventDispatcher, damit Tastenkombinationen immer funktionieren auch wenn der Fokus
        auf Menüleiste ist
         */
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            //Tastenkombination werden an Listener weitergegebn
            if(e.getID()==KeyEvent.KEY_PRESSED) listener.keyAdapter.keyPressed(e);
            if(e.getID()==KeyEvent.KEY_TYPED) listener.keyAdapter.keyTyped(e);

            return false; //Dadurch darf der Listener die Tastenkombination weiterverwenden
        });

        /*
        Hinzufügen der einzelnen Elemente zur Anwendung
         */
        add(leiste, BorderLayout.NORTH);
        add(zeichenflaeche, BorderLayout.CENTER);
        pack();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //um Pop up Fenster zu ermöglichen falls es änderungen gibt

        zeichenflaeche.setFocusable(true);  //Um Fokus nur auf der Zeichenfläche zu haben damit Tastenkombinationen funktionieren


        setVisible(true);
        zeichenflaeche.requestFocusInWindow();
    }
    /*
    Erstellen der inneren Klasse MeinListener, diese benutzt die verschiedenen Adapter der anderen Listener um benötigte Methoden
    zu benutzen
     */
    class MeinListener implements ActionListener {


        int counter = 0;
        int StartxKoordinate;
        int StartyKoordinate;
        int EndXKoordinate;
        int EndYKoordinate;
        int counterPolygon = 0;
        int[] xKoordinatenPolygon = new int[100]; //Das Polygon darf maximal 100 Punkte haben
        int[] yKoordinatenPolygon = new int[100];
        /*
        Überschreiben der Methoden vom ActionListener
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            //Abfrage ob wirklich neue Datei erstellt werden soll
            if (e.getActionCommand().equals("neue datei")) {

                if (Aenderungengespeichert) { //Wenn Änderungen gespeichert sind
                    JOptionPane PopupneuesFenstererstellen = new JOptionPane(

                            neueDateierstellenfenster,
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.DEFAULT_OPTION,
                            null,
                            new Object[]{} //wichtig da es sonst einen OK Button gibt
                    );
                    dialog = PopupneuesFenstererstellen.createDialog(GUI.this, "Neues Fenster erstellen?");
                    dialog.setVisible(true);
                    if (neuesFenstererstellen) {
                        zeichenflaeche.reset();
                        aktuelleDatei = null;          //Nutzer muss neuen Speicherort festlegen
                        Aenderungengespeichert = true; //Da noch keine Änderungen es in neue Datei gab
                    }
                }

                if (!Aenderungengespeichert) { //Wenn es ungespeicherte Änderungen gibt
                    JOptionPane PopupneuesFenstererstellen = new JOptionPane(

                            neueDateierstellenfenster,
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.DEFAULT_OPTION,
                            null,
                            new Object[]{} //Wichtig da es sonst OK Button gibt
                    );
                    dialog = PopupneuesFenstererstellen.createDialog(GUI.this, "Neues Fenster erstellen");
                    dialog.setVisible(true);
                    if (neuesFenstererstellen) { //Nur wenn wirklich ein neues Fenster erstellt werden soll kommt die Abfrage, ob man die Änderungen speichern will
                        JOptionPane ungespeicherteAenderungen = new JOptionPane(

                                Aenderungenungespeichertfenster,
                                JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.DEFAULT_OPTION,
                                null,
                                new Object[]{} //wichtig da sonst einen OK Button gibt
                        );
                        dialog = ungespeicherteAenderungen.createDialog(GUI.this, "Änderungen ungespeichert");
                        dialog.setVisible(true);
                    }
                }

            }
        }
        /*
        Überschreiben benötigter Methoden vom MouseListener
         */
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double distanz;

                if (Modus.equals("Frei")) { //Hier werden die Startkoordinaten für das freie Zeichnen beim erstmaligen Klicken erstellt
                    StartxKoordinate = e.getX();
                    StartyKoordinate = e.getY();
                }
            /*
            Hiremit kann der Nutzer ein Polygon erstllen
             */
                if (Modus.equals("Polygon")) {

                    if (counterPolygon < 100) { //Der Nutzer kann nur ein Polygon mit maximal 100 Punkten machen
                        xKoordinatenPolygon[counterPolygon] = e.getX();
                        yKoordinatenPolygon[counterPolygon] = e.getY();
                        counterPolygon++;
                    }
                    //Preview
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
                        //Erstellen des richtigen Polygons
                        zeichenflaeche.clearPreviewPolygon();
                        Aenderungengespeichert = false;
                        int[] xKoordinaten = Arrays.copyOf(xKoordinatenPolygon, counterPolygon); //Dadurch wird nur der wirklich benutzte Teil des benutzt um ein Polygon zu zeichnen
                        int[] yKoordinaten = Arrays.copyOf(yKoordinatenPolygon, counterPolygon);
                        Polygon polygon = new Polygon(xKoordinaten, yKoordinaten);
                        polygon.setFarbe(aktuelleFarbe);
                        polygon.setDicke(aktuelleDicke);
                        if(zeichenflaeche.isFuellFarbebenutzen()) polygon.setFuellfarbe(aktuelleFuellfarbe);
                        zeichenflaeche.zeichnePolygon(polygon);
                        counterPolygon = 0; //Für nächstes Polygon den counter zurücksetzen
                    }

                }
            /*
            Hiermit erstellt der Nutzer bei einem Click auf die Zeichenfläche einen Text, es wird ein Fenster
            aufgerufen, in dem der Nutzer seinen Text eingeben soll
             */
                if(Modus.equals("Text")){
                    String Eingabe = JOptionPane.showInputDialog("Text eingeben:");
                    if(Eingabe == null) return; //Wenn es keine Eingabe gibt soll einfach fortgefahren werden
                    zeichenflaeche.zeichneText(new Text(Eingabe,e.getX(),e.getY(),aktuelleTextFarbe,new Font(Font.SANS_SERIF,Font.PLAIN,schriftgroesse)));
                    Aenderungengespeichert = false;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                zeichenflaeche.requestFocusInWindow(); //Damit der Fokus bei jedem Click in der Zeichenfläche bleibt und Shortcuts funktionieren
                if (Modus.equals("Frei")) {
                    aktuelleLinie.clear(); //Liste der ganz vielen kleinen Striche die ein grossen Strich bilden wird leer gemacht
                    StartxKoordinate = e.getX(); //Startkoordinaten werden gesetzt
                    StartyKoordinate = e.getY();
                    Aenderungengespeichert = false;
                    zeichenflaeche.clearPreviewFreihand(); //Um neues Previe zu zeichnen
                }
                if (Modus.equals("Radiere")) {
                    aktuelleRadierung.clear(); //Liste der ganz vielen Radierpunkten wird leer gemacht, die einen Radierstrich bilden
                    aktuelleRadierung.add(new Point(e.getX(), e.getY())); //Liste ganzvieler Radierpunkte wird erstmals gefüllt
                    zeichenflaeche.Radiererpunkte.add(new Point(e.getX(), e.getY())); //Preview wird sofort sichtbar
                    Aenderungengespeichert = false;

                    zeichenflaeche.setPreviewRadierer(new ArrayList<>(aktuelleRadierung)); //Es wird ein Preview erstellt
                }
            /*
            Hiermit kann der Nutzern in dem er zwei Punkte markiert eine gerade Linie zeichnen
             */
                if (Modus.equals("Gerade")) {

                    switch (counter) {
                        case 0:
                            //Setzung Startkoordinaten
                            counter++;
                            StartyKoordinate = e.getY();
                            StartxKoordinate = e.getX();
                            break;


                        case 1:
                            //Verbinden Start mit Endkoordinaten
                            counter = 0; //counter wird für näctse Linie zurückgesetzt
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
                //Startkootdinaten für die Shapes außer Polygon werden gesetzt
                if (Modus.equals("Ellipse") || Modus.equals("Kreis") || Modus.equals("Rechteck")) {
                    StartxKoordinate = e.getX();
                    StartyKoordinate = e.getY();

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            /*
            Nach Loslassen der Maus werden beim freien Zeichen, beim Zeichnen von Ellipsen,Rechtecken und Kreisen und beim Radieren
            die Previews gelöscht und der finale Shape gesetzt. Beim Radieren wird im Grunde auch eine große Line gezeichnet in der
            jeweiligen Hintergrundfarbe
            Zusätzlich wird am Ende jedes Shapes abgefragt ob dieser gefüllt werden soll
             */
                if (Modus.equals("Frei")) {
                    if (!aktuelleLinie.isEmpty()) {
                        zeichenflaeche.clearPreviewFreihand();
                        zeichenflaeche.zeichneLinie(new ArrayList<>(aktuelleLinie)); //Es wird zeichnelinie für eine endgültige Linie aufgerufen
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
                    int hoehe = Math.abs(EndYKoordinate - StartyKoordinate); //Hier dasselbe nur für die Hoehe statt für die Breite
                    Ellipse ellipse = new Ellipse(xKoordinate, yKoordinate, breite, hoehe);
                    ellipse.setFarbe(aktuelleFarbe);
                    ellipse.setDicke(aktuelleDicke);
                    if(zeichenflaeche.isFuellFarbebenutzen()) ellipse.setFuellfarbe(aktuelleFuellfarbe); //Zuerst füllen dann Randlinien zeichnen falls es gefüllt werden soll
                    zeichenflaeche.zeichneEllipse(ellipse);
                }
                if (Modus.equals("Kreis")) {
                    zeichenflaeche.clearPreviewKreis();
                    Aenderungengespeichert = false;
                    EndXKoordinate = e.getX();
                    EndYKoordinate = e.getY();
                    double radiusinDouble = Math.sqrt((StartxKoordinate - EndXKoordinate) * (StartxKoordinate - EndXKoordinate)
                            + (StartyKoordinate - EndYKoordinate) * (StartyKoordinate - EndYKoordinate)); //Berechnung des Radius mithilfe des Pythagoras
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

                    int xKoordinate = Math.min(StartxKoordinate, EndXKoordinate); //Kleinere Koordinaten des linken oberen Eckpunktes
                    int yKoordinate = Math.min(StartyKoordinate, EndYKoordinate);
                    int breite = Math.abs(EndXKoordinate - StartxKoordinate); //Abstand wird hier berechnet dieser ist durch abs immer positiv
                    int hoehe = Math.abs(EndYKoordinate - StartyKoordinate);
                    Rechteck rechteck = new Rechteck(xKoordinate, yKoordinate, breite, hoehe);
                    rechteck.setFarbe(aktuelleFarbe);
                    rechteck.setDicke(aktuelleDicke);
                    if(zeichenflaeche.isFuellFarbebenutzen()) rechteck.setFuellfarbe(aktuelleFuellfarbe);

                    zeichenflaeche.zeichneRechteck(rechteck);
                }
                if (Modus.equals("Radiere")) {
                    if (!aktuelleRadierung.isEmpty()) {
                        zeichenflaeche.clearPreviewRadierer(); //preview wird wieder entfernt
                        //Kopie nötig, da aktuelleRadierung bei Undo und Redo geleert wird
                        zeichenflaeche.radiere(new ArrayList<>(aktuelleRadierung)); //Gesamter radiervorgang wird endgültig beendet, es wird der radierstrich hinzugefügt
                        aktuelleRadierung.clear();
                        Aenderungengespeichert = false;
                    }
                }

            }
        };
        /*
        Überschreiben benötigter Methoden vom MouseMotionListener
         */
        MouseMotionAdapter mouseMotionAdapter = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
            /*
            Hier werden die verschiedenen Previews erstellt, die entstehen wenn der Nutzer die Maustaste gedrückt hält
            Zusätzlich werden beim freien Zeichnen und beim Radieren sich all die Punkte gemerkt die radiert werden sollen,
            beziehungsweise all die kleinen Linien die erstellt werden sollen
             */
                if (Modus.equals("Frei")) {
                    EndXKoordinate = e.getX();
                    EndYKoordinate = e.getY();
                    Linie linie = new Linie(StartxKoordinate, StartyKoordinate, EndXKoordinate, EndYKoordinate);
                    linie.setFarbe(aktuelleFarbe);
                    linie.setDicke(aktuelleDicke);
                    aktuelleLinie.add(linie);

                    zeichenflaeche.setPreviewFreihand(new ArrayList<>(aktuelleLinie)); //Aktuelle linie wird kopiert und als preview genutzt

                    StartxKoordinate = EndXKoordinate; //Damit werden ganz viele kleine Linien verbunden zu einer großen
                    StartyKoordinate = EndYKoordinate;
                }
                if (Modus.equals("Radiere")) {
                    Point aktuellerPunkt = new Point(e.getX(), e.getY());
                    aktuelleRadierung.add(aktuellerPunkt); //Alle Punkte über die der Radierer gezogen wird, werden zum Aktuelle Liste hinzugefügt
                    zeichenflaeche.setPreviewRadierer(new ArrayList<>(aktuelleRadierung)); //Preview
                    Aenderungengespeichert = false;
                }

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

                    int xKoordinate = Math.min(StartxKoordinate, EndXKoordinate); //Kleinere Koordinaten nehmen für den obern linken Eckpunktes des Rechtecks
                    int yKoordinate = Math.min(StartyKoordinate, EndYKoordinate);
                    int breite = Math.abs(EndXKoordinate - StartxKoordinate); //Abstand wird hier berechnet, dieser ist durch abs immer positiv
                    int hoehe = Math.abs(EndYKoordinate - StartyKoordinate);
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
                    int breite = Math.abs(EndXKoordinate - StartxKoordinate); //Abstand wird hier berechnet, dieser ist durch abs immer positiv
                    int hoehe = Math.abs(EndYKoordinate - StartyKoordinate);
                    Ellipse ellipse = new Ellipse(xKoordinate, yKoordinate, breite, hoehe);
                    ellipse.setFarbe(aktuelleFarbe);
                    ellipse.setDicke(aktuelleDicke);
                    zeichenflaeche.setPreviewEllipse(ellipse);
                }
            }
        };
        /*
        Überschreiben benötiger Methoden vom WindowListener
         */
        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            /*
            Wenn das Fenster geschlossen wird und es ungespeicherte Änderungen gab, soll abgefragt weden
            ob diese gespeichert werden sollen
             */
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
        };
        /*
        Jetzt kommen die Tastenkombinationen mit dem KeyListener
         */
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

                //Dicke erhöhen mit +
                if(e.getKeyChar() == '+'){
                    if(aktuelleDicke<=50)  //Sonst wird das Programm langsamer
                        aktuelleDicke++;
                }
                //Dicke reduzieren mit -
                if(e.getKeyChar() == '-'){
                    if(aktuelleDicke>0) //Keine negative Dicke
                        aktuelleDicke--;
                }
            }


            @Override
            public void keyPressed(KeyEvent e) {
                //Speichern STRG + S
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
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
            /*
            Jetzt die Tastenkombinationen um die Einzelnen Menüpunkte aufzurufen
             */
                //ALT + D um Menüpunkt Datei zu öffnen
                if(e.isAltDown()&& e.getKeyCode() ==KeyEvent.VK_D){
                    datei.doClick();
                }
                //ALT + F um Menüpunkt Formen zu öffnen
                if(e.isAltDown()&& e.getKeyCode() ==KeyEvent.VK_F){
                    formen.doClick();
                }
                //ALT + B um Menüpunkt Bedienungshilfe zu öffnen
                if(e.isAltDown()&& e.getKeyCode() ==KeyEvent.VK_B) {
                    Bedienungshilfe.doClick();
                }
                //ALT + H um Menüpunkt Hintergrund zu öffnen
                if(e.isAltDown()&& e.getKeyCode() ==KeyEvent.VK_H){
                    hintergrund.doClick();
                }
                //ALT + E um Menüpunkt Radierer zu öffnen
                if(e.isAltDown()&& e.getKeyCode() ==KeyEvent.VK_E){
                    radierer.doClick();
                }
                //ALT + U um Menüpunkt Fülleimer zu öffnen
                if(e.isAltDown()&& e.getKeyCode() ==KeyEvent.VK_U) {
                    ausfuellen.doClick();
                }
                //ALT + S um Menüpunkt Stift zu öffnen
                if(e.isAltDown()&& e.getKeyCode() ==KeyEvent.VK_S){
                    stift.doClick();
                }
                //ALT + T für Menüpunkt Text zu öffnen
                if(e.isAltDown()&& e.getKeyCode() ==KeyEvent.VK_T){
                    text.doClick();
                }
            }
        };
    }
    /*
    Diese Methode legt zu erst einen Speicherort fest und wandelt dann die Zeichenfläche in ein BufferedImage, welches dann
    als JPG Datei gespeichert wird
     */
    public void SpeichernUnter() {
        JFileChooser filter = new JFileChooser();
        filter.setFileFilter(new FileNameExtensionFilter("JPG (*.jpg)", "jpg")); //ES werden nur JPG DAteien angezeigt
        if (filter.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { //Wenn Nutzer speichern wirklich will und nicht abbricht
            File datei = filter.getSelectedFile(); //Die Datei die, der Benutzer eingegeben hat
            if (!datei.getName().toLowerCase().endsWith(".jpg"))
                datei = new File(datei.getAbsoluteFile() + ".jpg"); //Es wird immer eine Datei mit Endung .jpg erstellt
            BufferedImage Bild = zeichenflaeche.wandleBildinBufferdImage();
            try {
                ImageIO.write(Bild, "jpg", datei); //Datei wird erzeugt
                aktuelleDatei = datei; //Damit wird gezeigt es gibt einen aktuellen Speicherort
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "es gab einen Fehler beim Speichern!"); //Wenn Schreibrechte fehlen
            }
        }
    }
    /*
    Diese Methode speichert die Änderungen speichert
    Wenn es keinen Speicherort gibt ruft sie SpeicherUnter() auf
     */
    public void zwischenspeichern() {
        if (aktuelleDatei == null) { //Abfrage ob schonmal Speicherortfestgelegt worden ist
            SpeichernUnter();
            return;
        }
        BufferedImage Bild = zeichenflaeche.wandleBildinBufferdImage();
        try {
            ImageIO.write(Bild, "jpg", aktuelleDatei); //Alte JPG Datei wird überschrieben
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "es gab einen Fehler beim Speichern der Datei!");
        }
    }
    /*
    Diese Methode lädt ausgewählte Bilder
     */
    public void ladeBild() {
        JFileChooser sucher = new JFileChooser();
        sucher.setFileFilter(new FileNameExtensionFilter("JPG (*.jpg)", "jpg"));
        if (sucher.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { //Wenn der Nutzer wirklich laden will und nicht zwischendurch abbricht
            try {
                BufferedImage zuladenesBild = ImageIO.read(sucher.getSelectedFile());//Nutzer sucht Bild aus welches geladen werden soll
                if(zuladenesBild == null){ //Abfrage ob richtiges Datei Format Ausgewählt worden ist
                    JOptionPane.showMessageDialog(this,"Die Datei ist kein gültiges JPG-Bild!");
                    return;
                }
                zeichenflaeche.reset();
                zeichenflaeche.setBild(zuladenesBild); //Auf die leere Zeichenfläche wird das Bild gesetzt
                zeichenflaeche.repaint();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Es gab einen Fehler beim Laden des Bildes!" + e.getMessage());
            }
        }
    }

}