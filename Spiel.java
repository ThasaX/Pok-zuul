import java.util.Stack;
import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

class Spiel 
{
    private Parser parser;
    private Raum aktuellerRaum;
    private Stack<Raum> letzterRaum;
    private Spieler player;
    private HashMap<String, Raum> map;
    private String nameRival;
    private Random random;

    public static void main(String[] args){
        Spiel s = new Spiel();
    }

    public Spiel() 
    {
        random = new Random();
        map = new HashMap<String, Raum>();
        raeumeAnlegen();
        letzterRaum = new Stack<Raum>();
        parser = new Parser();
        player = new Spieler("");
        player.setBeamer(new Beamer());
        player.setzeaktuellerRaum(aktuellerRaum);
        spielen();
    }

    /**
     * Erzeuge alle Raeume und verbinde ihre Ausgaenge miteinander.
     */
    private void raeumeAnlegen()
    {
        Raum alabastia, hausPlayer, hausRival, labor, see, wiese, routeSouth,
        routeNorth, routeEnd;

        // die Raeume erzeugen
        alabastia = new Raum("in mitten der kleinen Heimatstadt", true, false);
        hausPlayer = new Raum("in deinem zuhause", true, false);
        hausRival = new Raum("in dem Haus von " + nameRival, true, false);
        labor = new Raum("in dem Labor von Professor Eich", true, false);
        see = new Raum("an dem kleinen See von Alabastia", true, false);
        wiese = new Raum("auf einer kleinen Wiese zwischen Alabastia und Route 1 \n Du koenntest auf Pokémon treffen.", false, true);
        routeSouth = new Raum("auf dem suedlichem Teil von Route 1 \n Du koenntest auf Pokémon und Trainer treffen.", true, true);
        routeNorth = new Raum("auf dem noerdlichen Teil von Route 1 \n Du koenntest auf Pokémon und Trainer treffen.", true, true);
        routeEnd = new Raum("am Ende von Route 1 \n dein Rivale steht vor dir.", true, false);

        Person eich = new Person("Eich", "Nimm dir ein Pokemon und fordere " + nameRival + " heraus.\n du kriegst aber nur eines entscheide dich gut.", null);
        labor.setPerson(eich);
        Gegenstand glumanda = new Gegenstand(100, "Ein Pokemball in dem sich ein Glumanda befindet", "Glumanda");
        Gegenstand schiggy = new Gegenstand(100, "Ein Pokemball in dem sich ein Bisasam befindet", "Schiggy");
        Gegenstand bisasam = new Gegenstand(100, "Ein Pokemball in dem sich ein Schiggy befindet", "Bisasam");
        labor.gegenstandHinzufuegen(glumanda);
        labor.gegenstandHinzufuegen(schiggy);
        labor.gegenstandHinzufuegen(bisasam);

        // die Ausgaenge initialisieren
        alabastia.setzeAusgaenge("Zuhause", hausPlayer);
        alabastia.setzeAusgaenge("Nachbar", hausRival);
        alabastia.setzeAusgaenge("Labor", labor);
        alabastia.setzeAusgaenge("See", see);
        alabastia.setzeAusgaenge("Wiese", wiese);
        hausPlayer.setzeAusgaenge("Alabastia", alabastia);
        hausRival.setzeAusgaenge("Alabastia", alabastia);
        labor.setzeAusgaenge("Alabastia", alabastia);
        see.setzeAusgaenge("Alabstia", alabastia);
        wiese.setzeAusgaenge("Alabastia", alabastia);
        wiese.setzeAusgaenge("Route Sueden", routeSouth);
        routeSouth.setzeAusgaenge("Wiese", wiese);
        routeSouth.setzeAusgaenge("RouteNorden", routeNorth);
        routeNorth.setzeAusgaenge("RouteSueden", routeSouth);
        routeNorth.setzeAusgaenge("RouteEnde", routeEnd);
        routeEnd.setzeAusgaenge("RouteNord", routeNorth);

        hausPlayer.gegenstandHinzufuegen(new Gegenstand(100,"Ein Item, welches man nur am See nutzen kann","Angel"));
        hausPlayer.gegenstandHinzufuegen(new Gegenstand(100,"",""));

        map.put("alabastia",alabastia);
        map.put("hausplayer",hausPlayer);
        map.put("hausrival",hausRival);
        map.put("labor",labor);
        map.put("see", see);
        map.put("wiese", wiese);
        map.put("routeSouth", routeSouth);
        map.put("routeNorth", routeNorth);
        map.put("routeEnd", routeEnd);

        aktuellerRaum = hausPlayer;  // das Spiel startet draussen
    }

