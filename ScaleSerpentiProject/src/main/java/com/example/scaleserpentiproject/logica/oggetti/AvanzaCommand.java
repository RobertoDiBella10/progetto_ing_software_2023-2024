package com.example.scaleserpentiproject.logica.oggetti;

public class AvanzaCommand implements Command{
    private final Giocatore g;
    private final Tabellone tabellone;
    private final int numCaselle;

    public AvanzaCommand(Giocatore g,int numCaselle,Tabellone t){
        if(numCaselle<1)
            throw new IllegalArgumentException("Il giocatore non può avanzare di un numero negativo di caselle");
        this.g=g;
        this.numCaselle=numCaselle;
        this.tabellone=t;

    }

    @Override
    public void execute() {
        int n=tabellone.getNumCol() * tabellone.getNumRighe();
        if(numCaselle>(n-g.getPosizione())){
            int scarto= numCaselle-(n-g.getPosizione());
            g.setPosizione(g.getPosizione()+(n-g.getPosizione()));
            g.setPosizione(g.getPosizione()-scarto);
        }else{
            g.muovi(numCaselle);
        }
        System.out.println("Il giocatore "+g.getNome()+" deve avanzare di "+numCaselle+" caselle");
        System.out.println("Il giocatore con nome "+g.getNome()+" avanza alla casella con id "+ g.getPosizione());
    }
}
