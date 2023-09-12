package com.example.scaleserpentiproject.logica.oggetti;

import com.example.scaleserpentiproject.logica.caselle.Casella;
import com.example.scaleserpentiproject.logica.dado.Dado;

import java.util.ArrayList;

@SuppressWarnings("FieldCanBeLocal")
public class Partita {
    private final Tabellone tabellone;
    private final ArrayList<Giocatore> giocatori;
    private final MazzoCarte mazzo;
    private final int n;
    private Command comando;
    private final Dado d1=new Dado();
    private Dado d2;
    private boolean continua;

    public Partita(Tabellone t, int numGiocatori, int numDadi){
        if(t==null || numGiocatori<2 || numDadi<1 || numDadi>2)
            throw new IllegalArgumentException("Inserire un numero di giocatori maggiore di 1 e un numero di dadi compreso tra 1 e 2");
        this.tabellone=t;
        giocatori=new ArrayList<>(numGiocatori);
        for(int i=0;i<numGiocatori;i++){
            Giocatore g=new Giocatore("Giocatore "+(i+1));
            giocatori.add(g);
        }
        if(numDadi==2)
            d2=new Dado();
        mazzo=MazzoCarte.getInstance();
        mazzo.createMazzo();
        n=tabellone.getNumCol()* tabellone.getNumRighe();
        continua = true;
    }

    public void simula(){
        while(true){
            for(Giocatore g:giocatori){
                if(g.getTurniStop()==0){
                    avanza(g);
                }else{
                    g.setTurniStop(g.getTurniStop()-1);
                    System.out.println("Il giocatore "+g.getNome()+" è fermo per altri: "+g.getTurniStop());
                }
                if(g.getPosizione()==n){
                    System.out.println("Il giocatore "+g.getNome()+" ha vinto");
                    return;
                }
            }
        }
    }

    public void avanzaDelLancioPrecedente(Giocatore g, int lancioPrecedente){
        comando=new AvanzaCommand(g,lancioPrecedente,tabellone);
        comando.execute();
    }

    public void avanza(Giocatore g){
        if(continua) {
            if (g == null)
                throw new IllegalArgumentException("Giocatore inesistente");
            if (g.getTurniStop() == 0) {
                int dado1 = d1.lancioDado();
                g.setUltimoLancio(dado1);
                if (g.getPosizione() >= (n - 6) && g.getPosizione() <= (n - 1)) {
                    comando = new AvanzaCommand(g, dado1, tabellone);
                    comando.execute();
                    if (g.getPosizione() == n) {
                        System.out.println("Il giocatore " + g.getNome() + " ha vinto");
                        continua=false;
                    }
                    return;
                }

                if (d2 != null) {
                    int dado2 = d2.lancioDado();
                    int sommadadi = dado2 + dado1;
                    g.setUltimoLancio(sommadadi);
                    comando = new AvanzaCommand(g, sommadadi, tabellone);
                } else {
                    comando = new AvanzaCommand(g, dado1, tabellone);
                }
                comando.execute();

            } else {
                g.setTurniStop(g.getTurniStop() - 1);
                System.out.println("Il giocatore " + g.getNome() + " è fermo per altri: " + g.getTurniStop());
            }
            if (g.getPosizione() == n) {
                System.out.println("Il giocatore " + g.getNome() + " ha vinto");
                continua=false;
            }
        }
    }

    public Casella casellaCorrente(Giocatore g) {
        Casella[][] caselle = tabellone.getCaselle();
        int[] pos = tabellone.trovaPosizioneCasella(g.getPosizione());
        return caselle[pos[0]][pos[1]];
    }

    public ArrayList<Giocatore> getGiocatori() {
        return giocatori;
    }
}