    /**
     * Die Hauptmethode zum Spielen. Laeuft bis zum Ende des Spiels
     * in einer Schleife.
     */
    public void spielen() 
    {            
        willkommenstextAusgeben();
        boolean done = false;

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader r = new BufferedReader(isr);
        while(!done){
            System.out.println("Wie soll dein Name sein?");
            try{
                String temp = r.readLine();
                if(temp != null){
                    done = true;
                    player.setzeName(temp);
                }
            } catch(Exception e){}
        }

        done = false;

        while(!done){
            System.out.println("Wie soll dein Rivale heißen?");
            try{
                String temp = r.readLine();
                if(temp != null){
                    done = true;
                    nameRival = temp;
                }
            } catch(Exception e){}
        }

        System.out.println();
        gebeRaumStatus();
        printRooms();
        System.out.print(" \n Moegliche Befehle: \n");
        Befehlswoerter temp = new Befehlswoerter();
        temp.alleAusgeben();

        boolean beendet = false;
        while (! beendet) {
            Befehl befehl = parser.liefereBefehl();
            beendet = verarbeiteBefehl(befehl);
        }
        System.out.println("Danke fuer dieses Spiel. Auf Wiedersehen.");
    }

    /**
     * Einen Begrueßungstext fuer den Spieler ausgeben.
     */
    private void willkommenstextAusgeben()
    {
        System.out.println();
        System.out.println("Willkommen in der Welt der Pokémon!");
        System.out.println("Du befindest dich in der Region: Kanto.");
        System.out.println("Du willst seit je her deinen Rivalen besiegen.");
        System.out.println("Er ist auf Route 1 und hat bereits sein Pokémon.");    
        System.out.println("Hohl dir deines im Labor und fordere ihn heraus.");    
        System.out.println("*Tippen sie 'help', wenn Sie Hilfe brauchen.*");
    }

    private void printRooms(){
        System.out.println(aktuellerRaum.gibAusgaengeAlsString());
    }

