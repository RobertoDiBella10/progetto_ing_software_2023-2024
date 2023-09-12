package com.example.scaleserpentiproject.logica.dado;
import java.util.Random;

public class Dado {

    private final int numFacce;
    private final Random rand;

    public Dado(){
        this.numFacce=6;
        rand=new Random();
    }
    public int lancioDado() {
        return rand.nextInt(numFacce) + 1; // generazione di un numero casuale tra 1 e numFacce incluso
    }
}
