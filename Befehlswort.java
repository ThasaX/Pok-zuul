
public enum Befehlswort{
    GO("go", "gehen"), 

    QUIT("quit", "verlassen"), 

    HELP("help", "hilfe"),

    LOOK("look", "umsehen"),

    BACK("back", "zurueck"), 

    TAKE("take", "nehme"), 

    DROP("drop", "schmeiße"),

    STATUS("status", "status"),

    EQUIP("equip", "ausrüsten"),

    LOAD("load", "laden"),
    
    BEAM("beam","beam"),
    
    USE("use", "benutze"),
    
    SPEAK("speak", "spreche"),

    LANGUAGE("language", "sprache");

    private String befehlswortEnglisch;
    private String befehlswortDeutsch;
    private static int mode = 1;

    Befehlswort(String befehlswortEnglisch, String befehlswortDeutsch){
        this.befehlswortEnglisch = befehlswortEnglisch;
        this.befehlswortDeutsch = befehlswortDeutsch;
    }

    public String toString(){
        String erg = "";

        if(mode == 1){
            erg = befehlswortEnglisch;
        }
        else if(mode == 2){
            erg = befehlswortDeutsch;
        }

        return erg;
    }

    public static boolean setzeMode(String mode){
        boolean erg = true;

        int modeNumber = (int) mode.charAt(0);

        if(modeNumber != 49 && modeNumber != 50){
            erg = false;
        }
        else if (modeNumber == 49){
            Befehlswort.mode = 1; 
        }
        else{
            Befehlswort.mode = 2;
        }

        return erg;
    }
}
