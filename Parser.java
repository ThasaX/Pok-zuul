import java.util.Scanner;

/**
 * Dieser Parser liest Benutzereingaben und wandelt sie in
 * Befehle fuer das Adventure-Game um. Bei jedem Aufruf
 * liest er eine Zeile von der Konsole und versucht, diese als
 * einen Befehl aus bis zu zwei Woertern zu interpretieren. Er
 * liefert den Befehl als ein Objekt der Klasse Befehl zurueck.
 * 
 * Der Parser verfuegt ueber einen Satz an bekannten Befehlen. Er
 * vergleicht die Eingabe mit diesen Befehlen. Wenn die Eingabe
 * keinen bekannten Befehl enthaelt, dann liefert der Parser ein als 
 * unbekannter Befehl gekennzeichnetes Objekt zurueck.
 * 
 * @author  Michael Koelling und David J. Barnes
 * @version 2008.03.30
 */
class Parser 
{
    private Befehlswoerter befehle;  // haelt die gueltigen Befehlswoerter
    private Scanner leser;         // Lieferant fuer eingegebene Befehle

    /**
     * Erzeuge einen Parser, der Befehle von der Konsole einliest.
     */
    public Parser() 
    {
        befehle = new Befehlswoerter();
        leser = new Scanner(System.in);
    }

    /**
     * @return Den naechsten Befehl des Benutzers.
     */
    public Befehl liefereBefehl() 
    {
        String eingabezeile;   // fuer die gesamte Eingabezeile
        Befehlswort wort1 = null;
        String wort2 = null;

        System.out.print("> ");     // Eingabeaufforderung

        eingabezeile = leser.nextLine();

        // Finde bis zu zwei Woerter in der Zeile
        Scanner zerleger = new Scanner(eingabezeile);
        if(zerleger.hasNext()) {
            wort1 = befehle.gibBefehlswort(zerleger.next().toUpperCase());
            if (zerleger.hasNext()) {
                wort2 = zerleger.next();    // zweites Wort lesen
                // Hinweis: Wir ignorieren den Rest der Eingabezeile.
            }
        }

        // Jetzt pruefen, ob der Befehl bekannt ist. Wenn ja, erzeugen
        // wir das passende Befehl-Objekt. Wenn nicht, erzeugen wir
        // einen unbekannten Befehl mit 'null'.

        return new Befehl(wort1, wort2);
    }
    
    public void zeigeBefehle()
    {
        befehle.alleAusgeben();
    }
}
