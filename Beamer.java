
public class Beamer{
    private Raum selectedRoom;
    
    public Beamer(){
        this.selectedRoom = null;
    }
    
    public Raum getSelectedRoom(){
        return this.selectedRoom;
    }
    
    public void loadRoom(Raum aktuellerRaum){
        this.selectedRoom = aktuellerRaum;
    }
}