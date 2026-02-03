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


    public GUI(){
        super("Graphic Editor");
        setSize(500,700);


        setLayout(new BorderLayout());

        JToolBar symbolleiste = new JToolBar();
        symbolleiste.setFloatable(false);

        JButton Undo = new JButton("Undo");
        //hier image statt text
        JButton Redo = new JButton("Redo");
        JButton Speichern = new JButton("Speichern");

        Undo.setActionCommand("undo");
        Redo.setActionCommand("redo");
        Speichern.setActionCommand("speichern");

        symbolleiste.add(Undo);
        symbolleiste.add(Redo);
        symbolleiste.add(Speichern);

        add(symbolleiste);


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
