package org.example;

public class Aktion {
    Runnable zeichne;           //hiermit wird immer ein Paar für die Zeichnung selbst und wie man es wieder rückgängigmachen kann
    Runnable rueckgaengingmachen;

    public Aktion(Runnable zeichne,Runnable rueckgaengingmachen){
        this.zeichne = zeichne; //wird auch für redo verwendet
        this.rueckgaengingmachen = rueckgaengingmachen;
    }

}
