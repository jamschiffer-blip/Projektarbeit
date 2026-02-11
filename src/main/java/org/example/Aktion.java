package org.example;
/*
Durch diese Klasse wird gespeichert wie eine Aktion ausgeführt wird und rückgängig gemacht wird
 */
public class Aktion {
     Runnable zeichne;           //Typ Runnable damit Code als Objekt gespeichert wird und man diesen für den jeweiligen Shape umschreiben kann
     Runnable rueckgaengingmachen;

    public Aktion(Runnable zeichne,Runnable rueckgaengingmachen){ //Aktion besteht aus dem Paar Ausführen und rückgängig machen
        this.zeichne = zeichne; //Wird auch für Redo verwendet
        this.rueckgaengingmachen = rueckgaengingmachen;
    }

}
