package com.example.scaleserpentiproject.logica.oggetti;

import java.util.ArrayList;
import java.util.List;

public class CareTaker {
    private final Tabellone tabellone;
    private final List<TabelloneMemento> history;

    public CareTaker(Tabellone t){
        history = new ArrayList<>();
        this.tabellone=t;
    }

    public void addMemento(TabelloneMemento memento) {
        if(memento == null)
            throw new IllegalArgumentException("Memento inserito nullo");
        history.add(memento);
    }

    public TabelloneMemento ultimoMemento() {
        if(history.size() == 0)
            throw new IndexOutOfBoundsException();
        return history.get(history.size()-1);
    }

    public void undo(){
        if(history.size() < 2)
            throw new IllegalArgumentException("Impossibile fare undo su una lista con un numero di elementi inferiori a 1");
        history.remove(history.size()-1);
        tabellone.caricaStato(this.ultimoMemento());
    }

    public int getDimensione(){
        return history.size();
    }
}
