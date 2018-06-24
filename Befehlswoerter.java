import java.util.HashMap;
import java.util.Set;

class Befehlswoerter
{
    private HashMap<String, Befehlswort> gueltigeBefehle;

    public Befehlswoerter(){
        gueltigeBefehle = new HashMap<String, Befehlswort>();
        gueltigeBefehle.put("QUIT", Befehlswort.QUIT);
        gueltigeBefehle.put("GO", Befehlswort.GO);
        gueltigeBefehle.put("TAKE", Befehlswort.TAKE);
        gueltigeBefehle.put("DROP", Befehlswort.DROP);
        gueltigeBefehle.put("BACK", Befehlswort.BACK);
        gueltigeBefehle.put("EQUIP", Befehlswort.EQUIP);
        gueltigeBefehle.put("STATUS", Befehlswort.STATUS);
        gueltigeBefehle.put("HELP", Befehlswort.HELP);
        gueltigeBefehle.put("LOOK", Befehlswort.LOOK);
        gueltigeBefehle.put("BEAM", Befehlswort.BEAM);
        gueltigeBefehle.put("LOAD", Befehlswort.LOAD);
        gueltigeBefehle.put("USE", Befehlswort.USE);
        gueltigeBefehle.put("SPEAK", Befehlswort.SPEAK);
    }

    public boolean istBefehl(String eingabe)
    {
        boolean exist = false;
        Befehlswort temp = gueltigeBefehle.get(eingabe);
        if(temp != null){
            exist = true;
        }
        return exist;
    }

    public void alleAusgeben()
    {
        Set<String> keys = gueltigeBefehle.keySet();

        for(String key:keys) {
            System.out.print(gueltigeBefehle.get(key)+" ");
        }
        System.out.println();

    }

    public Befehlswort gibBefehlswort(String wort){
        return gueltigeBefehle.get(wort);
    }
}