    /**
     * Verarbeite einen gegebenen Befehl (fuehre ihn aus).
     * @param befehl Der zu verarbeitende Befehl.
     * @return 'true', wenn der Befehl das Spiel beendet, 'false' sonst.
     */
    private boolean verarbeiteBefehl(Befehl befehl) 
    {
        boolean moechteBeenden=false;
        Befehlswort befehlswort = befehl.gibBefehlswort();

        if(befehl == null)
        {
            System.out.print("Diesen Befehl gibt es nicht");
        }
        else if(befehlswort == Befehlswort.HELP)
        {
            hilfstextAusgeben();
        }
        else if(befehlswort == Befehlswort.GO ||befehlswort == Befehlswort.BACK)
        {
            wechsleRaum(befehl);
        }
        else if(befehlswort == Befehlswort.LOOK)
        {
            umSehen();
        }
        else if(befehlswort == Befehlswort.QUIT)
        {
            moechteBeenden=beenden(befehl);
        }
        else if(befehlswort == Befehlswort.TAKE){
            takeDropItem(true, befehl.gibZweitesWort());
        }
        else if(befehlswort == Befehlswort.DROP){
            takeDropItem(false, befehl.gibZweitesWort());
        }
        else if(befehlswort == Befehlswort.STATUS){
            gebeStatus();
        }
        else if (befehlswort == Befehlswort.EAT)
        {
            Gegenstand temp = player.gibGegenstand(befehl.gibZweitesWort());
            if(temp == null){
                System.out.println("Du hast diesen Gegenstand nicht im Inventar!");
            }
            else{
                konsumiereItem(temp);
            }
        }
        else if(befehlswort == Befehlswort.LOAD){
            player.getBeamer().loadRoom(aktuellerRaum);
            System.out.println("Der Raum wurde geladen.");
        }
        else if(befehlswort == Befehlswort.BEAM){
            if(player.getBeamer().getSelectedRoom() != null){
                beam(player.getBeamer().getSelectedRoom());
                player.getBeamer().loadRoom(null);
            }
            else{
                System.out.println("Ihr Beamer hat keinen Raum ausgewaehlt.");
            }
        }
        else if(befehlswort == Befehlswort.TELEPORT){
            if(aktuellerRaum == map.get("teleporter")){
                Set<String> keys = map.keySet();
                Iterator<String> it = keys.iterator();
                Random r = new Random();
                int n = r.nextInt(map.size());
                Raum temp = null;
                for(int i = 0; i < n; i++){
                    temp = map.get(it.next());
                }
                beam(temp);
            }
            else{
                System.out.println("Es ist kein Teleporter in dem Raum. Meinen sie den ''Beamer''?");
            }
        }
        else if(befehlswort == Befehlswort.SPEAK){
            if(aktuellerRaum.getPerson() == null){
                System.out.println("Du fuehrst eine ausgebige Diskussion mit dir selbst. Du verlierst.");
            }
            else{
                String text = aktuellerRaum.getPerson().getText();
                String name = aktuellerRaum.getPerson().getName();
                int n = text.length();
                System.out.print(name + ": ");
                for(int i = 0; i < n; i++){
                    System.out.print(text.charAt(i));
                }
                System.out.println();
            }
        }
        return moechteBeenden;
    }

    // Implementierung der Benutzerbefehle:

    /**
     * Gib Hilfsinformationen aus.
     * Hier geben wir eine etwas alberne und unklare Beschreibung
     * aus, sowie eine Liste der Befehlswoerter.
     */
    private void hilfstextAusgeben() 
    {
        System.out.println("Sie haben sich verlaufen. Sie sind allein. \nSie irren auf dem Unigelaende herum. \n \n Ihnen stehen folgende Befehle zur Verfuegung:");
        parser.zeigeBefehle();
    }

    /**
     * Versuche, den Raum zu wechseln. Wenn es einen Ausgang gibt,
     * wechsele in den neuen Raum, ansonsten gib eine Fehlermeldung
     * aus.
     */
    private void wechsleRaum(Befehl befehl) 
    {

        if(befehl.gibBefehlswort() == Befehlswort.BACK){
            if(!letzterRaum.isEmpty()){
                aktuellerRaum = (Raum) letzterRaum.pop();
                player.setzeaktuellerRaum(aktuellerRaum);

                gebeRaumStatus();
            }
            else{
                System.out.println("Sie waren zuvor in keinem anderen Raum.");
            }
        }
        else if(befehl.hatZweitesWort()){

            String richtung = befehl.gibZweitesWort();
            Raum naechsterRaum = aktuellerRaum.gibAusgang(richtung);

            if (naechsterRaum == null) {
                System.out.println("Dort ist keine Tuer!");
            }
            else {
                if(naechsterRaum.isOpen() || (!naechsterRaum.isOpen() &&  player.getPokemon() != null)){
                    if(aktuellerRaum.getHasEncounter() && random.nextBoolean()){
                        if(fight(aktuellerRaum.getRandomEncounter())){
                            System.out.println("Du hast verloren. \n Du rennst nachhause und heilst dein Pokemon.");
                            naechsterRaum = map.get("zuhause");
                        }
                    }

                    letzterRaum.push(aktuellerRaum);
                    aktuellerRaum = naechsterRaum;
                    player.setzeaktuellerRaum(aktuellerRaum);
                    gebeRaumStatus();
                }        
                else{
                    System.out.println("Die Tuer scheint abgeschlossen");
                }                

            }
        }
        else{
            System.out.println("Wohin moechten Sie gehen?");
        }
    }

