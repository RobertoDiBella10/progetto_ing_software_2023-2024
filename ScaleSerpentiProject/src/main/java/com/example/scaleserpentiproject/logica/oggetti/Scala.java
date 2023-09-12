package com.example.scaleserpentiproject.logica.oggetti;

@SuppressWarnings("ClassCanBeRecord")
public class Scala{
    private final int piedi;//numero della casella dove si trovano i piedi e la cima di una scala
    private final int cima;

    public Scala (int piedi, int cima) {
        this.piedi = piedi;
        this.cima = cima;
    }

    public int getPiedi() {
        return piedi;
    }

    public int getCima() {
        return cima;
    }

}
