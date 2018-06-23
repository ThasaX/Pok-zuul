import java.util.HashMap;
import java.util.Set;

public class Spieler
{
    private int maxGewicht;
    private Raum aktuellerRaum;
    private String name;
    private int momentanesGewicht;
    private HashMap<String, Gegenstand> inventar;
    private Beamer beamer;
    private Pokemon pokemon;

    public Spieler(String name, Raum aktuellerRaum, int maxGewicht){
        this.maxGewicht = maxGewicht;
        this.aktuellerRaum = aktuellerRaum;
        this.name = name;
        this.momentanesGewicht = 0;
        this.inventar = new HashMap<String, Gegenstand>();
    }

    public Spieler(String name, Raum aktuellerRaum, int maxGewicht, HashMap startItems){
        this.maxGewicht = maxGewicht;
        this.aktuellerRaum = aktuellerRaum;
        this.name = name;
        this.momentanesGewicht = 0;
        this.inventar = startItems;
    }

    public Spieler(String name){
        this.maxGewicht = 500;
        this.aktuellerRaum = null;
        this.name = name;
        this.momentanesGewicht = 0;
        this.inventar = new HashMap<String, Gegenstand>();
    }

    public Pokemon getPokemon(){
        return pokemon;
    }
    
    public int gibMaxgewicht(){
        return maxGewicht;
    }

    public Raum gibAktuellerRaum(){
        return aktuellerRaum;
    }

    public String gibName(){
        return name;
    }

    public int gibMomentanesGewicht(){
        return momentanesGewicht;
    }

    public Gegenstand gibGegenstand(String key){
        return inventar.get(key);
    }

    public Beamer getBeamer(){
        return beamer;
    }

    public void setzeaktuellerRaum(Raum raum){
        aktuellerRaum = raum;
    }

    public void setzeName(String name){
        this.name = name;
    }

    public void setzeMomentanesGewicht(int newGewicht){
        momentanesGewicht = newGewicht;
    }

    public void addItem(Gegenstand g){
        if(momentanesGewicht+g.gibGewicht() <= maxGewicht){
            inventar.put(g.gibName(),g);
            momentanesGewicht+= g.gibGewicht();
        }
        else{
            System.out.println("Der Gegenstand ist zu schwer!");
        }
    }

    public void removeItem(Gegenstand g){
        inventar.remove(g.gibName());
        momentanesGewicht -= g.gibGewicht();
    }

    public Set<String> gebeSchluessel(){
        return inventar.keySet();
    }

    public void setzeMaxGewicht(int neuesMaxGewicht){
        this.maxGewicht=neuesMaxGewicht;
    }

    public void setBeamer(Beamer beamer){
        this.beamer = beamer;
    }
    
    public void setPokemon(Pokemon pokemon){
        this.pokemon = pokemon;
    }
}