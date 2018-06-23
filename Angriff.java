
public class Angriff{
    public int damage;
    public String name;
    public String beschreibung;

    public Angriff(int damage, String beschreibung, String name){
        this.damage = damage;
        this.beschreibung = beschreibung;
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public String getBeschreibung(){
        return beschreibung;
    }

    public int getDamage(){
        return damage;
    }

    public void setDamage(int damage){
        this.damage = damage;
    }
    
    public void setBeschreibung(String beschreibung){
        this.beschreibung = beschreibung;
    }
    
    public void setName(String name){
        this.name = name;
    }
}