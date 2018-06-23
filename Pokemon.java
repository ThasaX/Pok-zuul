import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Random;

public class Pokemon{
    private String name;
    private int hPMax;
    private int hPCurrent;
    private int atk;
    private int def;
    private HashMap<String, Angriff> angriffe;

    public Pokemon(String name, int hPmax, int atk, int def){
        this.name = name;
        this.hPMax = hPmax;
        this.hPCurrent = hPmax;
        this.atk = atk;
        this.def = def;
        angriffe = new HashMap<String, Angriff>();
    }

    public int getHPMax(){
        return hPMax;
    }

    public int getHPCurrent(){
        return hPCurrent;
    }

    public int getAtk(){
        return atk;
    }

    public int getDef(){
        return def;
    }
    
    public Angriff getAngriff(String angriff){
        return angriffe.get(angriff);
    }

    public String getName(){
        return name;
    }

    public void setHPMax(int newHPMax){
        hPMax = newHPMax;
    }

    public void setHPCurrent(int newHPCurrent){
        hPCurrent = newHPCurrent;
    }

    public void setAtk(int newAtk){
        atk = newAtk;
    }

    public void setDef(int newDef){
        def = newDef;
    }

    public void addAngriff(String key, Angriff value){
        angriffe.put(key, value);
    }

    public void setName(String name){
        this.name = name;
    }

    public void printAngriffe(){
        Set<String> keys = angriffe.keySet();
        Iterator<String> it = keys.iterator();

        while(it.hasNext()){
            System.out.println(angriffe.get(it.next()).beschreibung);
        }
    }
    
    public Angriff getRandomAngriff(){
        Set<String> keys = angriffe.keySet();
        Iterator<String> it = keys.iterator();
        Random r = new Random();
        int n = r.nextInt(angriffe.size());
        Angriff erg = null;
        
        
        for(int i = 0; i < n; i++){
            erg = angriffe.get(it.next());
        }
        
        return erg;
    }
}