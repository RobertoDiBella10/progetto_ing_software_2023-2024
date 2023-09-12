package com.example.scaleserpentiproject.logica.oggetti;

import com.example.scaleserpentiproject.logica.caselle.Casella;
import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionException;
import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionRowException;
import com.example.scaleserpentiproject.logica.exceptions.OccupedBoxException;

import java.util.ArrayList;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class GiocoFacade {

    private Partita partita;
    private final Tabellone tabellone;
    private TabelloneMemento tabelloneMemento;
    private MazzoCarte mazzo;

    public GiocoFacade(int numRighe, int numColonne){
        tabellone = Tabellone.getInstance();
        tabellone.creaTabellone(numRighe,numColonne);

    }

    public void creaPartita(int numGiocatori, int numDadi){
        partita=new Partita(tabellone,numGiocatori,numDadi);
    }

    public void simula(){
        partita.simula();
    }

    public void avanza(Giocatore g){
        partita.avanza(g);
    }

    public void addCasellaSpeciale(int numCasella, Casella casellaSpeciale) throws OccupedBoxException, IllegalPositionException {
        tabellone.addCasellaSpeciale(numCasella, casellaSpeciale);
    }

    public void removeElement(int numCasella) throws OccupedBoxException, IllegalPositionException {
        tabellone.removeElement(numCasella);
    }

    public void addScala(Scala s) throws IllegalPositionRowException, IllegalPositionException, OccupedBoxException {
        tabellone.addScala(s);
    }



    public void addSerpente(Serpente s) throws IllegalPositionRowException, IllegalPositionException, OccupedBoxException {
        tabellone.addSerpente(s);
    }


    public TabelloneMemento salvaStato() {
        tabelloneMemento = tabellone.salvaStato();
        return tabelloneMemento;
    }

    public void caricaStato(TabelloneMemento memento) {
        tabellone.caricaStato(memento);
    }

    public int[] trovaPosizioneCasella(int numCasella){
        return tabellone.trovaPosizioneCasella(numCasella);
    }

    public ArrayList<Giocatore> getGiocatori(){
        return partita.getGiocatori();
    }

    public int getNumCaselle(){
        return tabellone.getNumCaselle();
    }

    public Casella casellaCorrente(Giocatore g) {
        return partita.casellaCorrente(g);
    }

    public MazzoCarte creaMazzo(){
        mazzo = MazzoCarte.getInstance();
        mazzo.createMazzo();
        return mazzo;
    }

    public Partita getPartita(){
        return this.partita;
    }

    public Tabellone getTabellone(){
        return this.tabellone;
    }
}
