package com.example.scaleserpentiproject.logica.oggetti;

@SuppressWarnings("ClassCanBeRecord")
public class Serpente{
    private final int testa;//numero delle caselle dove si trova la testa e la coda del serpente
    private final int coda;

    public Serpente(int testa, int coda) {
        this.testa = testa;
        this.coda = coda;
    }

    public int getTesta() {
        return testa;
    }

    public int getCoda() {
        return coda;
    }

}
