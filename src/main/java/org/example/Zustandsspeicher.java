package org.example;

import java.util.Stack;

public class Zustandsspeicher {
    /*
    Es werden zwei Stacks erstellt um einerseits alle Änderungen zu speichern und anderseits alle rückängiggemachten Änderungen
    zu speichern. Der Stack unterstützt das Last in First Out Prinzip, also kann die letzte Aktion rückgängig gemacht werden
     */
    private Stack<Aktion> UndoStack = new Stack<>();
    private Stack<Aktion> RedoStack = new Stack<>();
    private Aktion letzteAktion,letztezurueckgemachteAktion;

    /*
    Diese Methode wird aufgerufen wenn der Benutzer etwas neues zeichnet
     */
    public void fuehreaus(Runnable fuegehinzu,Runnable macherueckgaengig){
        //Neue Aktion wird erstellt und direkt ausgeführt mit run()
        Aktion aktion = new Aktion(fuegehinzu,macherueckgaengig);
        aktion.zeichne.run();

        UndoStack.push(aktion); //Die neue Aktion wird dem Undo Stapel hinzugefügt
        RedoStack.clear(); //Hiermit wird nach einer Änderung es nicht möglich gemacht REDO zu benutzen
    }
    /*
    Diese Methode macht die Änderung rückgänig, wenn der Nutzer auf Undo klickt
     */
    public void undo(){
        if(!UndoStack.isEmpty()){
            letzteAktion = UndoStack.pop(); //Letzte Aktion ausgeführte Aktion wird ausgeführt
            letzteAktion.rueckgaengingmachen.run(); //Damit wird der jeweilige Code zum rückgängigmachen ausgeführt
            RedoStack.push(letzteAktion); //Damit kann man die Zeichnung wieder herstellen mit REDO
        }
    }
    /*
    Diese Methode bringt die letzte rückgäniggemachte Änderunh zurück, wenn der Nutzer auf Redo klickt
     */
    public void redo(){
        if(!RedoStack.isEmpty()){
            letztezurueckgemachteAktion = RedoStack.pop(); //letzte rückgängig gemachte Änderung wird genommen
            letztezurueckgemachteAktion.zeichne.run(); //Damit wird der jeweilige Code zum Widerrufen der rückgängigemachten Änderung aufgerufen
            UndoStack.push(letztezurueckgemachteAktion); //Die zurückgeholte  Aktion kann wieder rückgängig gemacht werden
        }
    }
}
