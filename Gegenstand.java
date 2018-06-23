
public class Gegenstand
{
    private int gewicht;
    private String beschreibung;
    private String name;

    public Gegenstand(int gewicht, String beschreibung, String name)
    {
        this.gewicht = gewicht;
        this.beschreibung = beschreibung;
        this.name = name;
    }

    public int gibGewicht(){
        return this.gewicht; 
    }

    public String gibName(){ 
        return this.name; 
    }

    public String gibBeschreibung(){ 
        return this.beschreibung; 
    }
}