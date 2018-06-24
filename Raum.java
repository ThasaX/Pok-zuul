import java.util.HashMap;
import java.util.Set;
import java.util.Random;
import java.util.Iterator;

class Raum 
{
    private String beschreibung;
    private HashMap<String, Raum> ausgaenge;
    private HashMap<String, Gegenstand> gegenstand;
    private boolean open;
    private boolean hasEncounter;
    private Person person;
    private Pokemon[] possiblePokemon;

    /**
     * Erzeuge einen Raum mit einer Beschreibung. Ein Raum
     * hat anfangs keine Ausgaenge.
     * @param beschreibung enthaelt eine Beschreibung in der Form
     *        "in einer Kueche" oder "auf einem Sportplatz".
     */
    public Raum(String beschreibung, boolean isOpen, boolean hasEncounter) 
    {
        this.beschreibung = beschreibung;
        this.ausgaenge = new HashMap<String, Raum>();
        this.gegenstand = new HashMap<String, Gegenstand>();
        this.open = isOpen;
        this.hasEncounter = hasEncounter;
        if(hasEncounter){
            possiblePokemon = new Pokemon[2];
            Pokemon rattfratz = new Pokemon("Rattfratz", 20, 9, 8);
            rattfratz.addAngriff("Tackle",new Angriff(40, "Tackle - Schaden: 40, kein Zusatzeffekt", "Tackle"));
            rattfratz.addAngriff("Silberblick",new Angriff(0, "Tackle - Schaden: 0, Senkt gegnerische Verteidigung", "Silberblick"));
            possiblePokemon[0] = rattfratz;
            Pokemon taubsi = new Pokemon("Taubsi", 20, 9, 8);
            taubsi.addAngriff("Windstoss", new Angriff(40, "Windstoss - Schaden: 40, kein Zusatzeffekt", "Windstoss"));
            possiblePokemon[1] = taubsi;
        }
    }

    public Pokemon getRandomEncounter(){
        Random r = new Random();
        return possiblePokemon[r.nextInt(possiblePokemon.length)];
    }

    public void setzeAusgaenge(String richtung, Raum nachbar) 
    {
        ausgaenge.put(richtung, nachbar);
    }

    /**
     * @return Die Beschreibung dieses Raums.
     */
    public String gibBeschreibung()
    {
        return beschreibung;
    }

    public Raum gibAusgang(String richtung){
        return ausgaenge.get(richtung);
    }

    public String gibAusgaengeAlsString(){
        String erg = "Ausgaenge:";
        Set<String> keys = ausgaenge.keySet();
        for(String ausgang : keys){
            erg = erg +" " + ausgang;
        }
        return erg;
    }

    public String gibLangeBeschreibung(){
        return "Sie sind "+ beschreibung +  ".\n" + gibAusgaengeAlsString();
    }

    public Gegenstand gibGegenstand(String wunsch){
        return gegenstand.get(wunsch);
    }

    public boolean isOpen(){
        return open;
    }

    public void setOpen(boolean open){
        this.open = open;
    }

    public void gegenstandHinzufuegen(Gegenstand gegenstand){
        this.gegenstand.put(gegenstand.gibName(), gegenstand);
    }

    public void entferneGegenstand(Gegenstand gegenstand){
        this.gegenstand.remove(gegenstand.gibName());
    }

    public void printGegenstaende(){
        System.out.print("Gegenstaende: ");
        String erg = "";
        Set<String> keys = gegenstand.keySet();
        for(String temp : keys){
            erg += "\n" + temp;
        }

        if (erg.equals("")){
            erg = "keine";
        }

        System.out.println(erg);
    }

    public void setPerson(Person person){
        this.person = person;
    }

    public Person getPerson(){
        return person;
    }

    public boolean getHasEncounter(){
        return hasEncounter;
    }

    public void setHasEncounter(boolean hasEncounter){
        this.hasEncounter = hasEncounter;
    }

    public void removeAllItems(){
        gegenstand.clear();
    }
}
