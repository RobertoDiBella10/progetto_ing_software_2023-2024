package com.example.scaleserpentiproject.logica.oggetti;

import com.example.scaleserpentiproject.logica.caselle.Tipo;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MazzoCarte {
    private static MazzoCarte instance=null;
    private List<Tipo> mazzoCarte;
    private MazzoCarte() {
    }

    public synchronized static MazzoCarte getInstance() {
        if (instance == null) {
            instance = new MazzoCarte();
        }
        return instance;
    }

    public void createMazzo(){
        mazzoCarte = new LinkedList<>();
        mazzoCarte.add(Tipo.DADI);
        mazzoCarte.add(Tipo.MOLLA);
        mazzoCarte.add(Tipo.LOCANDA);
        mazzoCarte.add(Tipo.DADI);
        mazzoCarte.add(Tipo.MOLLA);
        mazzoCarte.add(Tipo.LOCANDA);
        mazzoCarte.add(Tipo.PANCHINA);
        mazzoCarte.add(Tipo.DADI);
        mazzoCarte.add(Tipo.MOLLA);
        mazzoCarte.add(Tipo.DIVIETOSOSTA);
        mazzoCarte.add(Tipo.LOCANDA);
        mazzoCarte.add(Tipo.PANCHINA);
        mazzoCarte.add(Tipo.DADI);
        mazzoCarte.add(Tipo.MOLLA);
        mazzoCarte.add(Tipo.DADI);
        mazzoCarte.add(Tipo.DIVIETOSOSTA);
        mazzoCarte.add(Tipo.MOLLA);
        mazzoCarte.add(Tipo.LOCANDA);
        mazzoCarte.add(Tipo.PANCHINA);
        mazzoCarte.add(Tipo.LOCANDA);
        mazzoCarte.add(Tipo.PANCHINA);
        mazzoCarte.add(Tipo.DIVIETOSOSTA);
        mazzoCarte.add(Tipo.PANCHINA);
        mescolaMazzo();
    }

    public Tipo pescaCarta(Giocatore giocatore) {
        Tipo carta = mazzoCarte.remove(0);
        if(carta==Tipo.DIVIETOSOSTA)
            giocatore.setCartaDivietoSosta(true);
        return carta;
    }

    public void riponiNelMazzo(Giocatore g,Tipo carta){
        mazzoCarte.add(carta);
        if(carta==Tipo.DIVIETOSOSTA)
            g.setCartaDivietoSosta(false);
    }

    private void mescolaMazzo() {
        long seed = System.nanoTime();
        Collections.shuffle(mazzoCarte, new Random(seed));
    }
}
