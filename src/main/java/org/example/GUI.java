package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

public class GUI extends JFrame {


    public GUI(String text){
        super(text);

        setLayout(new GridLayout());

        menu();
        add(symbolleiste());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
    class MeinListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    //Erstellung der Menübar mithilfe dieser Methode
    // das ist vom seminar
    private void menu(){
        JMenuBar menue = new JMenuBar();
        JMenu dateiMenue = new JMenu("Datei");

        MeinListener listener = new MeinListener();

        JMenuItem dateiNeu = new JMenuItem();
        dateiNeu.setText("Neu");
        dateiNeu.setIcon(new ImageIcon("icons/new24.gif"));
        dateiNeu.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        dateiNeu.setActionCommand("neu");
        dateiNeu.addActionListener(listener);
        dateiMenue.add(dateiNeu);



        JMenuItem dateiBeenden = new JMenuItem();
        dateiBeenden.setText("Beenden");
        dateiBeenden.setActionCommand("beenden");
        dateiBeenden.addActionListener(listener);
        dateiMenue.add(dateiBeenden);

        menue.add(dateiMenue);
        this.setJMenuBar(menue);
    }
    //Erstellung der Symbolleiste
    private JToolBar symbolleiste() {
        JToolBar leiste = new JToolBar();
        MeinListener listener = new MeinListener();

        JButton dateiNeuButton = new JButton();
        dateiNeuButton.setActionCommand("neu");
        dateiNeuButton.setIcon(new ImageIcon("icons/new24.gif"));
        dateiNeuButton.setToolTipText("Erstellt ein neues Dokument");

        dateiNeuButton.addActionListener(listener);

        leiste.add(dateiNeuButton);

        return (leiste);
    }
}
