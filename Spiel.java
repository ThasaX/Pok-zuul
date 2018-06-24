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
        alabastia = new Raum("in mitten deiner kleinen Heimatstadt", true, false);
        hausPlayer = new Raum("in deinem zuhause", true, false);
        hausRival = new Raum("in dem Haus von " + nameRival, true, false);
        labor = new Raum("in dem Labor von Professor Eich", true, false);
        see = new Raum("an dem kleinen See von Alabastia", true, false);
        wiese = new Raum("auf einer kleinen Wiese zwischen Alabastia und Route 1 \n Du koenntest auf Pokémon treffen.", false, true);
        routeSouth = new Raum("auf dem suedlichem Teil von Route 1 \n Du koenntest auf Pokémon treffen.", true, true);
        routeNorth = new Raum("auf dem noerdlichen Teil von Route 1 \n Du koenntest auf Pokémon treffen.", true, true);
        routeEnd = new Raum("am Ende von Route 1 \n dein Rivale steht vor dir.", true, false);

        Person eich = new Person("Eich", "Nimm dir ein Pokemon und fordere " + nameRival + " heraus.\n du kriegst aber nur eines entscheide dich gut.", null);
        labor.setPerson(eich);
        Gegenstand glumanda = new Gegenstand(100, "Ein Pokemball in dem sich ein Glumanda befindet", "glumanda");
        Gegenstand schiggy = new Gegenstand(100, "Ein Pokemball in dem sich ein Bisasam befindet", "schiggy");
        Gegenstand bisasam = new Gegenstand(100, "Ein Pokemball in dem sich ein Schiggy befindet", "bisasam");
        labor.gegenstandHinzufuegen(glumanda);
        labor.gegenstandHinzufuegen(schiggy);
        labor.gegenstandHinzufuegen(bisasam);

        Person mom = new Person("Mutter", "Wenn du angeln gehen willst musst du dir den Rucksack von \n" + nameRival + " zuhause holen.", null);
        hausPlayer.setPerson(mom);

        Gegenstand rucksack = new Gegenstand(99, "Ein Item welches deine Traglast erhöht", "rucksack");
        hausRival.gegenstandHinzufuegen(rucksack);        
        hausPlayer.gegenstandHinzufuegen(new Gegenstand(900,"Ein Item, welches man nur am See nutzen kann","angel"));

        Person rivale = new Person(nameRival, "Hallo " + player.gibName() + "jetzt köönen wir endlich entscheiden wer von uns der bessere ist!", null);
        routeEnd.setPerson(rivale);
        
        // die Ausgaenge initialisieren
        alabastia.setzeAusgaenge("zuhause", hausPlayer);
        alabastia.setzeAusgaenge("nachbar", hausRival);
        alabastia.setzeAusgaenge("labor", labor);
        alabastia.setzeAusgaenge("see", see);
        alabastia.setzeAusgaenge("wiese", wiese);
        hausPlayer.setzeAusgaenge("alabastia", alabastia);
        hausRival.setzeAusgaenge("alabastia", alabastia);
        labor.setzeAusgaenge("alabastia", alabastia);
        see.setzeAusgaenge("alabstia", alabastia);
        wiese.setzeAusgaenge("alabastia", alabastia);
        wiese.setzeAusgaenge("north", routeSouth);
        routeSouth.setzeAusgaenge("wiese", wiese);
        routeSouth.setzeAusgaenge("north", routeNorth);
        routeNorth.setzeAusgaenge("south", routeSouth);
        routeNorth.setzeAusgaenge("north", routeEnd);
        routeEnd.setzeAusgaenge("south", routeNorth);

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
            } catch(Exception e){System.out.println(e);}
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
            } catch(Exception e){System.out.println(e);}
        }

        raeumeAnlegen();

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
        else if (befehlswort == Befehlswort.EQUIP)
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
        else if(befehlswort == Befehlswort.USE){
            if(aktuellerRaum == map.get("see")
            && befehl.gibZweitesWort().toLowerCase().equals("angel")
            && player.gibGegenstand("angel") != null){
                if(player.getPokemon() != null){
                    Pokemon karpador = new Pokemon("Karpador", 10, 2, 9);
                    karpador.addAngriff("platscher", new Angriff(0, "Platscher - Schaden: 0, Kein Zusatzeffekt", "Platscher"));
                    fight(karpador);
                    System.out.println("Du hast Karpador besiegt.");
                }
                else{
                    System.out.println("Ich darf nicht ohne Pokémon Angeln.");
                }
            }
            else if (!befehl.gibZweitesWort().toLowerCase().equals("Angel")){
                System.out.println("Das kannst du nicht benutzen");
            }
            else{
                System.out.println("Du kannst jetzt nicht angeln");
            }
        }
        else if(befehlswort == Befehlswort.SPEAK){
            if(aktuellerRaum.getPerson() == null){
                System.out.println("Du fuehrst eine ausgebige Diskussion mit dir selbst. Du verlierst. \n Aber weil du ein Guter Verlierer bist, erkennst du deinen Sieg an.");
            }
            else{
                String text = aktuellerRaum.getPerson().getText();
                String name = aktuellerRaum.getPerson().getName();
                int n = text.length();
                System.out.print(name + ": ");
                for(int i = 0; i < n; i++){
                    System.out.print(text.charAt(i));
                    try{
                        Thread.sleep(10);
                    }catch(Exception e){}
                }
                System.out.println();

                Pokemon temp = aktuellerRaum.getPerson().getPokemon();
                if(temp != null){
                    if(fight(temp)){
                        System.out.println("Du hast verloren. \n Du rennst nachhause und heilst dein Pokemon.");
                        beam(map.get("hausplayer"));
                    }
                    else{
                        System.out.println("Glückwunsch! Du hast deinen Rivalen besiegt\n und dieses Spiel somit durchgespielt!");
                        moechteBeenden = true;
                    }
                }

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
        System.out.println("Du bist ein junge aus Alabastia der schon seit Kinder Tagen einen Rivalen hat. \n Heute kriegt ihr beiden eure ersten Pokémon und könnt endlich entscheiden wer der bessere ist! \n \n Ihnen stehen folgende Befehle zur Verfuegung:");
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

            String richtung = befehl.gibZweitesWort().toLowerCase();
            Raum naechsterRaum = aktuellerRaum.gibAusgang(richtung);

            if (naechsterRaum == null) {
                System.out.println("Dort ist kein Raum!");
            }
            else {
                if(naechsterRaum.isOpen() || (!naechsterRaum.isOpen() &&  player.getPokemon() != null)){
                    if(aktuellerRaum.getHasEncounter()){
                        Pokemon enemy = aktuellerRaum.getRandomEncounter();
                        if(fight(enemy)){
                            System.out.println("Du hast verloren. \n Du rennst nachhause und heilst dein Pokemon.");
                            beam(map.get("hausPlayer"));
                        }
                        else{
                            System.out.println("Du hast den Kampf gewonnen und gehst weiter!");
                        }
                    }

                    letzterRaum.push(aktuellerRaum);
                    aktuellerRaum = naechsterRaum;
                    player.setzeaktuellerRaum(aktuellerRaum);
                    gebeRaumStatus();
                }        
                else{
                    System.out.println("Dort darf ich nur mit einem Pokémon hin");
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
        if(b && aktuellerRaum.gibGegenstand(gegenstand.toLowerCase()) != null){
            Gegenstand temp = aktuellerRaum.gibGegenstand(gegenstand);
            boolean passt = player.addItem(temp);
            if(passt){
                aktuellerRaum.entferneGegenstand(temp);
            }

            if(gegenstand.toLowerCase().equals("glumanda")){
                Pokemon glumanda = new Pokemon("Glumanda", 19, 10, 8);
                glumanda.addAngriff("kratzer",new Angriff(40, "Kratzer - Schaden: 40, Kein Zusatzeffekt", "Kratzer"));
                glumanda.addAngriff("silberblick" ,new Angriff(0, "Silberblick - Schaden: 0, Senkt gegnerische Verteidigung", "Silberblick"));
                player.setPokemon(glumanda);

                System.out.println("Glückwunsch dein Pokemon ist nun Glumanda!");
                
                Pokemon schiggy = new Pokemon("Schiggy", 22, 9, 11);
                schiggy.addAngriff("tackle",new Angriff(40, "Tackle - Schaden: 40, Kein Zusatzeffekt", "Kratzer"));
                schiggy.addAngriff("rutenschlag" ,new Angriff(0, "Rutenschlag - Schaden: 0, Senkt gegnerische Verrteidigung", "Silberblick"));
                map.get("routeEnd").getPerson().setPokemon(schiggy);

                aktuellerRaum.removeAllItems();
            }
            else if(gegenstand.toLowerCase().equals("schiggy")){
                Pokemon schiggy = new Pokemon("Schiggy", 22, 9, 11);
                schiggy.addAngriff("tackle",new Angriff(40, "Tackle - Schaden: 40, Kein Zusatzeffekt", "Kratzer"));
                schiggy.addAngriff("rutenschlag" ,new Angriff(0, "Rutenschlag - Schaden: 0, Senkt gegnerische Verrteidigung", "Silberblick"));
                player.setPokemon(schiggy);

                Pokemon bisasam = new Pokemon("Bisasam", 22, 9, 9);
                bisasam.addAngriff("tackle",new Angriff(40, "Tackle - Schaden: 40, Kein Zusatzeffekt", "Kratzer"));
                bisasam.addAngriff("heuler" ,new Angriff(0, "Heuler - Schaden: 0, Senkt gegnerischen Angriff", "Heuler"));
                map.get("routeEnd").getPerson().setPokemon(bisasam);
                
                System.out.println("Glückwunsch dein Pokemon ist nun Schiggy!");
                aktuellerRaum.removeAllItems();
            }
            else if(gegenstand.toLowerCase().equals("bisasam")){
                Pokemon bisasam = new Pokemon("Bisasam", 22, 9, 9);
                bisasam.addAngriff("tackle",new Angriff(40, "Tackle - Schaden: 40, Kein Zusatzeffekt", "Kratzer"));
                bisasam.addAngriff("heuler" ,new Angriff(0, "Heuler - Schaden: 0, Senkt gegnerischen Angriff", "Heuler"));
                player.setPokemon(bisasam);

                Pokemon glumanda = new Pokemon("Glumanda", 19, 10, 8);
                glumanda.addAngriff("kratzer",new Angriff(40, "Kratzer - Schaden: 40, Kein Zusatzeffekt", "Kratzer"));
                glumanda.addAngriff("silberblick" ,new Angriff(0, "Silberblick - Schaden: 0, Senkt gegnerische Verteidigung", "Silberblick"));
                map.get("routeEnd").getPerson().setPokemon(glumanda);
                
                System.out.println("Glückwunsch dein Pokemon ist nun Bisasam!");
                aktuellerRaum.removeAllItems();
            }
            else if (passt){
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
        if (player.gibGegenstand("rucksack") == gegenstand){
            player.removeItem(gegenstand);
            player.setzeMaxGewicht(9999);
            System.out.println("Rucksack wurde ausgerüstet.");
        }
        else{
            System.out.println("Dieser Gegenstand kann nicht ausgerüstet werden");
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
        System.out.println("Ein kampf hat angefangen");

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
                    if(temp.getName().toLowerCase().equals("silberblick") 
                    || temp.getName().toLowerCase().equals("rutenschlag")){
                        defEnemy *= 0.75;
                        System.out.println("Das gegnerische Pokémon hat nun eine gesenkte Verteidigung.");
                    }
                    else if(temp.getName().toLowerCase().equals("heuler")){
                        atkEnemy *= 0.75;
                        System.out.println("Das gegnerische Pokémon hat nun einen gesenkten Angriff.");
                    }
                }
                else{
                    System.out.println("Das gegnerische Pokémon hat schaden bekommen.");
                    enemy.setHPCurrent(enemy.getHPCurrent()-atkPlayer*temp.getDamage()/defEnemy/10);
                }

                if(enemy.getHPCurrent() <=0){
                    done = true;
                    erg = false;
                }

                if(gegnerAngriff.getDamage() == 0){
                    if(gegnerAngriff.getName().toLowerCase().equals("silberblick") 
                    || gegnerAngriff.getName().toLowerCase().equals("rutenschlag")){
                        defPlayer *= 0.75;
                        System.out.println("Dein Pokémon hat nun eine gesenkte Verteidigung.");
                    }
                    else if(gegnerAngriff.getName().toLowerCase().equals("heuler")){
                        System.out.println("Dein Pokémon hat nun einen gesenkten Angriff.");
                        atkPlayer *= 0.75;
                    }
                    else if(gegnerAngriff.getName().toLowerCase().equals("platscher")){
                        System.out.println("Gegner setzt Platscher ein... Nichts passiert");
                    }
                }
                else{
                    System.out.println("Dein Pokémon hat schaden bekommen.");
                    player.getPokemon().setHPCurrent(player.getPokemon().getHPCurrent()-atkEnemy*gegnerAngriff.getDamage()/defPlayer/10);
                }

                if(player.getPokemon().getHPCurrent() <=0){
                    done = true;
                    erg = true;
                }
            }catch(Exception e){System.out.println(e);}
        }

        player.getPokemon().setHPCurrent(player.getPokemon().getHPMax());
        enemy.setHPCurrent(enemy.getHPMax());
        
        System.out.println("\n");
        
        return erg;
    }

    public void printFightInformation(Pokemon pokemon, String start){
        System.out.println(start);
        System.out.println("Name: " + pokemon.getName());
        System.out.println("Leben: " + pokemon.getHPCurrent() + " / " + pokemon.getHPMax() + "\n");
    }
} 