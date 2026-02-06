package org.example;

import java.util.Stack;

public class Zustandsspeicher {
    private Stack<Aktion> UndoStack = new Stack<>();//Stack ermöglicht Last in Firdt Out Prinzip
    private Stack<Aktion> RedoStack = new Stack<>();
    private Aktion letzteAktion,letztezurueckgemachteAktion;

    public void fuehreaus(Runnable fuegehinzu,Runnable macherueckgaengig){
        Aktion aktion = new Aktion(fuegehinzu,macherueckgaengig);
        aktion.zeichne.run(); //Jetzt zeichnet der Nutzer mit der Methode
        UndoStack.push(aktion);
        RedoStack.clear(); //hiermit wird nach einer Änderung es nicht möglkich gemacht Redo doch einmal zu benutzen
    }
    public void undo(){
        if(!UndoStack.isEmpty()){
            letzteAktion = UndoStack.pop(); //erstes Objekt des Stacks wird genommen
            letzteAktion.rueckgaengingmachen.run(); //damit wird die rückgängig machen aktion ausgeführt
            RedoStack.push(letzteAktion); //Damit kann man die Zeichnung wieder herstellen mit Redo
        }
    }
    public void redo(){
        if(!RedoStack.isEmpty()){
            letztezurueckgemachteAktion = RedoStack.pop();
            letztezurueckgemachteAktion.zeichne.run(); //die zuletzt rückgängig gemachte zeichnung wird wieder gezeichnet
            UndoStack.push(letztezurueckgemachteAktion);
        }
    }
}