    /**
     * "quit" wurde eingegeben. ueberpruefe den Rest des Befehls,
     * ob das Spiel wirklich beendet werden soll.
     * @return 'true', wenn der Befehl das Spiel beendet, 'false' sonst.
     */
    private boolean beenden(Befehl befehl){
        if(befehl.hatZweitesWort()) {
            System.out.println("Was soll beendet werden?");
            return false;
        }
        else {
            return true;  // Das Spiel soll beendet werden.
        }
    }

    private void umSehen(){
        System.out.println(aktuellerRaum.gibLangeBeschreibung());
    }

    public void takeDropItem(boolean b, String gegenstand){
        if(b && aktuellerRaum.gibGegenstand(gegenstand) != null){
            Gegenstand temp = aktuellerRaum.gibGegenstand(gegenstand);
            aktuellerRaum.entferneGegenstand(temp);
            player.addItem(temp);

            if(gegenstand.toLowerCase().equals("glumanda")&&aktuellerRaum.gibGegenstand(gegenstand) != null){
                Pokemon glumanda = new Pokemon("Glumanda", 19, 10, 8);
                glumanda.addAngriff("kratzer",new Angriff(40, "Kratzer - Schaden: 40, Kein Zusatzeffekt", "Kratzer"));
                glumanda.addAngriff("silberblick" ,new Angriff(0, "Silberblick - Schaden: 0, Senkt gegnerische Verteidigung", "Silberblick"));
                player.setPokemon(glumanda);
            }
            else if(gegenstand.toLowerCase().equals("schiggy")&&aktuellerRaum.gibGegenstand(gegenstand) != null){
                Pokemon glumanda = new Pokemon("Schiggy", 22, 9, 11);
                glumanda.addAngriff("tackle",new Angriff(40, "Tackle - Schaden: 40, Kein Zusatzeffekt", "Kratzer"));
                glumanda.addAngriff("rutenschlag" ,new Angriff(0, "Rutenschlag - Schaden: 0, Senkt gegnerische Verrteidigung", "Silberblick"));
                player.setPokemon(glumanda);
            }
            else if(gegenstand.toLowerCase().equals("bisasam")&&aktuellerRaum.gibGegenstand(gegenstand) != null){
                Pokemon glumanda = new Pokemon("Bisasam", 22, 9, 9);
                glumanda.addAngriff("tackle",new Angriff(40, "Tackle - Schaden: 40, Kein Zusatzeffekt", "Kratzer"));
                glumanda.addAngriff("heuler" ,new Angriff(0, "Heuler - Schaden: 0, Senkt gegnerischen Angriff", "Heuler"));
                player.setPokemon(glumanda);
            }
            else{
                System.out.println("Der Gegenstand wurde aufgenommen.");
            }
        }
        else if( !b && player.gibGegenstand(gegenstand) != null ){
            Gegenstand temp = player.gibGegenstand(gegenstand);
            player.removeItem(temp);
            aktuellerRaum.gegenstandHinzufuegen(temp);
            System.out.println("Der Gegenstand wurde fallen gelassen.");
        }
        else{
            System.out.println("Gegenstand nicht gefunden");
        }
    }

