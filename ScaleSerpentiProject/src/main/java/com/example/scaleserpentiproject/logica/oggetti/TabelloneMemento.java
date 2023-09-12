package com.example.scaleserpentiproject.logica.oggetti;

import com.example.scaleserpentiproject.logica.caselle.Casella;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("ALL")
public class TabelloneMemento implements Serializable {
    private final int numRighe;
    private final int numCol;
    private final Map<Integer,Serpente> serpenti;
    private final Map<Integer,Scala> scale;
    private Map<Integer,Casella> caselleSpeciali;

    public TabelloneMemento(int numRighe, int numCol, Map<Integer,Serpente> serpenti,
                            Map<Integer,Scala> scale, Map<Integer,Casella> caselleSpeciali) {
        this.numRighe = numRighe;
        this.numCol = numCol;
        this.serpenti = new HashMap<>(serpenti);
        this.scale = new HashMap<>(scale);
        this.caselleSpeciali = new HashMap<>(caselleSpeciali);
    }

    public int getNumRighe() {
        return numRighe;
    }

    public int getNumCol() {
        return numCol;
    }

    public Map<Integer, Serpente> getSerpenti() {
        return serpenti;
    }

    public Map<Integer, Scala> getScale() {
        return scale;
    }

    public Map<Integer, Casella> getCaselleSpeciali() {
        return caselleSpeciali;
    }

    private Casella[][] copiaTabellone(Casella[][] tabellone){
        Casella[][] ris = new Casella[tabellone.length][tabellone[0].length];
        for (int row = 0; row < ris.length; row++) {
            for (int col = 0; col < ris[0].length; col++) {
                ris[row][col] = tabellone[row][col];
            }
        }
        return ris;
    }
}