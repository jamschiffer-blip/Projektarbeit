package org.example;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame {

    private JButton Undo,Redo,Speichern;
    private JPanel leiste;
    private JToolBar symbolleiste;
    private JMenuBar menueleiste;
    private JMenu datei;
    private JMenuItem Speichernmenueleiste , Laden, NeueDatei ;
    private boolean mausgedrueckt = false;
    private String Modus = "Frei";
    public Zeichenflaeche zeichenflaeche;
    private Color aktuelleFarbe = Color.YELLOW;
    private float aktuelleDicke = 2.0f;
    private final int Zeichenflaeche_HOEHE = 1200;
    private final int Zeichenflaeche_BREITE = 1000;

    public GUI(){
        super("Graphic Editor");
        setSize(1500,2000);
        zeichenflaeche = new Zeichenflaeche();
        zeichenflaeche.setPreferredSize(new Dimension(1200,1000));

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

        symbolleiste.add(Undo);
        symbolleiste.add(Redo);
        symbolleiste.add(Speichern);
        symbolleiste.setLayout(new FlowLayout(FlowLayout.LEFT));

        leiste = new JPanel();
        leiste.setLayout(new BoxLayout(leiste,BoxLayout.Y_AXIS));
        leiste.add(symbolleiste,BorderLayout.WEST); //hiermit bleibt die leiste immer links in der ecke,auch wenn das fenster vergroessert oder verkleinert wird

        menueleiste = new JMenuBar();
        datei = new JMenu("Datei");
        Speichernmenueleiste = new JMenuItem("Speichern");
        Laden = new JMenuItem("Laden");
        NeueDatei = new JMenuItem("Neue Datei");
        Laden.setActionCommand("laden");
        Speichernmenueleiste.setActionCommand("speichern");
        NeueDatei.setActionCommand("neue datei");

        datei.add(Speichernmenueleiste);
        datei.add(Laden);
        datei.add(NeueDatei);

        menueleiste.setLayout(new FlowLayout(FlowLayout.LEFT));
        menueleiste.add(datei);

        leiste.add(menueleiste);



        add(leiste,BorderLayout.NORTH);
        add(zeichenflaeche,BorderLayout.CENTER);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
    class MeinListener implements ActionListener, MouseListener, MouseMotionListener {


        int counter = 0;
        int StartxKoordinate ;
        int StartyKoordinate ;
        int EndXKoordinate ;
        int EndYKoordinate;
        int counterPolygon = 0;
        int[] xKoordinatenPolygon = new int [100]; //Das Polygon darf maximal 100 Punkte haben
        int[] yKoordinatenPolygon = new int [100];
        @Override
        public void actionPerformed(ActionEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            double distanz;

            if(Modus.equals("Frei")){
                StartxKoordinate = e.getX();
                StartyKoordinate = e.getY();
            }
            if(Modus.equals("Polygon")){

                if(counterPolygon<100){
                    xKoordinatenPolygon[counterPolygon] = e.getX();
                    yKoordinatenPolygon[counterPolygon] = e.getY();
                    counterPolygon++;
                }
                if(counterPolygon>=3&&e.getClickCount()==2) //Clickcount==2 da man dann mit einem Doppelklick das Polygon abschliessen kann
                {
                    int[] xKoordinaten = Arrays.copyOf(xKoordinatenPolygon,counterPolygon);
                    int[] yKoordinaten = Arrays.copyOf(yKoordinatenPolygon,counterPolygon);
                    Polygon polygon = new Polygon(xKoordinaten,yKoordinaten);
                    polygon.setFarbe(aktuelleFarbe);
                    polygon.setDicke(aktuelleDicke);
                    zeichenflaeche.zeichnePolygon(polygon);//nur das benutzte Array wird verwendetet
                    counterPolygon = 0; //für nächstes Polygon zurücksetzen
                }

            }
            if(Modus.equals("Fuellen")&&e.getClickCount()==2){
                //hier kommt der Füllmodus hin
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(Modus.equals("Frei")){
                StartxKoordinate = e.getX();
                StartyKoordinate = e.getY();
            }

            if(Modus.equals("Gerade")){
                switch(counter){
                    case 0:
                        counter++;
                        StartyKoordinate = e.getY();
                        StartxKoordinate = e.getX();
                        break;


                    case 1:
                        counter = 0;
                        EndXKoordinate = e.getX();
                        EndYKoordinate = e.getY();
                        Linie linie = new Linie(StartxKoordinate,StartyKoordinate,EndXKoordinate,EndYKoordinate);
                        linie.setFarbe(aktuelleFarbe);
                        linie.setDicke(aktuelleDicke);
                        zeichenflaeche.zeichneLinie(linie);
                        break;



                }
            }
            if(Modus.equals("Ellipse")||Modus.equals("Kreis")||Modus.equals("Rechteck")){
                StartxKoordinate = e.getX();
                StartyKoordinate = e.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(Modus.equals("Ellipse")){
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();

                int xKoordinate = Math.min(StartxKoordinate,EndXKoordinate); //Kleinere Koordinaten nehmen da man in der "linken Oberen" Ecke hier die Koordinaten für die Ellipsen sind
                int yKoordinate = Math.min(StartyKoordinate,EndYKoordinate);
                int breite = Math.abs(EndXKoordinate-StartxKoordinate); //Abstand wird hier berechnet dieser ist durch abs immer positiv
                int hoehe = Math.abs(EndYKoordinate-StartyKoordinate); // hier dasselbe nur für die hoehe statt breite
                Ellipse ellipse = new Ellipse(xKoordinate,yKoordinate,breite,hoehe);
                ellipse.setFarbe(aktuelleFarbe);
                ellipse.setDicke(aktuelleDicke);
                zeichenflaeche.zeichneEllipse(ellipse);
            }
            if(Modus.equals("Kreis")){
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();
                double radiusinDouble = Math.sqrt((StartxKoordinate-EndXKoordinate)*(StartxKoordinate-EndXKoordinate)
                        +(StartyKoordinate-EndYKoordinate)*(StartyKoordinate-EndYKoordinate)) ; // Berechnung radius mithilfe Pythagoras
                int radius = (int) radiusinDouble;
                int xKoordinate = StartxKoordinate - radius ; // Berechnung der Koordinaten des Mittelpunktes
                int yKoordinate = StartyKoordinate - radius;
                Kreis kreis = new Kreis(radius*2,xKoordinate,yKoordinate);
                kreis.setFarbe(aktuelleFarbe);
                kreis.setDicke(aktuelleDicke);
                zeichenflaeche.zeichneKreis(kreis);
            }
            if(Modus.equals("Rechteck")){
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();

                int xKoordinate = Math.min(StartxKoordinate,EndXKoordinate); //Kleinere Koordinaten nehmen da man in der "linken Oberen" Ecke hier die Koordinaten für die Ellipsen sind
                int yKoordinate = Math.min(StartyKoordinate,EndYKoordinate);
                int breite = Math.abs(EndXKoordinate-StartxKoordinate); //Abstand wird hier berechnet dieser ist durch abs immer positiv
                int hoehe = Math.abs(EndYKoordinate-StartyKoordinate); // hier dasselbe nur für die hoehe statt breite
                Rechteck rechteck = new Rechteck(xKoordinate,yKoordinate,breite,hoehe);
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
            if(Modus.equals("Frei")){
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();
                Linie linie = new Linie(StartxKoordinate,StartyKoordinate,EndXKoordinate,EndYKoordinate);
                linie.setFarbe(aktuelleFarbe);
                linie.setDicke(aktuelleDicke);
                zeichenflaeche.zeichneLinie(linie);

                StartxKoordinate = EndXKoordinate; //Damit werden ganz viele kleine Linien verbunden zu einer großen
                StartyKoordinate = EndYKoordinate;
            }
            if(Modus.equals("Radieren")){

            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }
    public void speichern(){
        JFileChooser filter = new JFileChooser();
        filter.setFileFilter(new FileNameExtensionFilter("jpg")); //es werden nur jpg dateien angezeigt
        if(filter.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){ //Wenn Nutzer speichern will
           File datei = filter.getSelectedFile();
           if(!datei.getName().toLowerCase().endsWith(".jpg"))
               datei = new File(datei.getAbsoluteFile() + ".jpg"); //hiermit wird geprüft ob der Nutzer schon jpg am ende hat wenn nicht wird es angehangen
            BufferedImage Bild = zeichenflaeche.getBild();
            ImageIO.write(Bild,"jpg",datei); //damit wird datei erzeugt
        }
    }
}
