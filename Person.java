import java.util.HashMap;

public class Person{
    private String name;
    private String text;
    private HashMap<Gegenstand, String> itemReaction;
    private Pokemon pokemon;
    
    public Person(String name, String text, Pokemon pokemon){
        this.name = name;
        this.text = text;
        this.itemReaction = new HashMap<Gegenstand, String>();
        this.pokemon = pokemon;
    }
    
    public void setPokemon(Pokemon pokemon){
        this.pokemon = pokemon;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public void addItemReaction(Gegenstand item, String reaction){
        itemReaction.put(item, reaction);
    }
    
    public void removeItemReaction(Gegenstand item){
        itemReaction.remove(item);
    }
    
    public String getName(){
        return name;
    }
    
    public String getText(){
        return text;
    }
    
    public String getReaction(Gegenstand item){
        return itemReaction.get(item);
    }
    
    public Pokemon getPokemon(){
        return pokemon;
    }
}