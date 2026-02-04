package org.example;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.*;

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

    public GUI(){
        super("Graphic Editor");
        setSize(500,700);
        zeichenflaeche = new Zeichenflaeche();

        MeinListener listener = new MeinListener();
        zeichenflaeche.addMouseListener(listener);
        


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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
    class MeinListener implements ActionListener, MouseListener, MouseMotionListener {



        int StartxKoordinate = 0;
        int StartyKoordinate = 0;
        int EndXKoordinate = 0;
        int EndYKoordinate = 0;
        @Override
        public void actionPerformed(ActionEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(Modus.equals("Gerade")){
                StartxKoordinate = e.getX();
                StartyKoordinate = e.getY();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(Modus.equals("Gerade")){
                EndXKoordinate = e.getX();
                EndYKoordinate = e.getY();
                zeichenflaeche.zeichneLinie(new Linie(StartxKoordinate,StartyKoordinate,EndXKoordinate,EndYKoordinate));
                repaint();
            }
            if(Modus.equals("Frei")){

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(Modus.equals("Frei")){

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

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

}