    public void konsumiereItem(Gegenstand gegenstand){
        if (player.gibGegenstand("Muffin") == gegenstand){
            player.removeItem(gegenstand);
            player.setzeMaxGewicht(9999);
            System.out.println("Muffin wurde gegessen.");
        }
        else{
            System.out.println("Dieser Gegenstand kann nicht gegessen werden");
        }
    }

    public void gebeStatus(){
        Set<String> keys = player.gebeSchluessel();
        Iterator<String> it = keys.iterator();
        System.out.println("Gegenstaende:");
        while(it.hasNext()){
            Gegenstand g = player.gibGegenstand(it.next());
            System.out.println(g.gibName() + " Gewicht: " + g.gibGewicht());
        }
        System.out.println(player.gibMomentanesGewicht());
    }

    public void beam(Raum toBeam){
        if(aktuellerRaum == map.get("kerker")){
            System.out.println("Sie haben es aus dem Kerker geschafft! Glueckwunsch!");
        }
        player.setzeaktuellerRaum(toBeam);
        aktuellerRaum = toBeam;
        letzterRaum.clear();
        gebeRaumStatus();
    }

    public void gebeRaumStatus(){
        System.out.println("Du bist " + aktuellerRaum.gibBeschreibung());
        System.out.print("Person: ");
        if(aktuellerRaum.getPerson() == null){
            System.out.println("keine");
        }
        else{
            System.out.println(aktuellerRaum.getPerson().getName());
        }
        printRooms();
        aktuellerRaum.printGegenstaende();
        System.out.println();
    }

    public boolean fight(Pokemon enemy){
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(isr);

        boolean erg = true;
        boolean done = false;
        int atkPlayer = player.getPokemon().getAtk();
        int defPlayer = player.getPokemon().getDef();
        int atkEnemy = enemy.getAtk();
        int defEnemy = enemy.getDef();

        while(!done){
            printFightInformation(enemy, "Das Gegnerpokémon");
            printFightInformation(player.getPokemon(), "Dein Pokémon");

            try{
                System.out.println("Welche Attacke möchtest du einsetzen?");
                player.getPokemon().printAngriffe();
                Angriff temp = player.getPokemon().getAngriff(reader.readLine());
                while(temp == null){
                    System.out.println("Welche Attacke möchtest du einsetzen");
                    temp = player.getPokemon().getAngriff(reader.readLine());
                }
                Angriff gegnerAngriff = enemy.getRandomAngriff();

                if(temp.getDamage() == 0){
                    if(temp.getName().equals("Silberblick") 
                    || temp.getName().equals("Rutenschlag")){
                        defEnemy *= 0.75;
                    }
                    else if(temp.getName().equals("Heuler")){
                        atkEnemy *= 0.75;
                    }
                }
                else{
                    enemy.setHPCurrent(enemy.getHPCurrent()-atkPlayer*temp.getDamage()/defEnemy/10);
                }

                if(enemy.getHPCurrent() ==0){
                    done = true;
                    erg = false;
                }

                if(gegnerAngriff.getDamage() == 0){
                    if(gegnerAngriff.getName().equals("Silberblick") 
                    || gegnerAngriff.getName().equals("Rutenschlag")){
                        defPlayer *= 0.75;
                    }
                    else if(gegnerAngriff.getName().equals("Heuler")){
                        atkPlayer *= 0.75;
                    }
                }
                else{
                    player.getPokemon().setHPCurrent(player.getPokemon().getHPCurrent()-atkEnemy*gegnerAngriff.getDamage()/defPlayer/10);
                }

                if(player.getPokemon().getHPCurrent() ==0){
                    done = true;
                    erg = true;
                }
            }catch(Exception e){}
        }

        player.getPokemon().setHPCurrent(player.getPokemon().getHPMax());

        return erg;
    }

    public void printFightInformation(Pokemon pokemon, String start){
        System.out.println(start);
        System.out.println("Name: " + pokemon.getName());
        System.out.println("Leben: " + pokemon.getHPCurrent() + " / " + pokemon.getHPMax());
    }
} 