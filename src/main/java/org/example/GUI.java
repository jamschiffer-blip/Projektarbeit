package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.*;

public class GUI extends JFrame {

    private JButton Undo,Redo,Speichern;
    private JPanel leiste;
    private JToolBar symbolleiste;
    private JMenuBar menueleiste;
    private JMenu datei;
    private JMenuItem Speichernmenueleiste , Laden, NeueDatei ;


    public GUI(){
        super("Graphic Editor");
        setSize(500,700);

        MeinListener listener = new MeinListener();

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


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
    class MeinListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

}
